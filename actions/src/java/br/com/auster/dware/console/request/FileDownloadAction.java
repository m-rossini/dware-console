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
 * Created on Oct 26, 2004
 */
package br.com.auster.dware.console.request;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;

/**
 * @author Frederico A Ramos
 * @version $Id: FileDownloadAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class FileDownloadAction extends Action {



    public static final String FORWARD_ERROR_PAGE = "error";


    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {
        ActionMessages messages = new ActionMessages();

        String file = _request.getParameter(RequestScopeConstants.REQUEST_SELECTEDFILE_KEY);
        String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), file} ;
        try {
			if (file != null) {
			    File f = new File(file);
			    if ((f.exists()) && (f.canRead())) {
				    _response.setContentType("application/zip; charset=utf-8");
				    _response.setHeader("Content-Disposition", "attachment;filename=\"" + f.getName() + "\"");
					FileInputStream inS = new FileInputStream(f);
					OutputStream outS = _response.getOutputStream();
					long filesize = f.length();
					long totalBytes = 0;
			        byte[] bufferIn = new byte[32000];
			        // writing input bytes to output file
			        while (totalBytes < filesize) {
			        	int readBytes = inS.read(bufferIn);
			            outS.write(bufferIn, 0, readBytes);
			            totalBytes += readBytes;
			        }
					//outS.flush();
					outS.close();
		            // added audit message
		            ServiceLocator.getInstance().getAuditService().audit("request.download.ok", auditInfo);
			    } else {
		            throw new PortalRuntimeException("cannot find the specified file");
			    }
			}
        } catch (PortalRuntimeException prex) {
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("request.download.error", auditInfo, prex);
            // handling exception
        	throw new PortalRuntimeException(prex);
        } catch (IOException ioe) {
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("request.download.error", auditInfo, ioe);
            // handling exception
        	throw new PortalRuntimeException(ioe);
        }
        return null;
    }
}
