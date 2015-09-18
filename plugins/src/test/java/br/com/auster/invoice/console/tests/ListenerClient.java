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
package br.com.auster.invoice.console.tests;

import java.util.Properties;

import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;

import br.com.auster.dware.console.listener.RequestFinished;


public class ListenerClient {


    public static void main(String args[]) {

        try {
			
			if (args.length < 4) {
				System.out.println("Usage> ListenerClient <requestId> <accountId> <status> <message> ");
				System.exit(1);
			}
			
            Context jndiContext = getInitialContext();
            QueueConnectionFactory ref =
                    (QueueConnectionFactory)jndiContext.lookup("billcheckout/QueueConnectionFactory");
            // Queues are always accessed by "queue/<<queueName>>" and
            // topics by "topic/<<topicName>>"
            Queue queue = (Queue)jndiContext.lookup("queue/procNotification");
            QueueConnection queueConnection = ref.createQueueConnection();
            QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            // The temporary queue, which this servlet listens to, which
            // it will get a reply from the bean.
            QueueSender queueSender = queueSession.createSender(queue);
            ObjectMessage msg = queueSession.createObjectMessage();
			
			long id = Long.parseLong(args[0]);
			String accId = args[1].trim();
			int status = Integer.parseInt(args[2]);
			String msgText = args[3].trim();
			
			System.out.println("Seding message for requestId = " + id + " accountId = '" + accId + " status = " + status + " message = '" + msg + "'");
			
            msg.setObject(createFinishObject(id, accId, status, msgText));
            queueConnection.start();
            queueSender.send(msg);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Method for getting the context in which the lookup for
     * queues or connections is performed.
     *
     * @return The {@link Context} object.
     * @throws javax.naming.NamingException
     */
    public static Context getInitialContext() throws javax.naming.NamingException {
        //return new InitialContext();
        // By returning a new and empty InitalContext object the JNDI references
        // from a JNDI.PROPERTIES file in javas classpath will be used.
        // It is not recommended to hard code the references here if not
        // absolutely necessary, e.g. if you want a servlet to communicate with a
        // bean on running on a remote computer.
        /**** context initialized by jndi.properties file **/
         Properties p = new Properties();
//         p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
//         p.put(Context.URL_PKG_PREFIXES, "jboss.naming:org.jnp.interfaces");
//         p.put(Context.PROVIDER_URL, "localhost:1099");
 		 p.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
 		 p.put("java.naming.provider.url","t3://localhost:7001");
         return new javax.naming.InitialContext(p);
    }


    public static RequestFinished createFinishObject(long _requestId, String _accountId, int _status, String _message) {
        RequestFinished finished = new RequestFinished();
        finished.setRequestId(_requestId);
        finished.setAccountId(_accountId);
        finished.setStatus(_status);
		finished.setMessage(_message);
        return finished;
    }

        
}
