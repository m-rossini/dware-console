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

import java.util.Collection;
import java.util.Iterator;

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
import org.apache.struts.action.DynaActionForm;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.auster.billcheckout.thresholds.NFThreshold;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;

/**
 * @author framos
 * @version $Id$
 */
public class UpdateNFThresholdAction extends Action {

	private static final Logger log = Logger.getLogger(UpdateNFThresholdAction.class);
	private static final String FORWARD_SUCCESS = "success";


	/**
	 * Update NF Thresholds
	 */
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

    	Session s = null;
    	try {
	    	DynaActionForm form = (DynaActionForm) _form;
	    	log.debug("DynactionForm thresholds ");
	    	Context ctx = new InitialContext();
	    	SessionFactory sf = (SessionFactory) ctx.lookup(ServiceConstants.HIBERNATE_SESSION_JNDI);
	    	s = sf.openSession();
	    	Criteria criteria = s.createCriteria(NFThreshold.class);
	    	// loading current info for thresholds
	    	Collection currentThresholds = s.createCriteria(NFThreshold.class).list();
	    	NFThreshold localThreshold = new NFThreshold();
	    	NFThreshold ldThreshold = new NFThreshold();
	    	if(currentThresholds != null){
		    	for (Iterator it = currentThresholds.iterator(); it.hasNext();) {
		    		NFThreshold threshold = (NFThreshold) it.next();
			    	log.debug("Thresholds OBJ " + ToStringBuilder.reflectionToString(threshold));
		    		if (threshold.isLocalNF()) {
		    			log.debug("Local NF threshold found with uid " + threshold.getUid());
		    			localThreshold = threshold;
		    		} else {
		    			log.debug("LD NF threshold found with uid " + threshold.getUid());
		    			ldThreshold = threshold;
		    		}
		    	}
	    	}
	    	log.debug("Starting to update nf thresholds ");
	    	updateThreshold(localThreshold, form, true);
	    	updateThreshold(ldThreshold, form, false);
	    	// saving objects
	    	Transaction t = s.beginTransaction();
	    	s.saveOrUpdate(ldThreshold);
	    	s.saveOrUpdate(localThreshold);
	    	t.commit();
	    	log.debug("NF thresholds updated successfully");

			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.update.ok", this.getClass().getPackage().getName(), SessionHelper.getUsername(_request));

	    	return _mapping.findForward(FORWARD_SUCCESS);
    	} catch (Exception e) {
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.update.error", this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), e);
			// exception handling
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
    		throw new PortalRuntimeException(e);
    	} finally {
    		if (s != null) { s.close(); }
    	}
    }

    /**
     * Private helper methods
     *
     * @param _threshold
     * @param _form
     * @param _isLocal
     */
    private void updateThreshold(NFThreshold _threshold, DynaActionForm _form, boolean _isLocal) {
    	// updating local threshold
    	if (_threshold == null) {
    		_threshold = new NFThreshold();
    	}
		_threshold.setLocalNF(_isLocal);
    	log.debug("Next 3 update messages are related to Local NF ? " + _isLocal);
    	if (_isLocal) {
    		// setting all local attributes
    		if ("".equals(_form.get("localUpperAmount"))) {
        		_threshold.setUpperAmount(NFThreshold.DISABLED_LIMIT);
    		} else {
        		_threshold.setUpperAmount(Double.parseDouble((String)_form.get("localUpperAmount")));
    		}
    		if ("".equals(_form.get("localLowerAmount"))) {
        		_threshold.setLowerAmount(NFThreshold.DISABLED_LIMIT);
    		} else {
        		_threshold.setLowerAmount(Double.parseDouble((String)_form.get("localLowerAmount")));
    		}
    		_threshold.setHintMessage((String) _form.get("localMessage"));
    	} else {
    		// setting all LD attributes
    		if ("".equals(_form.get("ldUpperAmount"))) {
        		_threshold.setUpperAmount(NFThreshold.DISABLED_LIMIT);
    		} else {
        		_threshold.setUpperAmount(Double.parseDouble((String)_form.get("ldUpperAmount")));
    		}
    		if ("".equals(_form.get("ldLowerAmount"))) {
        		_threshold.setLowerAmount(NFThreshold.DISABLED_LIMIT);
    		} else {
        		_threshold.setLowerAmount(Double.parseDouble((String)_form.get("ldLowerAmount")));
    		}
    		_threshold.setHintMessage((String) _form.get("ldMessage"));
    	}

    	log.debug("NF threshold upper limit updated to " + _threshold.getUpperAmount());
    	log.debug("NF threshold lower limit updated to " + _threshold.getLowerAmount());
    	log.debug("NF threshold hint message updated to " + _threshold.getHintMessage());
    }
}