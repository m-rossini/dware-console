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
 * Created on Oct 7, 2004
 */
package br.com.auster.dware.console.listeners;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import br.com.auster.common.log.LogFactory;
import br.com.auster.dware.graph.Request;
import br.com.auster.dware.request.RequestErrorListener;




/**
 * @author Ricardo Barone
 * @version $Id: JMSErrorListener.java 146 2005-11-04 13:07:02Z framos $
 */
public class JMSErrorListener implements RequestErrorListener {


	
	private Logger log = LogFactory.getLogger(JMSErrorListener.class);

	private Element configuration;

	
	public JMSErrorListener(Element _configuration) {
		if (_configuration == null) {
			throw new IllegalStateException("cannot connect to JMS queue without queue configuration parameters");
		}
		this.configuration = _configuration;
	}
	

	
	public void errorOccured(Request _request, Throwable _error) {
		JMSFinishListener listener = new JMSFinishListener(configuration);
		log.error("detected error while handing request creation.", _error);
		listener.graphFinished(null, _request, _error, Calendar.getInstance().getTime());
	}

}
