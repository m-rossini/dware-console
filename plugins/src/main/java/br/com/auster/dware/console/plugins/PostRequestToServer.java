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

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.auster.common.log.LogFactory;
import br.com.auster.dware.RemoteBootstrap;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.model.InputFile;
import br.com.auster.facelift.requests.model.Request;
import br.com.auster.facelift.requests.web.interfaces.WebRequestLifeCycle;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.services.plugin.FaceliftPlugin;
import br.com.auster.facelift.services.plugin.PluginContext;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;
import br.com.auster.persistence.PersistenceResourceAccessException;

public class PostRequestToServer implements FaceliftPlugin {

	public static final String CONFIGURATION_RMI_SERVER_URL = "rmiserver.url";

	public static final String CONFIGURATION_CHAINNAME = "chain.name";

	public static final String CONFIGURATION_BUILDER_NAME = "chain.builder";

	private Logger log = LogFactory.getLogger(PostRequestToServer.class);

	private PluginContext context;

	private Map parameters;

	public PluginContext getContext() {
		return context;
	}

	public void setContext(PluginContext _context) {
		context = _context;
	}

	public void setConfigurationParameters(Map _config) {
		parameters = _config;
	}

	public Map getConfigurationParameters() {
		return parameters;
	}

	public void cleanup() {
		context = null;
		parameters = null;
	}

	public void execute(PluginContext _context) {
		setContext(_context);
		execute();
	}

	public void execute() {
		WebRequest request = getWebRequest();
		if (request == null) {
			log.info("no request found to be posted to server");
			return;
		}
		try {

			log.debug("RMI Server configuration:");
			log.debug(CONFIGURATION_RMI_SERVER_URL + "=" + parameters.get(CONFIGURATION_RMI_SERVER_URL));
			log.debug(CONFIGURATION_CHAINNAME+ "=" + parameters.get(CONFIGURATION_CHAINNAME));
			log.debug(CONFIGURATION_BUILDER_NAME + "=" + parameters.get(CONFIGURATION_BUILDER_NAME));

			log.debug("Input files: " + request.getInputFiles());
			if (request.getInputFiles() != null
					&& request.getInputFiles().size() > 0) {
				this.cmdLineExecute(request);
			} else {
				this.defaultExecute(request);
			}

		} catch (RemoteException re) {
			log.error("remote exception", re);
			throw new PluginRuntimeException("remote exception", re);
		} catch (MalformedURLException mfue) {
			log.error("invalid server URL", mfue);
			throw new PluginRuntimeException("invalid server url : "
					+ (String) parameters.get(CONFIGURATION_RMI_SERVER_URL),
					mfue);
		} catch (NotBoundException nbe) {
			log.error("server not bound", nbe);
			throw new PluginRuntimeException("server not bound", nbe);
		}
	}

	protected void defaultExecute(WebRequest request) throws MalformedURLException,
			RemoteException, NotBoundException {

		log.debug("preparing list of accounts to send to server");
		Map formatsMap = new HashMap();
		Set fileSet = new HashSet();
		Set results = getProcessingRequests(request);
		if (results == null) {
			log.warn("list of processing requests is empty.");
			return;
		}
		// getting list of files and formats
		Iterator iterator = results.iterator();
		List listOfAccounts = null;
		while (iterator.hasNext()) {
			prepareRequest((Request) iterator.next(), formatsMap, fileSet);
			listOfAccounts = buildListOfAccounts(results);
		}
		if (fileSet.size() <= 0) {
			log.warn("list of input files is empty.");
			return;
		}

		log.debug("updating request and account status");
		updateRequestStatus(results);

		if ((!parameters.containsKey(CONFIGURATION_CHAINNAME))
				|| (!parameters.containsKey(CONFIGURATION_BUILDER_NAME))) {
			log.error("Server builder chain name not properly configured");
			return;
		}

		log.debug("creating server parameters");
		Map argsMap = createBuilderArgs(request, formatsMap, fileSet);

		log.debug("sending remote request to server");
		RemoteBootstrap server = (RemoteBootstrap) Naming
				.lookup((String) parameters.get(CONFIGURATION_RMI_SERVER_URL));
		server.process((String) parameters.get(CONFIGURATION_CHAINNAME),
				argsMap, listOfAccounts);
		log.debug("remote request sent");
	}

	protected void cmdLineExecute(WebRequest request) throws MalformedURLException,
			RemoteException, NotBoundException {

		log.debug("preparing list of files to send to server");
		Map formatsMap = new HashMap();
		Set fileSet = new HashSet();

		// getting list of files and formats
		prepareRequest(request, formatsMap, fileSet);

		if (fileSet.size() <= 0) {
			log.warn("list of input files is empty.");
			return;
		}

		if ((!parameters.containsKey(CONFIGURATION_CHAINNAME))
				|| (!parameters.containsKey(CONFIGURATION_BUILDER_NAME))) {
			log.error("Server builder chain name not properly configured");
			return;
		}

		log.debug("creating server parameters");
		Map argsMap = createBuilderArgs(request, formatsMap, fileSet);

		log.debug("sending remote request to server");
		RemoteBootstrap server = (RemoteBootstrap) Naming.lookup((String) parameters.get(CONFIGURATION_RMI_SERVER_URL));
		server.process((String) parameters.get(CONFIGURATION_CHAINNAME), argsMap);
		log.debug("remote request sent");
	}

	public void prepareRequest(Request _request, Map _formatsMap, Set _fileSet) {

		_formatsMap.putAll(_request.getAdditionalInformation());
		
        //setting input files
		Iterator iterator = _request.getInputFiles().iterator();
        while (iterator.hasNext()) {
			File f = new File( ((InputFile)iterator.next()).getFilename());
			log.debug("added file to process " + f.getPath() );
            _fileSet.add(f);
        }
	}

	public void prepareRequest(WebRequest _request, Map _formatsMap,
			Set _fileSet) {

		_formatsMap.putAll(this.buildFormatsMap(_request.getFormats()));

        //setting input files
		Iterator iterator = _request.getInputFiles().iterator();
        while (iterator.hasNext()) {
			File f = new File((String) iterator.next());
			log.debug("added file to process " + f.getPath() );
            _fileSet.add(f);
        }
	}

	private Map buildFormatsMap(Map infoMap) {

		List formats = new ArrayList();
		Iterator iterator = infoMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			if (!key.startsWith("requests.format.")) {
				continue;
			}
			log.debug("added format " + key);
			formats.add(key);
		}

		Map formatsMap = new HashMap();
		formatsMap.put("format", formats);

		return formatsMap;
	}

	public WebRequest getWebRequest() {
		if ((context.getExecutionParameters() == null)
				|| (context.getExecutionParameters().size() <= 0)) {
			return null;
		}
		return (WebRequest) context.getExecutionParameters().get(0);
	}

	public Set getProcessingRequests(WebRequest _request) {
		return _request.getProcessingRequests();
	}

	protected void updateRequestStatus(Set _results) {
		WebRequest request = getWebRequest();
		try {
			WebRequestManager manager = (WebRequestManager) ServiceLocator
					.getInstance().getService(
							ServiceConstants.REQUESTMANAGER_SERVICE);
			manager.updateWebRequestStatus(request.getRequestId(),
					WebRequestLifeCycle.REQUEST_LIFECYCLE_INPROCESS);
			// creates the counter EJB component
//			try {
//				WebRequestCounterLocalHome home = WebRequestCounterUtil.getLocalHome();
//				WebRequestCounterVO counter = new WebRequestCounterVO();
//				counter.setRequestId(request.getRequestId());
//				counter.setTotalCount(_results.size());
//				counter.setFinishedCount(0);
//				home.create(counter);
//			} catch (NamingException ne) {
//				throw new PluginRuntimeException("cannot locate counter EJB");
//			} catch (CreateException ce) {
//				throw new PluginRuntimeException("could not create counter EJB");
//			}

		} catch (PersistenceResourceAccessException prae) {
			throw new PluginRuntimeException(
					"could not persist status modification", prae);
		} catch (RequestManagerException rme) {
			throw new PluginRuntimeException(
					"error calling update status method", rme);
		}
	}

	protected Map createBuilderArgs(WebRequest _request, Map _attributes,
			Set _files) {
		Map builderArgs = new HashMap();
		builderArgs.put("files", (File[]) _files.toArray(new File[] {}));
		builderArgs.put("transaction-id", String.valueOf(_request.getRequestId()));
		builderArgs.put("request-params", _attributes);
		Map args = new HashMap();
		args.put((String) parameters.get(CONFIGURATION_BUILDER_NAME), builderArgs);

		return args;
	}

	protected List buildListOfAccounts(Set _results) {
		Iterator iterator = _results.iterator();
		List accountList = new ArrayList();
		while (iterator.hasNext()) {
			Request request = (Request) iterator.next();
			accountList.add(request.getLabel());
		}
		return accountList;
	}
}
