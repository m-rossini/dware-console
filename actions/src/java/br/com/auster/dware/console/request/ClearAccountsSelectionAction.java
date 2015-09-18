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
 * Created on Sep 6, 2004
 */
package br.com.auster.dware.console.request;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.selectbox.utils.SelectBoxUtils;

/**
 * @author Frederico A Ramos
 * @version $Id: ClearAccountsSelectionAction.java 30 2005-05-24 18:46:06Z framos $
 */
public class ClearAccountsSelectionAction extends Action {

	
	Logger log = Logger.getLogger(ClearAccountsSelectionAction.class);
	I18n i18n = I18n.getInstance(ClearAccountsSelectionAction.class);
	

    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {
        
        String pageId = "1";
        String moveTo = "0";
        String filterBy = "";
        String filterCondition = null;       
        
        // clears the already selected list of accounts 
        Set selected = (Set)_request.getSession(false).getAttribute(SessionScopeConstants.SESSION_CACHEDACCOUNTS_KEY);
        if (selected != null) {
            selected.clear();
            _request.getSession(false).setAttribute(SessionScopeConstants.SESSION_CACHEDACCOUNTS_KEY, selected);
        }
        
        Map resultList = (Map) _request.getSession(false).getAttribute(SessionScopeConstants.SESSION_LISTOFRESULTS_KEY);
        Map finalResult = null;
        if (resultList != null) {
            finalResult = SelectBoxUtils.filterMap(resultList, filterCondition, filterBy);
            int totalPages = IndexingUtils.getNumberOfPages(finalResult.size(), 20);
            int pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pageId), Integer.parseInt(moveTo));
            int offset = IndexingUtils.getStartingElement(pageNbr, 20);
            _request.setAttribute(RequestScopeConstants.REQUEST_FILTERBY_KEY, filterBy);
            _request.setAttribute(RequestScopeConstants.REQUEST_FILTERCONDITION_KEY, filterCondition);
            _request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
            _request.setAttribute(RequestScopeConstants.REQUEST_OFFSET_KEY, String.valueOf(offset));
            _request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
            _request.setAttribute(RequestScopeConstants.REQUEST_DISPLAYLEN_KEY, String.valueOf(20));
        }
        if (finalResult != null) {
            _request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, finalResult.entrySet());
        }
        
    	return _mapping.findForward("show-accounts");
    }    
   
}
