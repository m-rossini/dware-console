/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 25/09/2008
 */
package br.com.auster.dware.console.user;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.ApplicationScopeConstants;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.error.ErrorMessageException;
import br.com.auster.dware.console.login.LoginAction;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;
import br.com.auster.security.model.PasswordInfo;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ChangePasswordAction extends Action {


	private static final I18n i18n = I18n.getInstance(LoginAction.class);

    public static final String FORWARD_CHANGE_PASSWORD_SUCCESS = "success";
    public static final String FORWARD_CHANGE_PASSWORD_ERROR = "error";

    //########################################
    // instance methods
    //########################################

    
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

        UpdateUserForm form = (UpdateUserForm) _form;
        String  userName = form.getLogin();
    	String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), userName} ;
    	
        try {
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);

			if(manager.validateCurrentPassword(userName, form.getPassword())) {
			
		
	        	PasswordInfo password = new PasswordInfo();
	        	password.setPassword(form.getNewPassword());
	        	int maxHistory = -1;
	        	if (getServlet().getServletContext().getAttribute(ApplicationScopeConstants.APPLICATION_MAX_PASSWORD_HISTORY) != null) {
	        		try {
	        			maxHistory = Integer.parseInt( (String) getServlet().getServletContext().getAttribute(ApplicationScopeConstants.APPLICATION_MAX_PASSWORD_HISTORY));
	        		} catch (Exception e) { // TO NOTHING
	        		}
	        	}
	        	try {
	        		if (!manager.assignPassword(userName, password, SessionHelper.getUsername(_request), maxHistory)) {
	        			throw new ErrorMessageException(i18n.getString("exception.passwdInHist"), new SecurityException());
	        		}
	        	} catch (SecurityException se) {
	        		throw se;
	        	}
			} else {
				// user and password do not match
				throw new SecurityException(i18n.getString("userNotAuthenticated"));
			}

            _request.setAttribute(RequestScopeConstants.REQUEST_CONFIRMOPERATION_TEXT, "text.users.userUpdatedSuccessfully");
            // audit message
            ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.update.ok", auditInfo);
            return _mapping.findForward(FORWARD_CHANGE_PASSWORD_SUCCESS);
        } catch (Exception e) {
			ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.update.error", auditInfo, e);
        	String message = new String(e.getMessage().substring(e.getMessage().indexOf(':')+1));
			_request.setAttribute(RequestScopeConstants.REQUEST_ERROROPERATION_TEXT, message);
			return _mapping.findForward(FORWARD_CHANGE_PASSWORD_ERROR);
		}
    }
    
}
