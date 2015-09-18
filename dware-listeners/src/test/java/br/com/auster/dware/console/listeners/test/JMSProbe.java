/*
 * Copyright (c) 2004-2006 Auster Solutions. All Rights Reserved.
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
 * Created on 30/08/2006
 */
package br.com.auster.dware.console.listeners.test;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.w3c.dom.Element;

import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.console.listener.RequestFinished;
import br.com.auster.dware.console.listeners.JMSFinishListener;

/**
 * @author framos
 * @version $Id$
 *
 */
public class JMSProbe {

	
	public void probe(Element _configuration, String _requestId, String _accountId, String _status) throws NamingException, JMSException {
		// building object to send to JMS
		RequestFinished probeObj = new RequestFinished();
		probeObj.setRequestId(Long.parseLong(_requestId));
		probeObj.setAccountId(_accountId);
		probeObj.setStatus(Integer.parseInt(_status));
		probeObj.setMessage("Account status probed.");
		// sending
		JMSFinishListener listener = new JMSFinishListener(_configuration);
		listener.send(probeObj);
	}
	
	
	/**
	 * Must only run when arguments are as follow:
	 * 
	 * arg[0] - points to an unencrypted XML document with the cofiguration for the JMS Finish listener
	 * arg[1] - number of the WEB portal request to be probed
	 * arg[2] - number of the account, in the request, to be updated
	 * arg[3] - the new status the selected account will be updated to. Allowed values
	 *    are 3 (for OK) and 4 (for ERROR). If another value is set, then defaults to 3.
	 */
	public static void main(String[] _args) {
		
		if (_args.length < 4) {
			System.out.println("Usage: ProbeAccountFinished <configuration-file> <request-id> <account-number> <status>");
			System.out.println("\nwhere:");
			System.out.println("  configuration-file\tpoints to an XML document used to configure a JMS listener");
			System.out.println("  request-id\t\tis the ID number of the Web portal request");
			System.out.println("  account-number\tis the number to be finished, in the specified request");
			System.out.println("  status\t\tone of the following (3 = finished OK, 4 = ERROR); defaults to 3");
			System.exit(1);
		}
		
		try {
		
			Element configuration = DOMUtils.openDocument(_args[0], false);
			String status = _args[3];
			if (!("3".equals(status) || "4".equals(status))) {
				status = "3";
			}
			
			JMSProbe probe = new JMSProbe();
			probe.probe(configuration, _args[1].trim(), _args[2].trim(), status);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

}
