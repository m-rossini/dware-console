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
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.auster.common.io.CompressUtils;
import br.com.auster.common.log.LogFactory;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.persistence.PersistenceResourceAccessException;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.BundleFile;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.services.plugin.FaceliftPlugin;
import br.com.auster.facelift.services.plugin.PluginContext;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;

public class BundleAllFiles implements FaceliftPlugin {


    
    public static final String CONFIGURATION_OUTPUT_DIR = "output-dir";
    public static final String CONFIGURATION_FILENAME_PREFIX = "file-prefix";
    public static final String CONFIGURATION_FILENAME_SUFIX = "file-sufix";
	public static final String CONFIGURATION_BUNDLE_MESSAGE = "message";
    
    
    
    private Logger log = LogFactory.getLogger(BundleAllFiles.class);
    
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
            log.warn("no web request found to have its generated files bundled into a ZIP");
            return;
        }
        try {        
			// get all proc. requests
	        WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);			
			List outputFiles = manager.findGeneratedOutputFiles(request.getRequestId());
			// build list of related files
			Iterator iterator = outputFiles.iterator();
            Set setOfFiles = new HashSet();
            while (iterator.hasNext()) {
				setOfFiles.add( (String)iterator.next() );
            }
			if ((setOfFiles == null) || (setOfFiles.isEmpty())) {
				log.warn("no files found to generate bundle ZIP for request " + request.getRequestId());
				return;
			}
			// generating the bundle file, even if it already exists
			String outputFilename = getOutputFilename(request.getRequestId(), (String) request.getAdditionalInformation().get("cycle.id"));
            File outputFile = CompressUtils.createZIPBundle(setOfFiles, outputFilename);
			// saving the bundle name info.
			BundleFile bundle = new BundleFile();
			bundle.setFileDatetime(new Timestamp(outputFile.lastModified()));
			bundle.setFilename(outputFilename);
			bundle.setMessage( (parameters.get(CONFIGURATION_BUNDLE_MESSAGE) == null ? 
					            "" : 
								(String) parameters.get(CONFIGURATION_BUNDLE_MESSAGE)) 
							 );
			manager.addBundleFile(request.getRequestId(), bundle);
        } catch (RequestManagerException rme) {
            log.error("request manager exception", rme);
			throw new PluginRuntimeException("request manager exception", rme);
        } catch (IOException ioe) {
            log.error("I/O Exception", ioe);
			throw new PluginRuntimeException("I/O Exception", ioe);
        } catch (PersistenceResourceAccessException prae) {
            log.error("Error while acessing persistence layer", prae);
			throw new PluginRuntimeException("Error while acessing persistence layer", prae);
		}
    }
    
    
    private String getOutputFilename(long _reqId, String _cycleId) {
        String output = (String) parameters.get(CONFIGURATION_OUTPUT_DIR);
        if (output == null) {
            output = System.getProperty("user.dir");
        }
		output += System.getProperty("file.separator");
        if (parameters.get(CONFIGURATION_FILENAME_PREFIX) != null) {
            output += (String) parameters.get(CONFIGURATION_FILENAME_PREFIX);
        } else {
			output += "BUNDLE";
        }
        output += "_REQ" + _reqId + "_CYCLE" + _cycleId;
        if (parameters.get(CONFIGURATION_FILENAME_SUFIX) != null) {
            output += (String) parameters.get(CONFIGURATION_FILENAME_SUFIX);
        }
        output += ".zip";
        return output;
    }

	private WebRequest getWebRequest() {
		if ((context.getExecutionParameters() == null) || 
			(context.getExecutionParameters().size() <= 0)) {
			return null;
		}
		return (WebRequest) context.getExecutionParameters().get(0);
	}
}
