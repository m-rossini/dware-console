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
 * Created on Sep 4, 2004
 */
package br.com.auster.dware.console.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.facelift.services.ServiceLocator;

/** 
 * @author Frederico A Ramos
 * @version $Id: ErrorAction.java 611 2008-02-15 12:50:02Z wsoares $
 */
public class ErrorAction extends Action {

	
    
    public static final String FORWARD_ERROR_PAGE = "error";
    private static Logger log = Logger.getLogger(ErrorAction.class);
	
    //########################################
    // Instance methods
    //########################################
    
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
            					 HttpServletRequest _request, HttpServletResponse _response) {

        //_request.setAttribute(ExceptionConstants.USERNOTLOGGED_MESSAGE_KEY, WebUtils.findRequestAttribute(_request, ExceptionConstants.USERNOTLOGGED_MESSAGE_KEY));
        //_request.setAttribute(ExceptionConstants.EXCEPTION_MESSAGE, WebUtils.findRequestAttribute(_request, ExceptionConstants.EXCEPTION_MESSAGE));
        
        _request.setAttribute(RequestScopeConstants.REQUEST_NOTLOGGED_KEY, RequestScopeConstants.REQUEST_NOTLOGGED_KEY);
        
        log.debug("User time-out inactived :" + ExceptionConstants.USERNOTLOGGED_MESSAGE_KEY);
        
        ServiceLocator.getInstance().getAuditService().audit(ExceptionConstants.USERNOTLOGGED_MESSAGE_KEY);
        // cria um novo message contendo o erro.

        ActionMessages messages = new ActionMessages();
         messages.add(Globals.MESSAGE_KEY, new
         ActionMessage(ExceptionConstants.USERNOTLOGGED_MESSAGE_KEY));
        saveMessages(_request, messages);

    	return _mapping.findForward(FORWARD_ERROR_PAGE);
    }
}
