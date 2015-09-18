/*
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 09/04/2007
 */
package br.com.auster.dware.console.thresholds.views;

import java.io.Serializable;

import br.com.auster.billcheckout.thresholds.NFThreshold;

/**
 * @author framos
 * @version $Id$
 *
 */
public class NFThresholdView implements Serializable {

	
	protected double localLowerLimit, localUpperLimit;
	protected double ldLowerLimit, ldUpperLimit;
	protected String localMessage, ldMessage;
	protected boolean hasLocal, hasLd;
	protected boolean localLowerSet, localUpperSet;
	protected boolean ldLowerSet, ldUpperSet;
	
	public NFThresholdView() {
		this(null, null);
	}
	
	public NFThresholdView(NFThreshold _local, NFThreshold _ld) {
		this.hasLocal = false;
		this.hasLd = false;
		if (_local != null) {
			this.setLocalLowerLimit(_local.getLowerAmount());
			this.setLocalLowerLimitSet(_local.isLowerAmountSet());
			this.setLocalUpperLimit(_local.getUpperAmount());
			this.setLocalUpperLimitSet(_local.isUpperAmountSet());
			this.setLocalMessage(_local.getHintMessage());
		}
		if (_ld != null) {
			this.setLDLowerLimit(_ld.getLowerAmount());
			this.setLDLowerLimitSet(_ld.isLowerAmountSet());
			this.setLDUpperLimit(_ld.getUpperAmount());
			this.setLDUpperLimitSet(_ld.isUpperAmountSet());
			this.setLDMessage(_ld.getHintMessage());
		}
	}
	
	
	public final void setLocalLowerLimit(double _limit) {
		this.localLowerLimit = _limit;
		this.hasLocal = true;
	}
	public final double getLocalLowerLimit() {
		return this.localLowerLimit;
	}
	
	public final void setLocalUpperLimit(double _limit) {
		this.localUpperLimit = _limit;
		this.hasLocal = true;
	}
	public final double getLocalUpperLimit() {
		return this.localUpperLimit;
	}

	public final void setLDLowerLimit(double _limit) {
		this.ldLowerLimit = _limit;
		this.hasLd = true;
	}
	public final double getLDLowerLimit() {
		return this.ldLowerLimit;
	}
	
	public final void setLDUpperLimit(double _limit) {
		this.ldUpperLimit = _limit;
		this.hasLd = true;
	}
	public final double getLDUpperLimit() {
		return this.ldUpperLimit;
	}	
	
	public final void setLocalMessage(String _message) {
		this.localMessage = _message;
		this.hasLocal = true;
	}
	public final String getLocalMessage() {
		return this.localMessage;
	}
	
	public final void setLDMessage(String _message) {
		this.ldMessage = _message;
		this.hasLd = true;
	}
	public final String getLDMessage() {
		return this.ldMessage;
	}
	
	public final boolean isLocalLowerLimitSet() {
		return this.localLowerSet;
	}
	public final void setLocalLowerLimitSet(boolean _flag) {
		this.localLowerSet = _flag;
	}
	
	public final boolean isLocalUpperLimitSet() {
		return this.localUpperSet;
	}
	public final void setLocalUpperLimitSet(boolean _flag) {
		this.localUpperSet = _flag;
	}
	
	public final boolean isLDLowerLimitSet() {
		return this.ldLowerSet;
	}
	public final void setLDLowerLimitSet(boolean _flag) {
		this.ldLowerSet = _flag;
	}
	
	public final boolean isLDUpperLimitSet() {
		return this.ldUpperSet;
	}
	public final void setLDUpperLimitSet(boolean _flag) {
		this.ldUpperSet = _flag;
	}
	
	public final boolean isLocalSet() {
		return this.hasLocal;
	}
	
	public final boolean isLDSet() {
		return this.hasLd;
	}
}
