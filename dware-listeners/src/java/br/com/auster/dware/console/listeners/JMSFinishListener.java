/*
 * Copyright (c) 2004 TTI Tecnologia. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Created on Sep 20, 2004
 */
package br.com.auster.dware.console.listeners;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import br.com.auster.common.log.LogFactory;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.console.listener.RequestFinished;
import br.com.auster.dware.graph.FinishListener;
import br.com.auster.dware.graph.Graph;
import br.com.auster.dware.graph.Request;
import br.com.auster.facelift.requests.interfaces.RequestLifeCycle;



/**
 * @author Ricardo Barone
 * @version $Id: RequestFinishListener.java,v 1.1 2004/11/08 15:54:21 rbarone
 *          Exp $
 */
public class JMSFinishListener implements FinishListener {

	private static final String JMS_ERROR_MESSAGE = "Exception occured while trying to send notification to remote queue.";
	private static final String JMS_RETRY_MESSAGE = " Will try one more time...";
	private static final String JMS_FATAL_MESSAGE = " Tried twice...giving up!";

  
	private Queue queue;
	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private QueueSender queueSender;

	private Element configuration;
	
	private Logger log = LogFactory.getLogger(JMSFinishListener.class);

  
  
	public JMSFinishListener(Element _configuration) {
		if (_configuration == null) {
			throw new IllegalStateException("cannot connect to JMS queue without queue configuration parameters");
		}
		this.configuration = _configuration;
		this.connectToQueue();
	}
  
	private void connectToQueue() {
		try {
			Properties p = new Properties();
			p.put(Context.INITIAL_CONTEXT_FACTORY, DOMUtils.getAttribute(configuration, "jndi.context.factory", true));
			log.debug("configured parameter [jndi.context.factory] = " + DOMUtils.getAttribute(configuration, "jndi.context.factory", true));
			p.put(Context.URL_PKG_PREFIXES, DOMUtils.getAttribute(configuration, "jndi.context.package", false));
			log.debug("configured parameter [jndi.context.package] = " + DOMUtils.getAttribute(configuration, "jndi.context.package", false));
			p.put(Context.PROVIDER_URL, DOMUtils.getAttribute(configuration, "jndi.context.url", true));
			log.debug("configured parameter [jndi.context.url] = " + DOMUtils.getAttribute(configuration, "jndi.context.url", true));
			Context jndiContext = new InitialContext(p);
			QueueConnectionFactory ref = (QueueConnectionFactory) jndiContext.lookup(DOMUtils.getAttribute(configuration, "jndi.jms.factory", true));
			log.debug("JMS factory is " + DOMUtils.getAttribute(configuration, "jndi.jms.factory", true));
			queue = (Queue) jndiContext.lookup(DOMUtils.getAttribute(configuration, "jndi.jms.name", true));
			log.debug("initializing JMS queue " + DOMUtils.getAttribute(configuration, "jndi.jms.name", true));
			queueConnection = ref.createQueueConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queueSender = queueSession.createSender(queue);
			log.debug("JMS queue session acquired");
		} catch (Exception e) {
			
			log.fatal("Could not initialize RequestFinishListener!", e);
			throw new RuntimeException(e);
		}
	}

	public void graphFinished(Graph _graph, Request _request, Throwable _error, Date _datetime) {

		if ((_request.getTransactionId() == null) || (_request.getTransactionId().length() <= 0)) {
			log.warn("cannot send to web portal a request notification without transaction id", _error);
			return;
		}
			
		RequestFinished result = new RequestFinished(); 
		result.setRequestId(Long.parseLong(_request.getTransactionId()));
		result.setAccountId(_request.getUserKey());
		log.debug("preparing message to request " + result.getRequestId() + ", account " + result.getAccountId());
		// by default, request is OK
		result.setStatus(RequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_OK);
		log.debug("start analysing request atributes");
		Iterator it = _request.getAttributes().keySet().iterator();
		while (it.hasNext()) {
			String attr = (String) it.next();		
			log.debug("checking attribute " + attr);
            if (! attr.equals("format")) {
                continue;
            }
			// check for error messages
			if (_error != null) {
				result.setStatus(RequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_ERROR);
				result.setMessage(_error.getLocalizedMessage());
				log.debug("sending error notification : " + _error.getLocalizedMessage());
			}
			Map generatedFiles = (Map) _request.getAttributes().get("generatedFiles");
			List formats = (List) _request.getAttributes().get(attr);
			Iterator iterator = formats.iterator();
			while ((iterator.hasNext()) && (generatedFiles != null)) {
				String format = (String) iterator.next();
				String filename = (String) generatedFiles.get(format);
				if (filename != null) {
					result.addFilename(filename, format);
					log.debug("setting file " + filename + " as result for format " + format);
				} else { 
					log.debug("no generated file was found for format " + format);
				}
			}
		}
		log.debug("done analysing request atributes");
	
		// Try twice to send message - exit if succeeds at first attempt
		boolean isSecondTime = false;
		while (! isSecondTime) {
			try {
				send(result);
				break;
			} catch (NamingException ne) {
				if (isSecondTime) {
					log.fatal(JMS_ERROR_MESSAGE + JMS_FATAL_MESSAGE, ne);
					throw new RuntimeException(JMS_ERROR_MESSAGE, ne);
				} else {
					log.error(JMS_ERROR_MESSAGE + JMS_RETRY_MESSAGE, ne);
					this.connectToQueue();
					isSecondTime = true;
				}
			} catch (JMSException jmse) {
				if (isSecondTime) {
					log.fatal(JMS_ERROR_MESSAGE + JMS_FATAL_MESSAGE, jmse);
					throw new RuntimeException(JMS_ERROR_MESSAGE, jmse);
				} else {
					log.error(JMS_ERROR_MESSAGE + JMS_RETRY_MESSAGE, jmse);
					this.connectToQueue();
					isSecondTime = true;
				}
			}
		}
	}

	public void graphCommiting(Graph _graph, Request _request) throws Exception {
		// do nothing. finish will do all the job
	}

    public void graphRollingBack(Graph _graph, Request _request, Throwable _error) throws Exception {
		// do nothing. finish will do all the job
    }

	public void send(Serializable _filter) throws NamingException, JMSException {
		ObjectMessage msg = queueSession.createObjectMessage();	  
		queueConnection.start();
		msg.setObject(_filter);
		log.debug("creating JMS object message");
		queueSender.send(msg);
		log.debug("message sent");
	}
}
