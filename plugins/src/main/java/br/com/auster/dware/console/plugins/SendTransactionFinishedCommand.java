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
 * Created on Apr 8, 2005
 */

/***
 * This class is the responsible to once activated, put a Message on a configured topic.
 * The topic is externally configured @see initEnvironment().
 * 
 * The message has the objective of notify all Topic Subscriber that a given transaction (Or WebRequest) has finished.
 * Althrought it can be configured in such way it can run at any stage of a transaction life-cycle, in doing so
 * the results are unpredictable and for sure will lead to control flow inconsistencies.
 */
package br.com.auster.dware.console.plugins;
/***
 * 
 */
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import br.com.auster.common.log.LogFactory;
import br.com.auster.dware.console.flowcontrol.FlowControlMessage;
import br.com.auster.dware.console.flowcontrol.Stage;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.plugin.FaceliftPlugin;
import br.com.auster.facelift.services.plugin.PluginContext;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;

public class SendTransactionFinishedCommand implements FaceliftPlugin {

	public static final String	CONFIGURATION_JMS_CONNFACTORY	= "jms.factory";
	public static final String	CONFIGURATION_JMS_TOPICNAME	  = "jms.name";

	private Logger	           log	                          = LogFactory
	                                                              .getLogger(SendTransactionFinishedCommand.class);

	private PluginContext	     context;
	private Map	               parameters;

	private String	           factory;
	private String	           name;
	private WebRequest	       request;

	public PluginContext getContext() {
		return context;
	}

	public void setContext(PluginContext _context) {
		context = _context;
	}

	public void setConfigurationParameters(Map _config) {
		parameters = _config;
	}

	public Map getConfigurationParameters() {
		return parameters;
	}

	public void cleanup() {
		context = null;
		parameters = null;
		this.factory = null;
		this.name = null;
	}

	public void execute(PluginContext _context) {
		setContext(_context);
		execute();
	}

	public void execute() {
		initEnvironment();
		request = getWebRequest();
		if (request == null) {
			log.info("No request found to command report generation.");
			return;
		}
		
		try {
			sendMessageToTopic(request.getRequestId());
		} catch (Exception e) {
			log.error("Error running transaction finished command plugin", e);
			throw new PluginRuntimeException(e.getMessage(), e);
		}
	}

	protected void initEnvironment() {
		this.factory = (String) this.parameters.get(CONFIGURATION_JMS_CONNFACTORY);
		this.name = (String) this.parameters.get(CONFIGURATION_JMS_TOPICNAME);

		if (this.factory == null) {
			throw new PluginRuntimeException("JMS connection factory must be set.");
		}
		if (this.name == null) {
			throw new PluginRuntimeException("JMS topic name must be set.I do not know the name of the Transaction Finished Topic");
		}
	}

	protected WebRequest getWebRequest() {
		if ((context.getExecutionParameters() == null)
		    || (context.getExecutionParameters().size() <= 0)) {
			return null;
		}
		return (WebRequest) context.getExecutionParameters().get(0);
	}

	protected void sendMessageToTopic(long _requestId) throws Exception {
		TopicConnection tcon = null;
		TopicSession tsession = null;
		TopicPublisher tpublisher = null;
		try {
			Context ctx = new InitialContext(new Hashtable(this.parameters));
			// getting connection facotry
			TopicConnectionFactory tconFactory = (TopicConnectionFactory) ctx.lookup(this.factory);
			tcon = tconFactory.createTopicConnection();
			// connecting to the JMS provider
			tsession = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = (Topic) ctx.lookup(this.name);
			tpublisher = tsession.createPublisher(topic);
			
			ObjectMessage msg = tsession.createObjectMessage();
			
			tcon.start();
			// builing message			
			FlowControlMessage message = new FlowControlMessage(String.valueOf(_requestId));
			Stage stage = new Stage();
			stage.setException(null);
			stage.setTextMessage("Finished according to Billcheckout Portal Notification.");
			stage.setReturnCode(Stage.PROC_OK);
			stage.setAcknowledgeDate(Calendar.getInstance());
			stage.setClassName(this.getClass().getName());
			message.addStage(stage);
			
			msg.setObject(message);
			// sending message
			tpublisher.publish(msg);
		} finally {
			if (tpublisher != null) {
				tpublisher.close();
			}
			if (tsession != null) {
				tsession.close();
			}
			if (tcon != null) {
				tcon.close();
			}
		}
	}
}
