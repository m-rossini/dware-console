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
package br.com.auster.dware.console.queries;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;



/**
 * @author Frederico A Ramos
 * @version $Id: DisplayResultsAction.java 150 2005-11-10 18:45:41Z framos $
 */
public class DisplayResultsAction extends Action {

    
    
	protected static final Logger log = Logger.getLogger(DisplayResultsAction.class);
	
	
	
    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form,  HttpServletRequest _request, HttpServletResponse _response) {

        Object results = _request.getSession(false).getAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY);
		_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, results);

		Object query = _request.getSession(false).getAttribute(RequestScopeConstants.REQUEST_QUERYINFO_KEY);
        _request.setAttribute(RequestScopeConstants.REQUEST_QUERYINFO_KEY, query);
    
		String pgId = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_PAGEID_KEY);
		if (pgId == null) {
			pgId = "1";
		}
		
        String moveTo = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_MOVETO_KEY);
        if (moveTo == null) {
            moveTo = "0";
        }
		
        int offset = 0;
        int pageNbr = 1;
        int totalPages = 1;
		try {
	        pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pgId), Integer.parseInt(moveTo));
	        offset = IndexingUtils.getStartingElement(pageNbr, 20);
	        totalPages = IndexingUtils.getNumberOfPages(((Collection)results).size(), 20);
		} catch (NumberFormatException nfe) {
			log.warn("cannot parse " + pgId + " into a number... defaulting to zero.");
		}
		
		_request.setAttribute(RequestScopeConstants.REQUEST_OFFSET_KEY, Integer.valueOf(offset));
        _request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
        _request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
        return _mapping.findForward("show-results");
	}
    
}
