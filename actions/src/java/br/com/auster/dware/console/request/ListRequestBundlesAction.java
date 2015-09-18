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
 * Created on Sep 24, 2004
 */
package br.com.auster.dware.console.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.request.views.ViewFactory;
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.web.utils.WebUtils;

/**
 * @author Frederico A Ramos
 * @version $Id: ListRequestBundlesAction.java 495 2007-06-12 21:54:53Z framos $
 */
public class ListRequestBundlesAction extends Action {

	// ########################################
	// instance methods
	// ########################################

	public ActionForward execute(ActionMapping _mapping, ActionForm _form,
	    HttpServletRequest _request, HttpServletResponse _response) {

		DynaActionForm form = (DynaActionForm) _form;
		String reqId = (String) form.get("requestId");
		if (reqId == null) {
			reqId = (String) WebUtils.findRequestAttribute(_request,
			    RequestScopeConstants.REQUEST_REQID_KEY);
		}
		if (reqId == null) {
			return _mapping.findForward("error");
		}

		long requestId = Long.parseLong(reqId);

		try {
			WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
			WebRequest request = manager.loadWebRequestDetail(requestId);
			_request.setAttribute(RequestScopeConstants.REQUEST_REQINFO_KEY, ViewFactory.createRequest(request));
			_request.getSession(false).removeAttribute(SessionScopeConstants.SESSION_LISTOFRESULTS_KEY);
		} catch (RequestManagerException rme) {
			throw new PortalRuntimeException(rme);
		} catch (PersistenceResourceAccessException prae) {
			throw new PortalRuntimeException(prae);
		}

		// show notification
		return _mapping.findForward("list-request-bundles");
	}
}
