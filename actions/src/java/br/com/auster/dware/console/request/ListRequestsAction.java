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
 * Created on Sep 5, 2004
 */
package br.com.auster.dware.console.request;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import br.com.auster.dware.console.commons.PermissionConstants;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.request.views.ViewFactory;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.web.interfaces.WebRequestCriteria;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.persistence.FetchCriteria;
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.security.base.SecurityException;
import br.com.auster.security.model.User;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;

/**
 * @author Frederico A Ramos
 * @version $Id: ListRequestsAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class ListRequestsAction extends Action {

	private Logger log = Logger.getLogger(ListRequestsAction.class);
	private FetchCriteria	fetch	= new FetchCriteria();
	public static final String FORWARD_LOGIN_ERROR	= "error";


	// ########################################
	// instance methods
	// ########################################

	public ActionForward execute(ActionMapping _mapping, ActionForm _form,
	    HttpServletRequest _request, HttpServletResponse _response) {

		fetch.clearOrder();
		ActionMessages messages = new ActionMessages();
		ExceptionUtils exceptionUtils = new ExceptionUtils();

		try {
			WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(
			    ServiceConstants.REQUESTMANAGER_SERVICE);

			HttpSession session = _request.getSession();
			User userInfo = (User) session.getAttribute(SessionScopeConstants.SESSION_USERINFO_KEY);

			// defining paging information
			String pageId = (String) WebUtils.findRequestAttribute(_request,
			    RequestScopeConstants.REQUEST_PAGEID_KEY);
			if (pageId == null) {
				pageId = "1";
			}
			String moveTo = (String) WebUtils.findRequestAttribute(_request,
			    RequestScopeConstants.REQUEST_MOVETO_KEY);
			if (moveTo == null) {
				moveTo = "0";
			}
			// order field and orientation
			String orderBy = (String) WebUtils.findRequestAttribute(_request,
			    RequestScopeConstants.REQUEST_ORDERBY_KEY);
			if ((orderBy == null) || (orderBy.trim().length() <= 0)) {
				orderBy = "request_id";
			}
			String orderWay = (String) WebUtils.findRequestAttribute(_request,
			    RequestScopeConstants.REQUEST_ORDERWAY_KEY);
			if (orderWay == null) {
				orderWay = RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY;
			}

			int pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pageId), Integer.parseInt(moveTo));
			int offset = IndexingUtils.getStartingElement(pageNbr, 20);
			fetch.setOffset(offset);
			fetch.setSize(20);
			fetch.addOrder(orderBy, orderWay.equals(RequestScopeConstants.REQUEST_ORDERFORWARD_KEY));

			UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
			WebRequestCriteria criterias = new WebRequestCriteria();

			// check for GROUPVIEW permission
			if (!userInfo.getAllowedDomains().contains(PermissionConstants.PERMISSION_REQUEST_GROUPVIEW_KEY)) {
				// if user does not have GROUPVIEW permission, show only his own
				criterias.setOwnerId(userInfo.getUid());
			}
            // removing logical deleted requests from view
            criterias.setStatusExclude(199);

			List queryList = manager.findWebRequests(criterias, fetch);
			List resultList = ViewFactory.createRequestList(queryList, userManager.loadUsers());

			int totalPages = IndexingUtils.getNumberOfPages(manager.countWebRequests(criterias), 20);
			_request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
			_request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String
			    .valueOf(totalPages));
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERBY_KEY,
			    (orderBy == null ? "request_id" : orderBy));
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERWAY_KEY, orderWay);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, resultList);

			return _mapping.findForward("list-requests");

		} catch (SecurityException se) {
			throw new PortalRuntimeException(se);
		} catch (RequestManagerException rme) {
			throw new PortalRuntimeException(rme);
		} catch (PersistenceResourceAccessException prae) {
		throw new PortalRuntimeException(prae);
	    }
	}
}
