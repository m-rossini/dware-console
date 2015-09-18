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
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;
import br.com.auster.security.model.DomainRoleRelation;
import br.com.auster.security.model.Role;
import br.com.auster.web.utils.WebUtils;

/**
 * @author Frederico A Ramos
 * @version $Id: LoadPermissionGroupsAction.java 111 2005-08-04 19:54:09Z framos $
 */
public class LoadGroupDetailsAction extends Action {


    public static final String FORWARD_DISPLAY_PAGE = "display-page";
    public static final String FORWARD_LIST_GROUPS = "list-groups";


    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form,
			 HttpServletRequest _request, HttpServletResponse _response) {

        try {

			String groupName = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_USER_CURRGROUP_KEY);
			if (groupName == null) {
				// TODO log
				return _mapping.findForward(FORWARD_LIST_GROUPS);
			}

			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
			Role groupInfo = manager.loadRole(groupName);
			UpdateGroupForm form = new UpdateGroupForm();
			form.setGroupName(groupName);
			form.setActive(groupInfo.isActive());
			// setting assigned domains to role
			Collection activeDomains = manager.loadActiveDomains(groupName);
			List temp = new ArrayList();
			for (Iterator it = activeDomains.iterator(); it.hasNext();) {
				temp.add( ((DomainRoleRelation) it.next()).getDomainName() );
			}
			form.setPermissionId((String[])temp.toArray(new String[] {}));

			_request.setAttribute("updateGroupForm", form);
            return _mapping.findForward(FORWARD_DISPLAY_PAGE);

        } catch (SecurityException se) {
            throw new PortalRuntimeException(se);
		}
    }
}
