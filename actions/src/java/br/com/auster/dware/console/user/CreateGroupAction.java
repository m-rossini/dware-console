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

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;
import br.com.auster.security.model.Role;


/**
 * @author Frederico A Ramos
 * @version $Id: CreateGroupAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class CreateGroupAction extends Action {



    public static final String FORWARD_USER_CREATED_SUCCESSFULLY = "confirm-operation";


    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

		CreateGroupForm form = (CreateGroupForm) _form;
		String  groupName = form.getGroupName();
    	String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), groupName} ;
    	try {

			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);

			Role role = new Role(form.getGroupName());
			role.setActive(true);
			manager.createRole(role);
			for (int i=0; i < form.getPermissionId().length; i++) {
				manager.grantDomain(form.getPermissionId()[i], form.getGroupName());
			}
			_request.setAttribute(RequestScopeConstants.REQUEST_CONFIRMOPERATION_TEXT, "text.groups.groupUpdatedSuccessfully");
			_request.setAttribute(RequestScopeConstants.REQUEST_USER_GROUPOPERATION_KEY, "true");
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("usermgmt.group.create.ok", auditInfo);
            return _mapping.findForward(FORWARD_USER_CREATED_SUCCESSFULLY);

        } catch (SecurityException se) {
            ServiceLocator.getInstance().getAuditService().audit("usermgmt.group.create.error", auditInfo, se);
            throw new PortalRuntimeException(se);
		}
    }
}
