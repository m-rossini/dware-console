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
 * Created on 22/01/2007
 */
package br.com.auster.dware.console.flowcontrol;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mtengelm
 *
 */
public class FlowControlMessage implements Serializable {

	private LinkedList stages;
	private long currentStage;
	private String transactionId;
	
	public FlowControlMessage(String transactionId) {
		stages = new LinkedList();
		currentStage=0;
		this.transactionId = transactionId;
	}

	public void addStage(Stage stage){
		stages.add(stage);
		currentStage++;
	}
	
	public List getStages() {
		return stages;
	}
	
	public long getCurrentStage() {
		return currentStage;
	}
	
	public String getTransactionId() {
		return this.transactionId;
	}
}
