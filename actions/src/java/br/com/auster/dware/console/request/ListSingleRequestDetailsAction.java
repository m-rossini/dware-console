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
 * Created on Sep 19, 2004
 */
package br.com.auster.dware.console.request;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.request.views.AccountView;
import br.com.auster.dware.console.request.views.ViewFactory;
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.model.Request;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.services.ServiceLocator;

/**
 * @author Frederico A Ramos
 * @version $Id: ListSingleRequestDetailsAction.java 255 2006-09-13 15:45:09Z framos $
 */
public class ListSingleRequestDetailsAction extends Action {

	
    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {

        DynaActionForm form = (DynaActionForm) _form;
        if ((form.get("requestId") == null) || (form.get("accountId") == null)) {
            return _mapping.findForward("error");
        }
        
        long requestId = ((Long) form.get("requestId")).longValue();
        
        try {
            WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
            
            Request procRequest = manager.loadRequestDetail(((Long) form.get("accountId")).longValue());
            if (procRequest == null) {
                return _mapping.findForward("error");
            }
            
            Map files = ViewFactory.createFormatMap(procRequest);
            List trails = ViewFactory.createTrailList(procRequest);
            AccountView acc = ViewFactory.createRequestAccount(procRequest);
            
            _request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, String.valueOf(requestId));
            _request.setAttribute(RequestScopeConstants.REQUEST_SECTIONINFO_KEY, acc);
            _request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, trails);
            _request.setAttribute(RequestScopeConstants.REQUEST_LISTOFFILES_KEY, files);
            
        } catch (RequestManagerException rme) {
            throw new PortalRuntimeException(rme);
        } catch (PersistenceResourceAccessException prae) {
            throw new PortalRuntimeException(prae);
        }
        
        return _mapping.findForward("show-details");
    }
}
