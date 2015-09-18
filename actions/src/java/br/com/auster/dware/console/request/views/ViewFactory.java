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
 * Created on Mar 14, 2005
 */
package br.com.auster.dware.console.request.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.model.OutputFile;
import br.com.auster.facelift.requests.model.Request;
import br.com.auster.facelift.requests.model.Trail;
import br.com.auster.facelift.requests.web.interfaces.WebRequestLifeCycle;
import br.com.auster.facelift.requests.web.model.BundleFile;
import br.com.auster.facelift.requests.web.model.NotificationEmail;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.security.model.User;

/**
 * @author framos
 * @version $Id: ViewFactory.java 640 2008-09-18 13:44:50Z framos $
 */
public abstract class ViewFactory {


	private static final Logger log = Logger.getLogger(ViewFactory.class);

    /**
     * Creates a list of <code>RequestView</code> out of a list of <code>Request</code>
     */
    public static List createRequestList(Collection _list, Collection _users) throws RequestManagerException, PersistenceResourceAccessException{
        Map userMap = new HashMap();
        if (_users != null) {
            Iterator iterator =  _users.iterator();
            while (iterator.hasNext()) {
                User token = (User) iterator.next();
                userMap.put(String.valueOf(token.getUid()),token);
            }
        }
        if ((_list != null)  && (_list.size() > 0)) {
            Iterator iterator = _list.iterator();
            ArrayList newList = new ArrayList(_list.size());
            while (iterator.hasNext()) {
                WebRequest req = (WebRequest)iterator.next();
				RequestView requestView = createRequest(req, (User) userMap.get(String.valueOf(req.getOwnerId())));
                newList.add( requestView );
            }
            return newList;
        }
        return null;
    }

    public static RequestView createRequest(WebRequest _request) {
        return createRequest(_request, null);
    }

    public static RequestView createRequest(WebRequest _request, User _user) {
        if (_request == null) {
            return null;
        }
        RequestView view = new RequestView();
        view.setId(_request.getRequestId());
        if (_user != null) {
            view.setOwner(_user.getFullName());
        }
        view.setStartDate(_request.getStartDate());
        view.setFinishDate(_request.getEndDate());
        view.setLatestStatus(_request.getStatus());
        view.setCycleId((String) _request.getAdditionalInformation().get("cycle.id"));
        // create list of notifications
        if (_request.getNotifications() != null) {
            Iterator iterator = _request.getNotifications().iterator();
            List emailList = new ArrayList();
            while (iterator.hasNext()) {
                NotificationEmail email = (NotificationEmail) iterator.next();
                NotificationView emailView = new NotificationView();
                emailView.setEmailAddress(email.getEmailAddress());
                emailView.setSentDatetime(email.getSentDatetime());
                emailList.add(emailView);
            }
            view.setEmailList(emailList);
        }
        // create list of bundle files
        if (_request.getBundleFiles() != null) {
            Iterator iterator = _request.getBundleFiles().iterator();
            List bundleList = new ArrayList();
            while (iterator.hasNext()) {
                BundleFile file = (BundleFile) iterator.next();
                FileView fileView = new FileView();
                fileView.setFilename(file.getFilename());
                fileView.setGenerationDate((java.util.Date)file.getFileDatetime());
                fileView.setMessage(file.getMessage());
                fileView.setConsequenceCount(file.getConsequencesCount());
                bundleList.add(fileView);
            }
// A colecao ja vem ordenada da maneira correta, por isso não ha mais necessidade de ordenala novamente
//            MultiFieldComparator mfc = new MultiFieldComparator(new String[] { "message" });
//            Collections.sort(bundleList, mfc);
            view.setBundleList(bundleList);
        }
        // set counters
        String tot =  (String)_request.getAdditionalInformation().get("request.size");
        if (tot != null) {
        	try {
        	view.setTotalCount( Integer.valueOf(tot).intValue());
        	} catch (Exception e) {
        		log.error("Could not handle request size " + tot);
        	}
        }
        // if there is no request.size, then try CREATE counter
        // TODO this should be removed for newer versions since there is no CREATE status anymore
        if (view.getTotalCount() == 0) {
        	view.setTotalCount(_request.getCounterForStatus(WebRequestLifeCycle.REQUEST_LIFECYCLE_CREATED));
        }
		view.setFinishedCount(_request.getCounterForStatus(WebRequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_OK) +
			               	  _request.getCounterForStatus(WebRequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_ERROR));
		log.debug("TotalCount  ==> " + view.getTotalCount());
		log.debug("FinidhedCount  ==> " + view.getFinishedCount());
        // set a consequenceCounters List
        view.setConsequenceCounters(_request.getConsequenceCounters());
        return view;
    }


    /**
     * Creates a list of <code>AccountView</code> out of a list of <code>RequestSection</code>
     */
    public static List createRequestAccountList(Collection _sections) {
        if ((_sections == null) || (_sections.size() <= 0)) {
            return null;
        }
        List list = new ArrayList();
        Iterator iterator = _sections.iterator();
        while (iterator.hasNext()) {
            list.add(createRequestAccount((Request) iterator.next()));
        }
        return list;
    }

    public static AccountView createRequestAccount(Request _procRequest) {
        if (_procRequest == null) {
            return null;
        }
        AccountView view = new AccountView();
        view.setId(_procRequest.getRequestId());
        view.setAccountId((String) _procRequest.getLabel());
        if (_procRequest.getAdditionalInformation() != null) {
        	if (_procRequest.getAdditionalInformation().containsKey("account.name")) {
        		view.setCustomerName((String) _procRequest.getAdditionalInformation().get("account.name"));
        	}
        }
        view.setLatestStatus(_procRequest.getLatestStatus());
        // get latest status && start date
        if ((_procRequest.getTrails() != null) && (_procRequest.getTrails().size() > 0)) {
            Trail trail = (Trail) _procRequest.getTrails().get(0);
            view.setStartDate(trail.getTrailDate());
            trail = (Trail) _procRequest.getTrails().get(_procRequest.getTrails().size()-1);
        }
        return view;
    }


    /**
     * Creates a list of <code>TrailView</code> out of the trails from the incoming request object
     */
    public static List createTrailList(Request _request) {
        if (_request == null) {
            return null;
        }
        ArrayList list = new ArrayList();
        Iterator iterator = _request.getTrails().iterator();
        while (iterator.hasNext()) {
            Trail currentTrail = (Trail) iterator.next();
            TrailView view = new TrailView();
            view.setMessage(currentTrail.getMessage());
            view.setStatus(currentTrail.getStatus());
            view.setTrailDate(currentTrail.getTrailDate());
            list.add(view);
        }
        return list;
    }

    /**
     * Creates a list of <code>FormatView</code> out of a list of <code>RequestSection</code>
     */
    public static Map createFormatMap(Request _request) {
        if (_request == null) {
            return null;
        }
        HashMap map = new HashMap();
        Iterator iterator = _request.getTrails().iterator();
        while (iterator.hasNext()) {
            Trail currentTrail = (Trail) iterator.next();
            List outFiles = currentTrail.getOutputFiles();
            if (outFiles != null) {
                addFileToMap(map, outFiles);
            }
        }
        return map;
    }

    /**
     * Creates a list of <code>FileView</code> out of a list of <code>GeneratedFile</code>
     */
    public static void addFileToMap(Map _map, List _files) {
        Iterator iterator = _files.iterator();
        // iterating through all files
        while (iterator.hasNext()) {
            OutputFile file = (OutputFile) iterator.next();
            Map attributes = file.getAttributes();
            // iterating throuugh possible formats this file is associated with
            if (attributes != null) {
                Iterator attIterator = attributes.keySet().iterator();
                while (attIterator.hasNext()) {
                    String key = (String) attIterator.next();
                    if (! key.startsWith("requests.format.")) {
                        continue;
                    }
                    Set filesSet = (Set) _map.get(key);
                    if (filesSet == null) {
                        filesSet = new HashSet();
                        _map.put(key, filesSet);
                    }
                    FileView fileView = new FileView();
                    fileView.setFilename(file.getFilename());
                    filesSet.add(fileView);
                }
            }
        }
    }
//
//    private static List createTrailList(List _trails) {
//        Iterator iterator = _trails.iterator();
//        ArrayList trailList = new ArrayList();
//        while (iterator.hasNext()) {
//            Trail t = (Trail) iterator.next();
//            TrailView view = new TrailView();
//            view.setMessage(t.getMessage());
//            view.setStatus(t.getStatus());
//            view.setTrailDate(t.getTrailDate());
//            trailList.add(view);
//        }
//        return trailList;
//    }

}
