/*
 *
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 16/03/2007
 *
 * @(#)SendMessageQueue.java 16/03/2007
 */
package br.com.auster.dware.console.request.queue;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import br.com.auster.common.log.LogFactory;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;
import br.com.auster.minerva.spi.ReportRequest;

/**
 * The class <code>SendMessageQueue</code> it is responsible in
 * <p>
 * TODO To improve the commentaries.
 * </p>
 * 
 * @author Gilberto Brandão
 * @version $Id$
 * @since JDK1.4
 */
public class SendMessageQueue {

	private Logger							log														= LogFactory
																																.getLogger(SendMessageQueue.class);

	/**
	 * Used to store the values of <code>CONFIGURATION_JMS_CONNFACTORY</code>.
	 */
	public static final String	CONFIGURATION_JMS_CONNFACTORY	= "jms.factory";

	/**
	 * Used to store the values of <code>CONFIGURATION_JMS_TOPICNAME</code>.
	 */
	public static final String	CONFIGURATION_JMS_TOPICNAME		= "jms.name";

	/**
	 * Used to store the values of <code>CONFIGURATION_REPORT_NAME</code>.
	 */
	public static final String	CONFIGURATION_REPORT_NAME			= "report.name";

	/**
	 * Used to store the values of <code>parameters</code>.
	 */
	private Map									parameters										= new Hashtable();

	/**
	 * Used to store the values of <code>factory</code>.
	 */
	private String							factory;

	/**
	 * Used to store the values of <code>name</code>.
	 */
	private String							name;

	public void setConfigurationParameters() {

		Map config = new HashMap();

		config.put(CONFIGURATION_JMS_CONNFACTORY,
				"billcheckout.QueueConnectionFactory");
		config.put(CONFIGURATION_JMS_TOPICNAME, "topic/reportTopic");
		config.put(CONFIGURATION_REPORT_NAME, "billcheckout-portal");
		parameters = config;
	}

	/**
	 * 
	 * Is responsible in
	 * 
	 */
	protected void initEnvironment() {
		this.setConfigurationParameters();
		this.factory = (String) this.parameters.get(CONFIGURATION_JMS_CONNFACTORY);
		this.name = (String) this.parameters.get(CONFIGURATION_JMS_TOPICNAME);

		if (this.factory == null) {
			throw new PluginRuntimeException("JMS connection factory must be set.");
		}
		if (this.name == null) {
			throw new PluginRuntimeException("JMS topic name must be set.");
		}
	}

	/**
	 * 
	 * Is responsible in
	 * 
	 * @param _requestId
	 * @param _reportName
	 * @throws Exception
	 */
	public void sendMessageToTopic(long _requestId) throws Exception {

		// inicia as configurações
		this.initEnvironment();

		TopicConnection tcon = null;
		TopicSession tsession = null;
		TopicPublisher tpublisher = null;
		try {
			Context ctx = new InitialContext(new Hashtable(this.parameters));
			// getting connection facotry
			TopicConnectionFactory tconFactory = (TopicConnectionFactory) ctx
					.lookup(this.factory);
			tcon = tconFactory.createTopicConnection();
			// connecting to the JMS provider
			tsession = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = (Topic) ctx.lookup(this.name);
			tpublisher = tsession.createPublisher(topic);
			ObjectMessage msg = tsession.createObjectMessage();
			tcon.start();
			// builing message
			 BillcheckoutReportRequest reportRequest = new
			 BillcheckoutReportRequest( (String) parameters.get(CONFIGURATION_REPORT_NAME) );
			 reportRequest.setTransactionId(String.valueOf(_requestId));
			 //reportRequest.getAttributes().put(ReportRequest.ATTR_BYPASS_CLEANUP_PREVIOUS, new Boolean(true));
			 reportRequest.getAttributes().put(ReportRequest.ATTR_BYPASS_DUP_CHECK, new Boolean(true));			 
			 msg.setObject(reportRequest);
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
