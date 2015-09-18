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
 * Created on Oct 03, 2004
 */
package br.com.auster.dware.console.user;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;

/**
 * @author Frederico A Ramos
 * @version $Id: UnlockUserLoginAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class UnlockUserLoginAction extends Action {


    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

        DynaActionForm form = (DynaActionForm) _form;
    	String userName = (String) form.get("login");;

    	String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), userName} ;
        try {
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
			manager.unlockUser(userName, SessionHelper.getUsername(_request));
            _request.setAttribute(RequestScopeConstants.REQUEST_CONFIRMOPERATION_TEXT, "text.users.userUpdatedSuccessfully");
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.unlock.ok", auditInfo);
            return _mapping.findForward("confirm-operation");
        } catch (SecurityException se) {
        	ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.unlock.error", auditInfo, se);
            throw new PortalRuntimeException(se);
		}
    }
}
