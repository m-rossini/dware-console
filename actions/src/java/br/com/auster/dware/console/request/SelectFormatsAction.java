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
 * Created on Sep 25, 2004
 */
package br.com.auster.dware.console.request;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.facelift.requests.model.Request;
import br.com.auster.facelift.requests.web.model.WebRequest;

/**
 * @author Frederico A Ramos
 * @version $Id: SelectFormatsAction.java 264 2006-09-21 13:03:13Z framos $
 */
public class SelectFormatsAction extends Action {

	

    Logger log = Logger.getLogger(SaveAccountsSelectionAction.class);
    I18n i18n = I18n.getInstance(SaveAccountsSelectionAction.class);
    
    
    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {
        
        DynaActionForm form = (DynaActionForm) _form;
        String[] options = (String[]) form.get("format");

        //adding formats to request
        WebRequest request = (WebRequest) _request.getSession(false).getAttribute(SessionScopeConstants.SESSION_NEWREQUEST_KEY);
        if (request == null) {
            throw new IllegalStateException("no request initialized in current scope");
        }
        createRequestFormats(request, options);
        _request.getSession(false).setAttribute(SessionScopeConstants.SESSION_NEWREQUEST_KEY, request);
        return _mapping.findForward("show-notification");
    }
    
    
    private void createRequestFormats(WebRequest _request, String[] _formats) {
    	if (_formats == null) { return; }
        Iterator iterator = _request.getProcessingRequests().iterator();
        while (iterator.hasNext()) {
            Request procRequest = (Request) iterator.next();            
            for (int i=0; i < _formats.length; i++) {
                procRequest.getAdditionalInformation().put(_formats[i], "true");
            }
        }
    }
}
