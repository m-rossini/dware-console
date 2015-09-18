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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.web.utils.WebUtils;

/**
 * @author Frederico A Ramos
 * @version $Id: ParseUploadForAccountsAction.java 30 2005-05-24 18:46:06Z framos $
 */
public abstract class ParseUploadForAccountsAction extends Action {

	
    
    Logger log = Logger.getLogger(ParseUploadForAccountsAction.class);
    I18n i18n = I18n.getInstance(ParseUploadForAccountsAction.class);
    

    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {

       String filepath = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_UPLOADEDFILE_KEY);
       if (filepath == null) {
           throw new PortalRuntimeException("file not defined");
       }
       
       String filesize = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_UPLOADEDFILESIZE_KEY);
       if (filesize == null) {
           filesize = "";
       }

	   parseFile(filepath, _request);
       return _mapping.findForward("show-accounts");
    }    

	protected abstract void parseFile(String _filename, HttpServletRequest _request);

}
