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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.facelift.requests.interfaces.RequestLifeCycle;
import br.com.auster.facelift.requests.model.InputFile;
import br.com.auster.facelift.requests.model.Request;
import br.com.auster.facelift.requests.model.Trail;
import br.com.auster.facelift.requests.web.model.WebRequest;

/**
 * @author Frederico A Ramos
 * @version $Id: SaveAccountsSelectionAction.java 64 2005-06-08 19:32:23Z framos $
 */
public class SaveAccountsSelectionAction extends Action {

	
	Logger log = Logger.getLogger(SaveAccountsSelectionAction.class);
	I18n i18n = I18n.getInstance(SaveAccountsSelectionAction.class);
	

    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {
        
 
        Set selected = (Set)_request.getSession(false).getAttribute(SessionScopeConstants.SESSION_CACHEDACCOUNTS_KEY);
        Map allAccounts = (Map) _request.getSession(false).getAttribute(SessionScopeConstants.SESSION_LISTOFRESULTS_KEY);
        if ((selected == null) || (allAccounts == null)) {
            throw new IllegalStateException("no accounts selected or there are no accounts to be selected");
        }
        
        if (selected.size() <= 0) {
            _request.getSession(false).setAttribute(SessionScopeConstants.SESSION_SELECTEDACCOUNTS_ERROR_KEY, "true");
            return _mapping.findForward("backTo-show-accounts");
        }
        
        //adding accounts to request
        WebRequest request = (WebRequest) _request.getSession(false).getAttribute(SessionScopeConstants.SESSION_NEWREQUEST_KEY);
        if (request == null) {
            throw new IllegalStateException("no request initialized in current scope");
        }
        String filename = (String) _request.getSession(false).getAttribute(SessionScopeConstants.SESSION_UPLOADEDFILE_KEY);
        if (filename == null) {
            throw new IllegalStateException("no input file selected");            
        }
        createAccountList(request, allAccounts, selected, filename);
        _request.getSession(false).setAttribute(SessionScopeConstants.SESSION_NEWREQUEST_KEY, request);
        // removing session information no longer needed
        _request.getSession(false).removeAttribute(SessionScopeConstants.SESSION_CACHEDACCOUNTS_KEY);
        _request.getSession(false).removeAttribute(SessionScopeConstants.SESSION_LISTOFRESULTS_KEY);
        
    	return _mapping.findForward("show-formats");
    }    
    
    
    private void createAccountList(WebRequest _request, Map _allAccounts, Set _selected, String _filename) {
        
        Iterator iterator = _selected.iterator();
        Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
        
        _request.setStartDate(now);
        
        Map addInfo;
        List trailsList;
        Set filesSet;
        
        Set requestSet = new HashSet();
        while (iterator.hasNext()) {
            
            Request procRequest = new Request();   
            
            String accountId = (String) iterator.next();
            String accountName = (String) _allAccounts.get(accountId);
            procRequest.setLabel(accountId);

            addInfo = new HashMap();
            addInfo.put("account.name", accountName);
            procRequest.setAdditionalInformation(addInfo);
            
            Trail trailInfo = new Trail();
            trailInfo.setRequest(procRequest);
            trailInfo.setStatus(RequestLifeCycle.REQUEST_LIFECYCLE_CREATED);
            trailInfo.setTrailDate(now);
            trailsList = new ArrayList();
            trailsList.add(trailInfo);
            procRequest.setTrails(trailsList);
            
            filesSet = new HashSet();
            InputFile inFile = new InputFile();
            inFile.setFilename(_filename);
            inFile.setRequest(procRequest);
            filesSet.add(inFile);
            procRequest.setInputFiles(filesSet);
            
            requestSet.add(procRequest);
        }
        _request.setProcessingRequests(requestSet);
    }
   
}
