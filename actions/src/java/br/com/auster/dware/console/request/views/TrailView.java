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
 * @version $Id: TrailView.java 300 2006-11-20 22:55:22Z framos $
 */
public class TrailView implements Serializable {

    private String message;
    private int status;
    private Date trailDate;
    
    
    public final String getMessage() {
        return message;
    }
    public final void setMessage(String message) {
        this.message = message;
    }
    public final int getStatus() {
        return status;
    }
    public final void setStatus(int status) {
        this.status = status;
    }
    public final Date getTrailDate() {
        return trailDate;
    }
    public final void setTrailDate(Date trailDate) {
        this.trailDate = trailDate;
    }
    
    public final String getStatusBundleKey() {
        return "text.status." + status;
    }    

    public final String getStatusColorKey() {
        return "color" + status;
    }

}
