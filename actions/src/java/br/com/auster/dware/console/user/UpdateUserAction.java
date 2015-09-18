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



import java.util.Collection;
import java.util.Iterator;

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
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;
import br.com.auster.security.model.PasswordInfo;
import br.com.auster.security.model.Role;
import br.com.auster.security.model.User;

/**
 * @author Frederico A Ramos
 * @version $Id: UpdateUserAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class UpdateUserAction extends Action {


	private static final I18n i18n = I18n.getInstance(UpdateUserAction.class);



    public static final String FORWARD_USER_UPDATED_SUCCESSFULLY = "confirm-operation";
    public static final String FORWARD_USER_UPDATE_ERROR = "back-to-list";

    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

        UpdateUserForm form = (UpdateUserForm) _form;
        String  userName = form.getLogin();
    	String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), userName} ;
        try {
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);

			// loading user info to update
			User userInfo = manager.loadUserDetails(userName);
			int idx = form.getUserFullname().indexOf(" ");
			if (idx > 0) {
				userInfo.setFirstName(form.getUserFullname().substring(0, idx));
				userInfo.setLastName(form.getUserFullname().substring(idx+1));
			} else {
				userInfo.setFirstName(form.getUserFullname());
			}
			userInfo.setCustom1(form.getCustom1());
			userInfo.setCustom2(form.getCustom2());
			userInfo.setCustom3(form.getCustom3());
			// update user info.
            manager.alterUser(userInfo);

			// changes user security group
            if (form.getPermissionGroupName() != null) {
	            Collection currentRoles = manager.loadActiveUserRoles(userName);
	            for (Iterator it=currentRoles.iterator(); it.hasNext();) {
	            	manager.revokeRole(userName, ((Role)it.next()).getRoleName());
	            }
	            manager.grantRole(userName, form.getPermissionGroupName());
            }

			// if password was modified, change it
            if ((form.getPassword() != null) && (form.getPassword().trim().length() > 0)) {
            	PasswordInfo password = new PasswordInfo();
            	password.setPassword(form.getPassword());
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
            		throw new PasswordAssignException(se);
            	}
            }
            _request.setAttribute(RequestScopeConstants.REQUEST_CONFIRMOPERATION_TEXT, "text.users.userUpdatedSuccessfully");
            // audit message
            ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.update.ok", auditInfo);
            return _mapping.findForward(FORWARD_USER_UPDATED_SUCCESSFULLY);
        } catch (SecurityException se) {
        	ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.update.error", auditInfo, se);
            throw new PortalRuntimeException(se);
        } catch (PasswordAssignException pae) {
        	ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.update.error", auditInfo, pae);
            throw new PortalRuntimeException(pae);
        } catch (ErrorMessageException eme) {
        	ServiceLocator.getInstance().getAuditService().audit("usermgmt.user.update.error", auditInfo, eme);
            throw new PortalRuntimeException(eme);
		}
    }
}
