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



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import br.com.auster.security.model.DomainRoleRelation;

/**
 * @author Frederico A Ramos
 * @version $Id: UpdateGroupAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class UpdateGroupAction extends Action {


    public static final String FORWARD_USER_UPDATED_SUCCESSFULLY = "confirm-operation";
    public static final String FORWARD_USER_UPDATE_ERROR = "back-to-list";

    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

    	UpdateGroupForm form = (UpdateGroupForm) _form;
    	String groupName = form.getGroupName();
    	String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), groupName} ;
    	try {

			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);

			Collection activeDomains = manager.loadActiveDomains(groupName);
			List currentDomains = new ArrayList();
			for (Iterator it = activeDomains.iterator(); it.hasNext();) {
				currentDomains.add( ((DomainRoleRelation) it.next()).getDomainName() );
			}

			// here we will grant all permissions not previously granted
			String[] permissionList = form.getPermissionId();
			for (int i=0; i < permissionList.length; i++) {
				if (! currentDomains.remove(permissionList[i])) {
					manager.grantDomain(permissionList[i], groupName);
				}
			}

			// now we must remove those which where revoked
			for (Iterator it = currentDomains.iterator(); it.hasNext();) {
				manager.revokeDomain((String) it.next(), groupName);
			}

			_request.setAttribute(RequestScopeConstants.REQUEST_USER_GROUPOPERATION_KEY, "true");
            _request.setAttribute(RequestScopeConstants.REQUEST_CONFIRMOPERATION_TEXT, "text.groups.groupUpdatedSuccessfully");
            // audit message
            ServiceLocator.getInstance().getAuditService().audit("usermgmt.group.update.ok", auditInfo);
            return _mapping.findForward(FORWARD_USER_UPDATED_SUCCESSFULLY);
        } catch (SecurityException se) {
        	ServiceLocator.getInstance().getAuditService().audit("usermgmt.group.update.error", auditInfo, se);
            throw new PortalRuntimeException(se);
        }
    }
}
