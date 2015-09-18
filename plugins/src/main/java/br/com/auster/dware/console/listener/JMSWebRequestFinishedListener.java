/*
 * Copyright (c) 2004 TTI Tecnologia. All Rights Reserved.
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
 * Created on Jun 10, 2005
 */
package br.com.auster.dware.console.listener;

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
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.persistence.PersistenceResourceAccessException;

/**
 * @ejb.bean name="JMSWebRequestFinishedListener" 
 * 			 destination-jndi-name="topic/webRequestFinished"
 *           destination-type="javax.jms.Topic" 
 *           transaction-type="Bean"
 *
 * @jboss.destination-jndi-name 
 *           name="topic/webRequestFinished"
 * 
 * @weblogic.message-driven
 *           connection-factory-jndi-name="billcheckout.QueueConnectionFactory"
 *           destination-jndi-name="topic/webRequestFinished"         
 * 
 * @author framos
 * @version $Id: JMSRequestBuilderListener.java 258 2006-09-13 15:46:35Z framos $
 */
public class JMSWebRequestFinishedListener implements MessageDrivenBean,
		MessageListener {

	private MessageDrivenContext context;

	private Logger log = LogFactory.getLogger(JMSWebRequestFinishedListener.class);

	public void setMessageDrivenContext(MessageDrivenContext _context)
			throws EJBException {
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

		try {
			ObjectMessage message = (ObjectMessage) _message;
			if (message != null) {

				long requestId = message.getLongProperty("transactionId");
				int status = message.getIntProperty("status");

				WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(
								ServiceConstants.REQUESTMANAGER_SERVICE);

				log.debug("Updating WebRequest " + requestId + " with status " + status);
				manager.updateWebRequestStatus(requestId, status);

			} else {
				log.error("message is either not and object or is not a valid RequestCreation instance");
				throw new IllegalArgumentException("argument sent to MDB is not an instance of RequestCreation");
			}
		} catch (JMSException jmse) {
			log.error("exception while handling message", jmse);
		} catch (PersistenceResourceAccessException prae) {
			log.error("exception while acessing persistence storage", prae);
		} catch (RequestManagerException rme) {
			log.error("exception creating the web request", rme);
		}
	}
}
