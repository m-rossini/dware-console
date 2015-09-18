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

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.error.ErrorMessageException;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;
import br.com.auster.security.model.PasswordInfo;
import br.com.auster.security.model.User;

/**
 * @author Frederico A Ramos
 * @version $Id: CreateUserAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class CreateUserAction extends Action {


	private static final I18n i18n = I18n.getInstance(CreateUserAction.class);



    public static final String FORWARD_USER_CREATED_SUCCESSFULLY = "confirm-operation";


    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

        CreateUserForm form = (CreateUserForm) _form;
        String userName = form.getLogin();
        String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), userName} ;

        try {
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);

			// building user
			User user = new User();
            user.setEmail(form.getUserEmail());
            user.setLogin(form.getLogin());
            int idx = form.getUserFullname().indexOf(" ");
            if (idx > 0) {
            	user.setFirstName(form.getUserFullname().substring(0, idx));
            	user.setLastName(form.getUserFullname().substring(idx+1));
            } else {
            	user.setFirstName(form.getUserFullname());
            }
            user.setCustom1(form.getCustom1());
            user.setCustom2(form.getCustom2());
            user.setCustom3(form.getCustom3());
            // building password
            PasswordInfo password = new PasswordInfo();
            password.setPassword(form.getPassword());
            manager.createUser(user, password);

			// update user security group
            manager.grantRole(userName, form.getPermissionGroupName());

            _request.setAttribute(RequestScopeConstants.REQUEST_CONFIRMOPERATION_TEXT, "text.users.userCreatedSuccessfully");
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.create.ok", auditInfo);
            return _mapping.findForward(FORWARD_USER_CREATED_SUCCESSFULLY);
        } catch (SecurityException se) {
        	ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.create.error", auditInfo, se);

            throw new PortalRuntimeException(handleSecurityException(se));
		}
    }


    private Throwable handleSecurityException(SecurityException _se) {
    	if (_se != null) {
    		String msg = _se.getMessage();
    		if (_se.getCause() != null) {
	    		msg = _se.getCause().getMessage();
	    		if (msg == null) {
	    			return _se;
	    		}
	    		if (_se.getCause() instanceof java.sql.SQLException) {
	    			if (msg.indexOf("AUTH_USER_UK1") > 0) {
	    			return new ErrorMessageException(i18n.getString("exception.duplicateEmail"), _se);
	    			} else if (msg.indexOf("AUTH_USER_UK2") > 0) {
	    			return new ErrorMessageException(i18n.getString("exception.duplicateLogin"), _se);
	    			}
	    		}
	    	}
    		return new ErrorMessageException(msg, _se);
    	}
    	return _se;
    }
}
