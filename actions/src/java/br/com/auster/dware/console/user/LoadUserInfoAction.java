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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.user.views.ViewFactory;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;
import br.com.auster.security.model.Role;
import br.com.auster.security.model.User;
import br.com.auster.security.model.UserRoleRelation;
import br.com.auster.web.utils.WebUtils;

/**
 * @author Frederico A Ramos
 * @version $Id: LoadUserInfoAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class LoadUserInfoAction extends Action {


    public static final String FORWARD_DISPLAY_USERDETAIL = "display-user-info";
    public static final String FORWARD_LOADING_ERROR = "back-to-list";


    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form,
			 HttpServletRequest _request, HttpServletResponse _response) {

        String userEmail = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_USEREMAIL_KEY);

        try {
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);

            User userInfo = manager.loadUserDetails(userEmail);
            if (userInfo != null) {
                _request.setAttribute(RequestScopeConstants.REQUEST_USERINFO_KEY, ViewFactory.createUser(userInfo));
				_request.setAttribute(RequestScopeConstants.REQUEST_USER_GROUPLIST_KEY, ViewFactory.createGroups(manager.loadRoles(), true) );

				// should have only one
				Collection rolesForUser = manager.loadActiveUserRoles(userEmail);
				if ((rolesForUser != null) && (rolesForUser.size() > 0)) {
					Role currGroup = (Role) rolesForUser.iterator().next();

					_request.setAttribute(RequestScopeConstants.REQUEST_USER_CURRGROUP_KEY, currGroup.getRoleName());
				}

                return _mapping.findForward(FORWARD_DISPLAY_USERDETAIL);
            }
        } catch (SecurityException se) {
            throw new PortalRuntimeException(se);
		}
        return _mapping.findForward(FORWARD_LOADING_ERROR);
    }
}
