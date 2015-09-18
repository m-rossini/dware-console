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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.queries.interfaces.QueryException;
import br.com.auster.facelift.queries.interfaces.QueryFunctions;
import br.com.auster.facelift.queries.interfaces.QueryManager;
import br.com.auster.facelift.queries.model.ColumnObject;
import br.com.auster.facelift.queries.model.Query;
import br.com.auster.facelift.queries.model.ViewObject;
import br.com.auster.facelift.services.ServiceLocator;



/**
 * @author Frederico A Ramos
 * @version $Id: ExecuteQueryAction.java 190 2005-12-16 11:22:48Z framos $
 */
public class ExecuteQueryAction extends Action {

    
    
	
    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {

        try {
	        QueryManager manager = (QueryManager) ServiceLocator.getInstance().getService(ServiceConstants.QUERYMANAGER_SERVICE);
	
	        DynaActionForm form = (DynaActionForm) _form;
	        String tableName = (String) form.get("table");
	        ViewObject table = manager.loadViewDetails(tableName);
	        Query query = manager.createQuery(table);
	               
	        String[] fields = (String[]) form.get("queryField");
	        buildQueryFields(query, fields);
	        
	        String[] conditions = (String[]) form.get("queryCondition");
	        buildQueryConditions(query, table, conditions);

	        String[] orderBy = (String[]) form.get("orderField");
	        buildQueryOrderBy(query, table, orderBy);

            Collection result = manager.executeQuery(query);
            _request.getSession(false).setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, result);
            _request.getSession(false).setAttribute(RequestScopeConstants.REQUEST_QUERYINFO_KEY, query);
        } catch (QueryException qe) {
            throw new PortalRuntimeException(qe);
        }        
        return _mapping.findForward("show-results");
    }
    
    
    private void buildQueryFields(Query _query, String[] _fields) {
        if (_fields == null) {
            throw new IllegalStateException("no fields where defined");
        }
		
		ViewObject currentView = _query.getSourceView();
        for (int i=0; i < _fields.length; i++) {
			String fieldName = _fields[i];
            if (_fields[i].indexOf("$") < 0) {
                _query.addColumn(QueryFunctions.COLUMN_RAW_VALUE, currentView.getColumn(fieldName));
            } else {
                int pos = _fields[i].indexOf("$");
                _query.addColumn( _fields[i].substring(0, pos), currentView.getColumn(_fields[i].substring(pos+1)) );
            }
        }
    }
    
    private void buildQueryConditions(Query _query, ViewObject _table, String[] _fields) {

    	for (int i=0; i < _fields.length; i++) {
			String fieldName = _fields[i];
			int firstPos = _fields[i].indexOf("$");
			int secondPos = _fields[i].indexOf("$",firstPos+1);
			String op = _fields[i].substring(0, firstPos);
            String value = fieldName.substring(secondPos+1);
            if ((QueryFunctions.STRING_LIKE.equals(op)) || (QueryFunctions.STRING_NOT_LIKE.equals(op))) {
            	value = "%" + value + "%";
            }
        	_query.or(QueryFunctions.COLUMN_RAW_VALUE, _table.getColumn(fieldName.substring(firstPos+1, secondPos)), op, value);
        }
    	
        return;
    }
    
    private void buildQueryOrderBy(Query _query, ViewObject _table, String[] _fields) {
    	
    	for (int i=0; i < _fields.length; i++) {
			int pos = _fields[i].indexOf("$");
			ColumnObject col = _table.getColumn(_fields[i].substring(pos+1));
			if (col != null) {
				_query.orderBy(col, QueryFunctions.ORDER_ASCENDING.equals(_fields[i].substring(0, pos)));
			}
    	}
    }
    
}
