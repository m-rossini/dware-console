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

import java.io.Serializable;
import java.util.Date;

/**
 * @author framos
 * @version $Id: RequestBaseView.java 300 2006-11-20 22:55:22Z framos $
 */
public class RequestBaseView implements Serializable {

    private long id;
    private String label;
    private int status;
    private Date startDate;
    private Date endDate;
    
    
    
    
    public final long getId() {
        return id;
    }
    public final void setId(long id) {
        this.id = id;
    }
    public final String getLabel() {
        return label;
    }
    public final void setLabel(String label) {
        this.label = label;
    }
    public final int getLatestStatus() {
        return status;
    }
    public final void setLatestStatus(int _status) {
        status = _status;
    }
    public final Date getStartDate() {
        return startDate;
    }
    public final void setStartDate(Date _date) {
        startDate = _date;
    }
    public final Date getFinishDate() {
        return endDate;
    }
    public final void setFinishDate(Date _date) {
        endDate = _date;
    }

    
    public final String getStatusBundleKey() {
        return "text.status." + status;
    }

    public final String getStatusColorKey() {
        return "color" + status;
    }

}
