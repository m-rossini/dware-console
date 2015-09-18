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
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.security.model.User;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;

/**
 * @ejb.bean name="RequestBuilderListener"
 * 			 destination-jndi-name="queue/createRequest"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Bean"
 *
 * @jboss.destination-jndi-name
 *           name="queue/createRequest"
 *
 * @weblogic.message-driven
 *           connection-factory-jndi-name="billcheckout.QueueConnectionFactory"
 *           destination-jndi-name="queue/createRequest"
 *
 * @author framos
 * @version $Id: JMSRequestBuilderListener.java 640 2008-09-18 13:44:50Z framos $
 */
public class JMSRequestBuilderListener implements MessageDrivenBean, MessageListener {



    private MessageDrivenContext context;
    private Logger log = LogFactory.getLogger(JMSRequestBuilderListener.class);



    public void setMessageDrivenContext(MessageDrivenContext _context) throws EJBException {
        context = _context;
    }

    public void ejbCreate() {}

    public void ejbRemove() throws EJBException {
    }


	/**
	 * @ejb.transaction type="Never"
	 */
    public void onMessage(Message _message) {

		try {
			Object message =  ((ObjectMessage)_message).getObject();
	        if ((message != null) && (message instanceof RequestCreation)) {

				RequestCreation createToken = (RequestCreation) message;

				UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
				User userInfo = userManager.loadUserDetails(createToken.getUserEmail());

				WebRequest request = createToken.getRequestToCreate();
				request.setOwnerId(userInfo.getUid());

                WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
				manager.createWebRequest(request);

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
		} catch (SecurityException se) {
			log.error("exception acessing user information", se);
		}
    }
}
