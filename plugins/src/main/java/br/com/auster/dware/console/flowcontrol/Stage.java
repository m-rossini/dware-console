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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mtengelm
 *
 */
public class Stage implements Serializable {

	public static final long PROC_OK = 0;
	public static final long PROC_OK_WITH_WARNINGS = 1000;
	public static final long PROC_NOT_OK = 3000;
	public static final long PROC_UNKNOWN = 4000;	
	
	public static final String ATTR_RETURNCODE="returnCode";
	public static final String ATTR_EXCEPTION="exception";
	public static final String ATTR_TEXTMESSAGE="textMessage";
	public static final String ATTR_ACKNOWLEDGEDATE="acknowledgeDate";
	
	private Exception exception;
	private long returnCode;
	private String textMessage;
	private Calendar acknowledgeDate;
	private Map attributes;
	private String className;
	
	public Stage() {
		exception=null;
		returnCode = PROC_OK;
		attributes = new HashMap();
	}
	
	public boolean hasException() {
		return (exception != null);
	}
	
	public long getReturnCode() {
		return returnCode;
	}
	
	public String getTextMessage() {
		return this.textMessage;
	}

	/**
   * @return the exception
   */
  public Exception getException() {
  	return this.exception;
  }

	/**
   * @param exception the exception to set
   */
  public void setException(Exception exception) {
  	this.exception = exception;
  	this.attributes.put(ATTR_EXCEPTION, exception);
  }

	/**
   * @param returnCode the returnCode to set
   */
  public void setReturnCode(long returnCode) {
  	this.returnCode = returnCode;
  	this.attributes.put(ATTR_RETURNCODE, new Long(returnCode));
  }

	/**
   * @param textMessage the textMessage to set
   */
  public void setTextMessage(String textMessage) {
  	this.textMessage = textMessage;
  	this.attributes.put(ATTR_TEXTMESSAGE, textMessage);
  }
  
  public Map getAttributes() {
  	return this.attributes;
  }

	/**
   * @return the acknowledgeDate
   */
  public Calendar getAcknowledgeDate() {
  	return this.acknowledgeDate;
  }

	/**
   * @param acknowledgeDate the acknowledgeDate to set
   */
  public void setAcknowledgeDate(Calendar acknowledgeDate) {
  	this.acknowledgeDate = acknowledgeDate;
  	this.attributes.put(ATTR_ACKNOWLEDGEDATE, acknowledgeDate);
  }

	/**
   * @return the className
   */
  public String getClassName() {
  	return this.className;
  }

	/**
   * @param className the className to set
   */
  public void setClassName(String className) {
  	this.className = className;
  }
}
