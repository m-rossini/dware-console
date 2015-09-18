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
 * Created on Apr 26, 2005
 */
package br.com.auster.invoice.console.tests;

import java.util.HashMap;
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

import br.com.auster.dware.console.plugins.report.BillcheckoutReportRequest;

public class ReportClient {



    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println(("Usage : ReportClient <requestId>"));
            return;
        }
        
        try {
            Map paramList = new HashMap();
            paramList.put("java.naming.factory.initial","weblogic.jndi.WLInitialContextFactory");
            paramList.put("java.naming.provider.url","t3://localhost:7001");
            
            Context ctx = new InitialContext(new Hashtable(paramList));
        	// getting connection facotry
		    TopicConnectionFactory tconFactory = (TopicConnectionFactory) ctx.lookup("/billcheckout/QueueConnectionFactory");
		    TopicConnection tcon = tconFactory.createTopicConnection();
		    // connecting to the JMS provider
		    TopicSession tsession = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		    Topic topic = (Topic) ctx.lookup("topic/reportTopic");
		    TopicPublisher tpublisher = tsession.createPublisher(topic);
		    ObjectMessage msg = tsession.createObjectMessage();
		    tcon.start();
		    // builing message
		    BillcheckoutReportRequest reportRequest = new BillcheckoutReportRequest("billcheckout-portal");
		    reportRequest.setTransactionId(args[0]);
		    msg.setObject(reportRequest);
		    // sending message
		    tpublisher.publish(msg);
            
		    tpublisher.close();
		    tcon.close();
		    tsession.close();
		    
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception rme) {
            rme.printStackTrace();
        }
        
    }

}
