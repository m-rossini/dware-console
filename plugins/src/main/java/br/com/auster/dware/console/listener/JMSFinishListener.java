/*
 * Copyright (c) 2004 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Apr 11, 2005
 */
package br.com.auster.dware.console.listener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import br.com.auster.common.log.LogFactory;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.model.OutputFile;
import br.com.auster.facelift.requests.model.Request;
import br.com.auster.facelift.requests.model.Trail;
import br.com.auster.facelift.requests.web.interfaces.ResumeCriteria;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.persistence.PersistenceException;

/**
 * @ejb.bean name="FinishListener"
 *           destination-jndi-name="queue/procNotification"
 *           destination-type="javax.jms.Queue" transaction-type="Bean"
 * 
 * @jboss.destination-jndi-name name="queue/procNotification"
 * 
 * @weblogic.message-driven connection-factory-jndi-name="billcheckout.QueueConnectionFactory"
 *                          destination-jndi-name="queue/procNotification"
 * 
 * @author framos
 * @version $Id: JMSFinishListener.java 572 2007-08-21 21:05:39Z framos $
 */
public class JMSFinishListener implements MessageDrivenBean, MessageListener {

	public static final String	 FINISHLISTENER_MESSAGE_KEY	= "jms.requestFinished";

	private MessageDrivenContext context;
	private Logger log = LogFactory.getLogger(JMSFinishListener.class);

	
	public void setMessageDrivenContext(MessageDrivenContext _context) throws EJBException {
		context = _context;
	}

	public void ejbCreate() {
	}

	public void ejbRemove() throws EJBException {
	}

	/**
	 * @ejb.transaction type="Never"
	 */
	public void onMessage(Message _message) {

		if (!(_message instanceof ObjectMessage)) {
			log.error("message sent is not of type ObjectMessage");
			throw new IllegalArgumentException("invalid argument for MDB");
		}
		long startTime = Calendar.getInstance().getTimeInMillis();
		try {
			Object message = ((ObjectMessage) _message).getObject();
			if ((message != null) && (message instanceof RequestFinished)) {
				RequestFinished reqFinished = (RequestFinished) message;
				WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
				log.debug("JMS finish listener partial time = "
				    + (Calendar.getInstance().getTimeInMillis() - startTime) + "ms");
				// Creating processing request == ACCOUNT information
				Request procRequest = new Request();
				procRequest.setLabel(reqFinished.getAccountId());
				procRequest.setLatestStatus(reqFinished.getStatus());
				log.debug("received notification of status change for proc. request "
				    + procRequest.getRequestId() + " in web request " + reqFinished.getRequestId());
				// Saving trail information
				Trail trail = new Trail();
				trail.setMessage(reqFinished.getMessage());
				trail.setStatus(reqFinished.getStatus());
				trail.setTrailDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				List trails = procRequest.getTrails();
				if (trails == null) {
					procRequest.setTrails(new ArrayList());
				}
				procRequest.getTrails().add(trail);
				// If there was some file generated, attach it to processing request
				if (reqFinished.getFilenames() != null) {
					for (Iterator it = reqFinished.getFilenames(); it.hasNext();) {
						String filename = (String) it.next();
						OutputFile file = new OutputFile();
						//file.setTrail(trail);
						file.setFilename(filename);
						file.setAttributes(new HashMap());
						file.getAttributes().put(reqFinished.getFormat(filename), "true");
						log.debug("Format " + reqFinished.getFormat(filename) + " returned with file " + filename);
						trail.getOutputFiles().add(file);
					}
				}
				manager.createRequest(procRequest);
				// inserting into web_request_requests
				manager.createRequestLink(reqFinished.getRequestId(), procRequest);

			} else if ((message != null) && (message instanceof ResumeCriteria)) {
				resumeRequests((ResumeCriteria) message);
			} else {
				log.error("message is either not and object or is not a valid RequestFinished instance : " + message);
				throw new IllegalArgumentException("argument sent to MDB is not an instance of RequestFinished");
			}
		} catch (RequestManagerException rme) {
			log.error("Error while executing business logic", rme);
		} catch (JMSException jmse) {
			log.error("JMS exception", jmse);
		} catch (PersistenceException prae) {
			log.error("Error while acessing persistence layer", prae);
		} finally {
			long endTime = Calendar.getInstance().getTimeInMillis();
			log.debug("JMS finish listener execution time = " + (endTime - startTime) + "ms");
		}
	}

	private OutputFile[] createOutputFiles(Trail _trail, RequestFinished _files) {

		ArrayList outFiles = new ArrayList();
		Iterator iterator = _files.getFilenames();
		while (iterator.hasNext()) {
			String filename = (String) iterator.next();

			OutputFile file = new OutputFile();
			file.setFilename(filename);
			file.setTrail(_trail);

			Map formats = new HashMap();
			formats.put(_files.getFormat(filename), "true");
			file.setAttributes(formats);

			outFiles.add(file);
		}
		return (OutputFile[]) outFiles.toArray(new OutputFile[] {});
	}

	private void resumeRequests(ResumeCriteria _criteria) throws PersistenceException,
	    RequestManagerException {
		WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(
		    ServiceConstants.REQUESTMANAGER_SERVICE);
		manager.resumeUnfinishedRequests(_criteria);
	}
}
