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

import java.io.Serializable;

import br.com.auster.facelift.requests.web.model.WebRequest;

/**
 * @author framos
 * @version $Id: RequestCreation.java 251 2006-09-06 23:37:50Z framos $
 */
public class RequestCreation implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2036949865792634840L;
	private String email;
	private WebRequest request;
	
	
	public RequestCreation(String _userEmail, WebRequest _request) {
		email = _userEmail;
		request = _request;
	}
	
	public String getUserEmail() {
		return email;
	}
	
	public WebRequest getRequestToCreate() {
		return request;
	}
	
}
