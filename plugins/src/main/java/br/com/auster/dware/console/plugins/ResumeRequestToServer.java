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
 * Created on Apr 8, 2005
 */
package br.com.auster.dware.console.plugins;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.facelift.requests.interfaces.RequestCriteria;
import br.com.auster.facelift.requests.interfaces.RequestLifeCycle;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.model.Request;
import br.com.auster.facelift.requests.model.Trail;
import br.com.auster.facelift.requests.web.interfaces.WebRequestLifeCycle;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;

public class ResumeRequestToServer extends PostRequestToServer {


    private int currentRequest=0;
    

    
    
    
    public void execute() {
        int total = getContext().getExecutionParameters().size();
        for (currentRequest=0; currentRequest < total; currentRequest++) {
            super.execute();
        }
    }
    
    public WebRequest getWebRequest() {
        if ((getContext().getExecutionParameters() == null) ||
            (getContext().getExecutionParameters().size() <= 0)) {
	        return null;
        }
		//List requestList = (List) getContext().getExecutionParameters().get(0);
		//return (WebRequest) requestList.get(currentRequest);
		return (WebRequest) getContext().getExecutionParameters().get(currentRequest);
    }    
    
    public Set getProcessingRequests(WebRequest _request) {
        try {
            WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
            
            Set resultSet = new HashSet();
            RequestCriteria criteria = new RequestCriteria();
            // getting CREATED requests
            criteria.setStatus(RequestLifeCycle.REQUEST_LIFECYCLE_CREATED);
			List result = manager.findWebRequestProcesses(_request.getRequestId(), criteria);
			resultSet.addAll(result);
            // getting IN PROCESS requests
            criteria.setStatus(RequestLifeCycle.REQUEST_LIFECYCLE_INPROCESS);
            result = manager.findWebRequestProcesses(_request.getRequestId(), criteria);
            resultSet.addAll(result);
			// getting RESTARTED requests
            criteria.setStatus(RequestLifeCycle.REQUEST_LIFECYCLE_RESTARTED);
            result = manager.findWebRequestProcesses(_request.getRequestId(), criteria);
            resultSet.addAll(result);			
            return resultSet;
        } catch (RequestManagerException rme) {
            throw new PluginRuntimeException("business exception", rme);
        } catch (PersistenceResourceAccessException prae) {
            throw new PluginRuntimeException("Error while acessing persistence layer", prae);
		}
    }
    
	protected void updateRequestStatus(Set _results) {
		WebRequest request = getWebRequest();
		try {
			WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
			manager.updateWebRequestStatus(request.getRequestId(), WebRequestLifeCycle.REQUEST_LIFECYCLE_INPROCESS);
			
			Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
			Iterator iterator = _results.iterator();
	        Trail trail = new Trail();
	        trail.setStatus(RequestLifeCycle.REQUEST_LIFECYCLE_RESTARTED);
	        trail.setTrailDate(now);
			while (iterator.hasNext()) {
				Request req = (Request)iterator.next();
		        req.getTrails().add(trail);
				manager.updateRequestStatus(req.getRequestId(), trail);
			}
		} catch (PersistenceResourceAccessException prae) {
			throw new PluginRuntimeException("could not persist status modification", prae);
		} catch (RequestManagerException rme) {
			throw new PluginRuntimeException("error calling update status method", rme);
		}
	}
	
}
