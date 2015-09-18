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
 * Created on Sep 6, 2004
 */
package br.com.auster.dware.console.request;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.security.model.User;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;

/**
 * @author Frederico A Ramos
 * @version $Id: CreateNewRequestAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class CreateNewRequestAction extends Action {



    Logger log = Logger.getLogger(SaveAccountsSelectionAction.class);
    I18n i18n = I18n.getInstance(SaveAccountsSelectionAction.class);

    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

        // user info
        HttpSession session = _request.getSession(false);
        User user = (User) session.getAttribute(SessionScopeConstants.SESSION_USERINFO_KEY);
        WebRequest request = (WebRequest) session.getAttribute(SessionScopeConstants.SESSION_NEWREQUEST_KEY);

        if ((request == null) || (user == null)) {
            throw new IllegalStateException("no user or request in current scope");
        }

        String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), String.valueOf(request.getRequestId())} ;
    	try {
            WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);

            request.setOwnerId(user.getUid());
            manager.createWebRequest(request);
            log.debug("created request with id '" + request.getRequestId() + "'");
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("request.create.ok", auditInfo);
        } catch (RequestManagerException rme) {
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("request.create.error", auditInfo, rme);
            // handling exception
            throw new PortalRuntimeException(rme);
        } catch (PersistenceResourceAccessException prae) {
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("request.create.error", auditInfo, prae);
            // handling exception
            throw new PortalRuntimeException(prae);
		}

        String filename = (String) _request.getSession(false).getAttribute(RequestScopeConstants.REQUEST_UPLOADEDFILE_KEY);
        if (filename != null) {
            File f = new File(filename);
            _request.setAttribute(RequestScopeConstants.REQUEST_UPLOADEDFILE_KEY, f.getName());
        }
        _request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, String.valueOf(request.getRequestId()));
        return _mapping.findForward("request-created");
   }
}
