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
 * Created on Apr 26, 2005
 */
package br.com.auster.invoice.console.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.plugins.ResumeRequestToServer;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.services.plugin.PluginContext;

public class ResumeTest {

    
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage : ResumeTest <requestId> <limit> [<start-date> <as-previous>]"));
            return;
        }
        
        try {
            int reqId = Integer.parseInt(args[0]);
            int limit = Integer.parseInt(args[1]);
            if (args.length > 2) {
                Date dt = formatter.parse(args[2]);
                boolean asPrevious = Boolean.valueOf(args[3]).booleanValue();
            }
            
            ResumeRequestToServer resume = new ResumeRequestToServer();
            
//            WebRequestManagerRemoteHome ejb = (WebRequestManagerRemoteHome) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
//            WebRequestManagerRemote manager = ejb.create();
            WebRequestManager manager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
            WebRequest request = manager.loadWebRequestDetail(reqId);
            

            PluginContext context = new PluginContext();
            List resultList = new ArrayList();
            resultList.add(request);
            resultList.add(request);
            context.setExecutionParameters(resultList);
            
            HashMap map = new HashMap();
            map.put(ResumeRequestToServer.CONFIGURATION_RMI_SERVER_URL, "//localhost:2004/InvoiceServer");
            resume.setConfigurationParameters(map);
            resume.execute(context);
            
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (Exception rme) {
            rme.printStackTrace();
        }
        
    }

}
