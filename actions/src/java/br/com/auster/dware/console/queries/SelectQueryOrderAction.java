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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
import br.com.auster.dware.console.queries.views.ViewFactory;
import br.com.auster.facelift.queries.interfaces.QueryFunctions;
import br.com.auster.facelift.queries.interfaces.QueryManager;
import br.com.auster.facelift.queries.model.ColumnObject;
import br.com.auster.facelift.queries.model.ViewObject;
import br.com.auster.facelift.services.ServiceLocator;



/**
 * @author Frederico A Ramos
 * @version $Id: SelectQueryFieldsAction.java 146 2005-11-04 13:07:02Z framos $
 */
public class SelectQueryOrderAction extends Action {

    
    
	
    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {

        
        DynaActionForm form = (DynaActionForm) _form;
        String tableName = (String) form.get("table");
        
        // putting table in request
        QueryManager manager = (QueryManager) ServiceLocator.getInstance().getService(ServiceConstants.QUERYMANAGER_SERVICE);
        ViewObject view = manager.loadViewDetails(tableName);
        _request.setAttribute(RequestScopeConstants.REQUEST_QUERYTABLE_KEY, ViewFactory.createTableView(view));
        
        // saving list of selected fields
        String[] fields = (String[]) form.get("queryField");
        _request.setAttribute(RequestScopeConstants.REQUEST_QUERYFIELDLIST_KEY, Arrays.asList(fields));
        
        // saving list of designated conditions
        String[] conditions = (String[]) form.get("queryCondition");
        _request.setAttribute(RequestScopeConstants.REQUEST_QUERYCONDITIONLIST_KEY, Arrays.asList(conditions));

        Boolean runNow = new Boolean((String)form.get("runNow"));
        if ((runNow != null) && (runNow.booleanValue())) {
        	return _mapping.findForward("execute-query");
        }
        
        // building list of fields to be displayed in the order-by page
        List orderByList = new ArrayList(); 
        for (Iterator it=view.getColumns().iterator(); it.hasNext();) {
        	ColumnObject obj = (ColumnObject) it.next();
        	for (int i=0; i < fields.length; i++) {
        		if ((fields[i].indexOf(QueryFunctions.COLUMN_RAW_VALUE) >= 0) && (fields[i].indexOf(obj.getQualifiedName()) > 0)){
        			orderByList.add(obj);
        		}
        	}
        }
        
        if (orderByList.size() > 0) {
        	_request.setAttribute(RequestScopeConstants.REQUEST_QUERYORDERLIST_KEY, orderByList);
        	return _mapping.findForward("show-orderBy");
        } 
    	return _mapping.findForward("execute-query");
    }
}
