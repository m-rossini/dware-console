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

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.user.views.PermissionGroupView;
import br.com.auster.dware.console.user.views.ViewFactory;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;
import br.com.auster.web.utils.WebUtils;


/**
 * @author Frederico A Ramos
 * @version $Id: LoadPermissionGroupsAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class LoadPermissionGroupsAction extends Action {


    public static final String FORWARD_DISPLAY_PAGE = "display-page";


    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form,
			 HttpServletRequest _request, HttpServletResponse _response) {

        try {
        	boolean hideInactive = (WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_USER_CURRGROUP_KEY) != null);
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
			Collection roles = ViewFactory.createGroups(manager.loadRoles(), hideInactive);
			for (Iterator it = roles.iterator(); it.hasNext(); ) {
				PermissionGroupView groupView = (PermissionGroupView) it.next();
				Collection activeDomains = manager.loadActiveDomains(groupView.getGroupName());
				ViewFactory.setPermissions(groupView, activeDomains);
			}
			_request.setAttribute(RequestScopeConstants.REQUEST_USER_GROUPLIST_KEY, roles);

            return _mapping.findForward(FORWARD_DISPLAY_PAGE);

        } catch (SecurityException ume) {
            throw new PortalRuntimeException(ume);
		}
    }
}
