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
 * Created on Sep 24, 2004
 */
package br.com.auster.dware.console.request;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.request.views.RequestView;
import br.com.auster.dware.console.request.views.ViewFactory;
import br.com.auster.persistence.FetchCriteria;
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.facelift.requests.interfaces.RequestCriteria;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;

/**
 * @author Frederico A Ramos
 * @version $Id: ListRequestAccountsAction.java 324 2007-02-22 13:44:34Z gbrandao $
 */
public class ListRequestAccountsAction extends Action {

    
    private FetchCriteria fetch = new FetchCriteria();

    
	
    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {

        fetch.clearOrder();
        DynaActionForm form = (DynaActionForm) _form;
        
        String reqId = (String) form.get("requestId");
        if ((reqId == null) || (reqId.trim().length() <= 0)) {
        	reqId = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_REQID_KEY);
        }
        if (reqId == null) {
            return _mapping.findForward("error");
        }

        long requestId = Long.parseLong(reqId);

        String pageId = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_PAGEID_KEY);
        if (pageId == null) {
            pageId = "1";
        }            
        String moveTo = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_MOVETO_KEY);
        if (moveTo == null) {
            moveTo = "0";
        }

        String filterBy = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_FILTERBY_KEY);
        if (filterBy == null) {
            filterBy = "";
        }
        String filterCondition = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_FILTERCONDITION_KEY);

        // order field and orientation
        String orderBy = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERBY_KEY);
        if ((orderBy == null) || (orderBy.trim().length() <= 0)) {
            orderBy = "proc_request.request_label";
        }
        String orderWay = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERWAY_KEY);
        if (orderWay == null) {
            orderWay = RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY;
        }
        
        int pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pageId), Integer.parseInt(moveTo));
        int offset = IndexingUtils.getStartingElement(pageNbr, 20);
        fetch.setOffset(offset);
        fetch.setSize(20);
        fetch.addOrder(orderBy, orderWay.equals(RequestScopeConstants.REQUEST_ORDERFORWARD_KEY));
        
        try {
            WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
            WebRequest webRequest = manager.loadWebRequestDetail(requestId);
            RequestView req = ViewFactory.createRequest(webRequest);
            
            List resultList = null;
            RequestCriteria criteria = null;
            if (filterCondition != null) {
                criteria = new RequestCriteria();
                criteria.setLabel("%"+ filterCondition + "%");
            }
			List accounts = manager.findWebRequestProcesses(requestId, criteria, fetch);
            resultList = ViewFactory.createRequestAccountList(accounts);
            _request.getSession(false).setAttribute(SessionScopeConstants.SESSION_LISTOFRESULTS_KEY, accounts);
            
            int totalPages = IndexingUtils.getNumberOfPages(manager.countWebRequestProcesses(requestId, criteria), 20);
            _request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
            _request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
            _request.setAttribute(RequestScopeConstants.REQUEST_FILTERCONDITION_KEY, filterCondition);
            _request.setAttribute(RequestScopeConstants.REQUEST_FILTERBY_KEY, filterBy);
            _request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, resultList);
            _request.setAttribute(RequestScopeConstants.REQUEST_ORDERBY_KEY, (orderBy == null ? "proc_request.request_label" : orderBy) );
            _request.setAttribute(RequestScopeConstants.REQUEST_ORDERWAY_KEY, orderWay);
            _request.setAttribute(RequestScopeConstants.REQUEST_REQINFO_KEY, req);
            
        } catch (RequestManagerException rme) {
            throw new PortalRuntimeException(rme);
        } catch (PersistenceResourceAccessException prae) {
            throw new PortalRuntimeException(prae);
		}
        
        // show notification
        //_request.setAttribute(Constants.LISTOFNOTIFICARequestScopeConstants, manager.findRequestNotifications(requestId));
        return _mapping.findForward("list-request-accounts");
    }
}
