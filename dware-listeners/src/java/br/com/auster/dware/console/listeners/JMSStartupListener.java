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

import java.util.Calendar;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import br.com.auster.common.log.LogFactory;
import br.com.auster.common.util.I18n;
import br.com.auster.dware.DataAware;
import br.com.auster.dware.StartupListener;
import br.com.auster.dware.graph.Request;
import br.com.auster.facelift.requests.web.interfaces.ResumeCriteria;


/**
 * 
 * @author framos
 * @version $Id: JMSStartupListener.java 146 2005-11-04 13:07:02Z framos $
 */
public class JMSStartupListener extends JMSFinishListener implements StartupListener {

	
	
	private Logger log = LogFactory.getLogger(JMSStartupListener.class);
	private I18n  i18n = I18n.getInstance(JMSStartupListener.class);
	
	
	
	public JMSStartupListener(Element _configuration) {
		super(_configuration);
	}
	
	
	public void beforeConfig(DataAware _dware, Element _config) {
		// do nothing
	}

	/**
	 * Sends a resume notification for all requests with start date >= TODAY - 1 MONTH
	 */
	public void afterConfig(DataAware _dware, Element _config) {
		// execute resume action
		log.info("preparing message to resume all unfinished requests");
		try {
			ResumeCriteria criteria = new ResumeCriteria();
			Calendar calendar = Calendar.getInstance();
			calendar.roll(Calendar.MONTH, -1);
			criteria.setCreationDate(calendar.getTime());
			this.send(criteria);
		} catch (NamingException ne) {
			log.warn("could not resume unfinished requests", ne);
		} catch (JMSException jmse) {
			log.warn("could not resume unfinished requests", jmse);
		}
	}	

	public boolean beforeEnqueue(DataAware _dware, Request _request) {
		// always return TRUE 
		return true;
	}

	public void afterEnqueue(DataAware _dware, Request _request) {
		
//		RequestFinished result = new RequestFinished(); 
//		result.setRequestId(Long.parseLong(_request.getTransactionId()));
//		result.setAccountId((String)_request.getAttributes().get("account"));
//		log.debug("preparing message to request " + result.getRequestId() + ", account " + result.getAccountId());
//
//		result.setStatus(RequestLifeCycle.REQUEST_LIFECYCLE_INPROCESS);
//		result.setMessage(i18n.getString("request.enqueued"));
//		
//		try {
//			this.send(result);
//		} catch (NamingException ne) {
//			log.warn("could not notify account enqueue action", ne);
//		} catch (JMSException jmse) {
//			log.warn("could not notify account enqueue action", jmse);
//		}
	}

}
