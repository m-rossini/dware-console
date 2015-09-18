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
 * Created on Apr 11, 2005
 */
package br.com.auster.dware.console.listener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class RequestFinished implements Serializable {

	
	
    

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4256375623940210870L;
	private long requestId;
    private String accountId;
    private int status;
    
    private String message;
    
    private HashMap filenames = new HashMap();

    
    
    public final String getAccountId() {
        return accountId;
    }

    public final void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(String message) {
        this.message = message;
    }

    public final long getRequestId() {
        return requestId;
    }
    
    public final void setRequestId(long requestId) {
        this.requestId = requestId;
    }
    
    public final int getStatus() {
        return status;
    }

    public final void setStatus(int status) {
        this.status = status;
    }

    public final void addFilename(String _filename, String _format) {
        filenames.put(_filename, _format);
    }
    
    public final void removeFilename(String _filename) {
        filenames.remove(_filename);
    }    
    
    public final String getFormat(String _filename) {
        return (String) filenames.get(_filename);
    }
     
    public final Iterator getFilenames() {
        return filenames.keySet().iterator();
    }
}
