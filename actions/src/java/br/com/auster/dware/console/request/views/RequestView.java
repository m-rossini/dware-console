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

import java.util.ArrayList;
import java.util.List;


/**
 * @author framos
 * @version $Id: RequestView.java 335 2007-02-26 22:22:57Z gbrandao $
 */
public class RequestView extends RequestBaseView {

    
    
    /** 
     * Used to store the values of  <code>owner</code>.
     */
    private String owner;
    
    /** 
     * Used to store the values of  <code>cycleId</code>.
     */
    private String cycleId;
    
    /** 
     * Used to store the values of  <code>totalCount</code>.
     */
    private int totalCount;
    
    /** 
     * Used to store the values of  <code>finishedCount</code>.
     */
    private int finishedCount;
    
    /** 
     * Used to store the values of  <code>emails</code>.
     */
    private List emails = new ArrayList();
    
    /** 
     * Used to store the values of  <code>bundleList</code>.
     */
    private List bundleList  = new ArrayList();
    
    /** 
     * Used to store the values of  <code>consequenceCounters</code>.
     */
    private List consequenceCounters = new ArrayList();
    
    
    
    public final String getOwner() {
        return owner;
    }
    public final void setOwner(String _owner) {
        this.owner = _owner;
    }

    public final String getCycleId() {
        return cycleId;
    }
    public final void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public final int getFinishedCount() {
        return Math.min(this.finishedCount, this.totalCount);
    }
    public final void setFinishedCount(int finishedCount) {
        this.finishedCount = finishedCount;
    }
    
    public final int getTotalCount() {
        return totalCount;
    }
    public final void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public final List getEmailList() {
        return emails;
    }
    
    public final void setEmailList(List _emails) {
        emails = _emails;
    }
    
    public final List getBundleList() {
        return bundleList;
    }
    
    public final void setBundleList(List _bundleList) {
        bundleList = _bundleList;
    }
    /**
     * Return the value of a attribute<code>consequenceCounters</code>.
     * @return return the value of <code>consequenceCounters</code>.
     */
    public List getConsequenceCounters() {
        return consequenceCounters;
    }
    /**
     * Set a value of <code>consequenceCounters</code>.
     * @param consequenceCounters
     */
    public void setConsequenceCounters(List consequenceCounters) {
        this.consequenceCounters = consequenceCounters;
    }    
}
