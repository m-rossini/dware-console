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
 * Created on Apr 25, 2005
 */
package br.com.auster.dware.console.plugins.email;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.com.auster.common.log.LogFactory;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.facelift.requests.interfaces.RequestCriteria;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.web.interfaces.WebRequestLifeCycle;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.persistence.PersistenceException;
import br.com.auster.security.model.User;

public class DefaultEmailContentBuilder implements EmailContentBuilder {



    private SimpleDateFormat formatter;
    private Logger log = LogFactory.getLogger(DefaultEmailContentBuilder.class);




    public DefaultEmailContentBuilder() {
        formatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
    }


    public Document getContentAsXML(WebRequest _request, User _owner) throws Exception {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element parent = (Element) createRequestInfo(doc, _request, _owner);
        doc.appendChild(parent);
        parent.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        return doc;
    }


    private Node createRequestInfo(Document _doc, WebRequest _request, User _owner) {

        Element requestInfoXML = _doc.createElement("request-info");
        requestInfoXML.setAttribute("request-id", String.valueOf(_request.getRequestId()));
        requestInfoXML.setAttribute("request-date", formatter.format(_request.getStartDate()));
        requestInfoXML.setAttribute("user-name", (_owner != null ? _owner.getFullName() : "") );

        Map totals = getTotals(_request);
        requestInfoXML.setAttribute("accounts-total", (String)totals.get("total") );
        requestInfoXML.setAttribute("accounts-error", (String)totals.get("error") );
        requestInfoXML.setAttribute("accounts-ok", (String)totals.get("ok") );
        return requestInfoXML;
    }


    private Map getTotals(WebRequest _request) {

        HashMap map = new HashMap();

		int totalCounter = 0;
		int finishedOk = 0;

		try {
			WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
			RequestCriteria criteria = new RequestCriteria();
			criteria.setStatus(WebRequestLifeCycle.REQUEST_LIFECYCLE_CREATED);
	        totalCounter = manager.countWebRequestProcesses(_request.getRequestId(), criteria);
			criteria.setStatus(WebRequestLifeCycle.REQUEST_LIFECYCLE_FINISHED_OK);
	        finishedOk = manager.countWebRequestProcesses(_request.getRequestId(), criteria);
		} catch (RequestManagerException rme) {
			log.error("exception while running business manager", rme);
		} catch (PersistenceException prae) {
			log.error("exception acessing persistence resource", prae);
		}

        map.put("total", String.valueOf(totalCounter));
        map.put("error", String.valueOf(totalCounter-finishedOk));
        map.put("ok", String.valueOf(finishedOk));
        return map;
    }

}
