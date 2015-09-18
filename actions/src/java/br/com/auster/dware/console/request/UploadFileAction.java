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
 * Created on Sep 5, 2004
 */
package br.com.auster.dware.console.request;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.services.properties.PropertyHandler;

/** 
 * @author Frederico A Ramos
 * @version $Id: UploadFileAction.java 30 2005-05-24 18:46:06Z framos $
 */
public class UploadFileAction extends Action {

    
    
    
    private static I18n i18n = I18n.getInstance(UploadFileAction.class);
    private static Logger log = Logger.getLogger(UploadFileAction.class);
    

    //########################################
    // instance methods
    //########################################
	
    public ActionForward execute(ActionMapping _mapping, ActionForm _form, 
			 HttpServletRequest _request, HttpServletResponse _response) {
        
        try {
	        UploadForm form = (UploadForm) _form;
	        FormFile file = form.getFilepath();        

            PropertyHandler properties = (PropertyHandler) ServiceLocator.getInstance().getPropertiesService();
            
	        // creating request and saving file do disk
            String uploadDir = properties.getProperty("portal.uploadDir");
            if (uploadDir == null) {
                throw new PortalRuntimeException(i18n.getString("requests.upload.noUploadDir"));
            }
	        File filepath = new File(uploadDir + "/" + file.getFileName());       
            saveFileToDisk(filepath, file.getFileSize(), file.getInputStream());
            
            // request created successfully
            _request.setAttribute(RequestScopeConstants.REQUEST_UPLOADEDFILE_KEY, filepath.toString());
            _request.setAttribute(RequestScopeConstants.REQUEST_UPLOADEDFILESIZE_KEY, String.valueOf(file.getFileSize()));         
            
            // initializes the new request instance
            _request.getSession(false).removeAttribute(SessionScopeConstants.SESSION_NEWREQUEST_KEY);
            return _mapping.findForward("parse-accounts");
        } catch (IOException ioe) {
            throw new PortalRuntimeException(ioe);
        } 
    }
    
    
    private void saveFileToDisk(File _filepath, int _filesize, InputStream _in) throws IOException {
        try {
            if (_filesize == 0) { 
                log.error(i18n.getString("requests.upload.emptyFile"));
                throw new IOException(i18n.getString("requests.upload.emptyFile")); 
            } 
            FileOutputStream out = new FileOutputStream(_filepath);
            int readBytes = 0;
            int totalBytes = 0;
            byte[] bufferIn = new byte[32000];
            while (totalBytes < _filesize) {
                readBytes = _in.read(bufferIn);
                out.write(bufferIn, 0, readBytes);
                totalBytes += readBytes;
            }
            if (out != null) { out.close(); }
            
        } catch (IOException ioe) {
            log.error(i18n.getString("requests.upload.ioException", _filepath), ioe);
        }
    }    

}
