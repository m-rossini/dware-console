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
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.dware.console.commons.MultiFieldComparator;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.user.views.ViewFactory;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.persistence.FetchCriteria;
import br.com.auster.security.base.SecurityException;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;

/**
 * @author Frederico A Ramos
 * @version $Id: ListUsersAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class ListUsersAction extends Action {


    private FetchCriteria fetch = new FetchCriteria();

    public static final String FORWARD_DISPLAYLIST = "show-users";




    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form,
			 HttpServletRequest _request, HttpServletResponse _response) {

        Collection resultList;
        try {
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);

            // defining paging information
            String pageId = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_PAGEID_KEY);
            if (pageId == null) {
                pageId = "1";
            }
            String moveTo = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_MOVETO_KEY);
            if (moveTo == null) {
                moveTo = "0";
            }
            // order field and orientation
            String orderBy = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERBY_KEY);
            if ((orderBy == null) || (orderBy.trim().length() <= 0)) {
                orderBy = "user_email";
            }
            String orderWay = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERWAY_KEY);
            if (orderWay == null) {
                orderWay = RequestScopeConstants.REQUEST_ORDERFORWARD_KEY;
            }

            int pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pageId), Integer.parseInt(moveTo));
            int offset = IndexingUtils.getStartingElement(pageNbr, 20);
            fetch.clearOrder();
            fetch.setOffset(offset);
            fetch.setSize(20);
            fetch.addOrder(orderBy, orderWay.equals(RequestScopeConstants.REQUEST_ORDERFORWARD_KEY));

            resultList = manager.loadUsers(fetch);
            int totalPages = manager.countUsers();

            List users = ViewFactory.createUserList(resultList);
            String orderByProperties = (orderBy.equals("user_email") ? "email" : "userName");
            MultiFieldComparator mfc = new MultiFieldComparator(new String[] { orderByProperties } );
            if (orderWay.equals(RequestScopeConstants.REQUEST_ORDERFORWARD_KEY)) {
            	mfc.setAscendingOrder();
            } else {
            	mfc.setDescendingOrder();
            }
            Collections.sort(users, mfc);
            _request.setAttribute(RequestScopeConstants.REQUEST_LISTOFUSERS_KEY, users);

            totalPages = IndexingUtils.getNumberOfPages(totalPages, 20);
            _request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
            _request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
            _request.setAttribute(RequestScopeConstants.REQUEST_ORDERBY_KEY, (orderBy == null ? "user_email" : orderBy) );
            _request.setAttribute(RequestScopeConstants.REQUEST_ORDERWAY_KEY, orderWay);

            return _mapping.findForward(FORWARD_DISPLAYLIST);
        } catch (SecurityException se) {
            throw new PortalRuntimeException(se);
		}
    }
}
