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
 * Created on Mar 16, 2005
 */
package br.com.auster.dware.console.request.views;

import java.io.Serializable;
import java.util.Date;

/**
 * @author framos
 * @version $Id: FileView.java 560 2007-07-24 19:22:16Z gbrandao $
 */
public class FileView implements Serializable {

    private String filename;
    private Date generationDate;
    private String message;
    private int consequenceCount;
    
    
    public final String getFilename() {
        return filename;
    }
    public final void setFilename(String filename) {
        this.filename = filename;
    }
    public final Date getGenerationDate() {
        return generationDate;
    }
    public final void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }
    
    public final String getMessage() {
        return message;
    }
    public final void setMessage(String _message) {
        message = _message;
    }
    
    public final String getTruncatedFilepath() {
        if (filename == null) {
            return null;
        }
        int idx = filename.lastIndexOf(System.getProperty("file.separator"));
        if ((idx  < 0) || (idx >= filename.length())) {
            return filename;
        }
        return filename.substring(idx+1);
    }
	/**
	 * Return the value of a attribute<code>consequenceCount</code>.
	 * @return return the value of <code>consequenceCount</code>.
	 */
	public int getConsequenceCount() {
		return consequenceCount;
	}
	/**
	 * Set a value of <code>consequenceCount</code>.
	 * @param consequenceCount
	 */
	public void setConsequenceCount(int consequenceCount) {
		this.consequenceCount = consequenceCount;
	}    
}
