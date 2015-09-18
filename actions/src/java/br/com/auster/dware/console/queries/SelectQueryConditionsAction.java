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

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.queries.views.ViewFactory;
import br.com.auster.facelift.queries.interfaces.QueryManager;
import br.com.auster.facelift.queries.model.ViewObject;
import br.com.auster.facelift.services.ServiceLocator;



/**
 * @author Frederico A Ramos
 * @version $Id: SelectQueryConditionsAction.java 190 2005-12-16 11:22:48Z framos $
 */
public class SelectQueryConditionsAction extends Action {

    
    
	
    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {

        QueryManager manager = (QueryManager) ServiceLocator.getInstance().getService(ServiceConstants.QUERYMANAGER_SERVICE);

        DynaActionForm form = (DynaActionForm) _form;
        String tableName = (String) form.get("table");
       
        ViewObject view = manager.loadViewDetails(tableName);
        _request.setAttribute(RequestScopeConstants.REQUEST_QUERYTABLE_KEY, ViewFactory.createTableView(view));
        
        String[] fields = (String[]) form.get("queryField");
        _request.setAttribute(RequestScopeConstants.REQUEST_QUERYFIELDLIST_KEY, Arrays.asList(fields));
        
        Boolean runNow = new Boolean((String)form.get("runNow"));
        if ((runNow != null) && (runNow.booleanValue())) {
        	return _mapping.findForward("execute-query");
        }
        
        return _mapping.findForward("show-conditions");
    }
}
