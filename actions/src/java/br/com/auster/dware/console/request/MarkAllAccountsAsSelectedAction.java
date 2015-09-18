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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.SessionScopeConstants;

/**
 * @author Frederico A Ramos
 * @version $Id: MarkAllAccountsAsSelectedAction.java 30 2005-05-24 18:46:06Z framos $
 */
public class MarkAllAccountsAsSelectedAction extends Action {

	
	Logger log = Logger.getLogger(MarkAllAccountsAsSelectedAction.class);
	I18n i18n = I18n.getInstance(MarkAllAccountsAsSelectedAction.class);
	

    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {
        
        
        Map resultList = (Map) _request.getSession(false).getAttribute(SessionScopeConstants.SESSION_LISTOFRESULTS_KEY);
        if (resultList != null) {
            _request.getSession(false).setAttribute(SessionScopeConstants.SESSION_CACHEDACCOUNTS_KEY, resultList.keySet());
            return _mapping.findForward("save-accounts");
        }
        _request.getSession(false).removeAttribute(SessionScopeConstants.SESSION_CACHEDACCOUNTS_KEY);
    	return _mapping.findForward("show-accounts");
    }    
   
}
