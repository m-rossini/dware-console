/*
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 04-04-2007
 */
package br.com.auster.dware.console.thresholds;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.auster.billcheckout.thresholds.NFThreshold;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.thresholds.views.NFThresholdView;

/**
 * @author framos
 * @version $Id$
 */
public class ListNFThresholdAction extends Action {


	private static final String FORWARD_SUCCESS = "success";
	private static final Logger log = Logger.getLogger(ListNFThresholdAction.class);

	/**
	 * Displays all NF thresholds configured
	 */
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {
    	Session s = null;
    	try {
	    	Context ctx = new InitialContext();
	    	SessionFactory sf = (SessionFactory) ctx.lookup(ServiceConstants.HIBERNATE_SESSION_JNDI);
	    	s = sf.openSession();
	    	Criteria criteria = s.createCriteria(NFThreshold.class);

	    	List results = criteria.list();

	    	NFThreshold local = null;
	    	NFThreshold  ld = null;
	    	if ((results != null) && (results.size() > 0)) {
	    		NFThreshold thres1 = (NFThreshold) results.get(0);
		    	log.debug("Thresholds1 ==> " + ToStringBuilder.reflectionToString(thres1));
	    		NFThreshold thres2 = (results.size() > 1 ? (NFThreshold) results.get(1) : null);
		    	log.debug("Thresholds2 ==> " + ToStringBuilder.reflectionToString(thres2));
	    		if (thres1.isLocalNF()) {
	    			local = thres1;
	    			ld = thres2;
	    		} else {
	    			local = thres2;
	    			ld = thres1;
	    		}
	    		if (thres2 != null) {
		    		if (thres2.isLocalNF()) {
		    			local = thres2;
		    			ld = thres1;
		    		} else {
		    			local = thres1;
		    			ld = thres2;
		    		}
	    		}
	    	}
    		NFThresholdView view = new NFThresholdView(local, ld);
    		_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, view);
	    	return _mapping.findForward(FORWARD_SUCCESS);

    	} catch (Exception ne) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", ne));
			saveMessages(_request, messages);
    		throw new PortalRuntimeException(ne);
    	}
    	finally {
    		if (s != null) { s.close(); }
    	}
    }

}