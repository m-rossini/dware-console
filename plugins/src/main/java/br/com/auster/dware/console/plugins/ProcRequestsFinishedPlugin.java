/*
 * Copyright (c) 2004 Auster Solutions. All Rights Reserved.
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
 * Created on Apr 13, 2005
 */
package br.com.auster.dware.console.plugins;

import java.util.Map;

import org.apache.log4j.Logger;

import br.com.auster.common.log.LogFactory;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.facelift.requests.interfaces.RequestLifeCycle;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.model.Trail;
import br.com.auster.facelift.requests.web.interfaces.WebRequestLifeCycle;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.services.plugin.FaceliftPlugin;
import br.com.auster.facelift.services.plugin.PluginContext;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;
import br.com.auster.persistence.PersistenceResourceAccessException;

public class ProcRequestsFinishedPlugin implements FaceliftPlugin {


	
    private static Logger log = LogFactory.getLogger(ProcRequestsFinishedPlugin.class);
	
    private PluginContext context;
    private Map parameters;
    
    
    
    public PluginContext getContext() {
        return context;
    }
    
    public void setContext(PluginContext _context) {
        context = _context;
    }

    public void setConfigurationParameters(Map _params) {
        parameters = _params;
    }

    public Map getConfigurationParameters() {
        return parameters;
    }

    
    public void execute() {
        
        WebRequest webRequest = getWebRequest();
        Trail trail = getTrail();
		
		// to simulate as if the proc. request was already accounted in the web request
        if ((webRequest == null) || (trail == null)) {
            log.error("parameters for this plugin are not correctly set. Existing!");			
            return;
        }
        // check if finished
        try {
			int requestFinished = isRequestFinished(webRequest, trail.getStatus());
			if (requestFinished > 0) {
				log.debug("web request " + webRequest.getRequestId() + " finished. Checking final status");
				// if all proc. requests where finalized
		        int newStatus = WebRequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_OK;
				if (requestFinished == 2) {
		            newStatus = WebRequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_ERROR;
		        }
				log.debug("setting final status of " + newStatus + " to web request " + webRequest.getRequestId());
				WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
				manager.updateWebRequestStatus(webRequest.getRequestId(), newStatus, trail.getTrailDate());
			} 
        } catch (RequestManagerException rme) {
            throw new PluginRuntimeException("could not persist information", rme);
        } catch (PersistenceResourceAccessException prae) {
            throw new PluginRuntimeException("Error while acessing persistence layer", prae);
		}
    }

    public void execute(PluginContext _context) {
        setContext(_context);
        execute();
    }

    public void cleanup() {
        context = null;
        parameters = null;
    }
    
    
    private WebRequest getWebRequest() {
        if ((context.getExecutionParameters() == null) ||
            (context.getExecutionParameters().size() <= 0)) {
            return null;
        }
        return (WebRequest) context.getExecutionParameters().get(0);
    }

    private Trail getTrail() {
        if ((context.getExecutionParameters() == null) ||
            (context.getExecutionParameters().size() <= 2)) {
			return null;    
        }
		return (Trail) context.getExecutionParameters().get(2);
    }
    
    protected int isRequestFinished(WebRequest _request, int _status) throws RequestManagerException, PersistenceResourceAccessException {
//		try {
			// locks the request, and updates the finish counter by 1
//			WebRequestCounterManagerHome home = WebRequestCounterManagerUtil.getHome();
//			WebRequestCounterManager ejb = home.create();
//			int finishedAll = 0;
//			if ((_status == RequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_OK) || (_status == RequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_ERROR)) {
//				finishedAll = ejb.updateRequestCounter(_request.getRequestId());
//			} else {
//				finishedAll = ejb.getRequestCounter(_request.getRequestId());
//			}
//			int totalCount = ejb.getTotalCounter(_request.getRequestId());
			// checking other status 
			WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
			Map counters = manager.getWebRequestCounters(_request.getRequestId());
			Integer tmp = (Integer)counters.get(String.valueOf(RequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_ERROR));
			int finishedAll = (tmp != null ? tmp.intValue() : 0);
			int finishedError = finishedAll;
			tmp = (Integer)counters.get(String.valueOf(RequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_OK));
			finishedAll += (tmp != null ? tmp.intValue() : 0);
			tmp = (Integer)counters.get(String.valueOf(RequestLifeCycle.REQUEST_LIFECYCLE_CREATED));
			int totalCount = (tmp != null ? tmp.intValue() : 0);
			log.debug("counters : total=" + totalCount + ", finished=" + finishedAll);
			// if has finished all proc. requests
			if (totalCount == finishedAll) {
//				return 1;
	            return (finishedError == 0 ? 1 : 2);
	        }
			log.debug("there are still processing requests to finish in web request " + _request.getRequestId());
	        return 0;
//		} catch (NamingException ne) {
//			throw new PluginRuntimeException("cannot locate counter EJB", ne);
//		} catch (CreateException ce) {
//			throw new PluginRuntimeException("could not locate counter EJB", ce);
//		} catch (FinderException fe) {
//			throw new PluginRuntimeException("could not create counter EJB", fe);
//		} catch (RemoteException re) {
//			throw new PluginRuntimeException("remote exception while acessing EJB", re);
//		}
    }    

}
