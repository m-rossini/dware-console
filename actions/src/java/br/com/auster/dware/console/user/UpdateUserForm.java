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
 * Created on Oct 03, 2004
 */
package br.com.auster.dware.console.user;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @author Frederico A Ramos
 * @version $Id: UpdateUserForm.java 647 2008-10-07 21:33:45Z wsoares $
 */
public class UpdateUserForm extends ActionForm implements Serializable {


    //########################################
    // instance variables
    //########################################


	private String login;
    private String userEmail;
    private String userFullname;
    private String password;
    private String newPassword;
    private String groupName;
    private String custom1;
    private String custom2;
    private String custom3;


    //########################################
    // instance methods
    //########################################


    public final String getLogin() {
        return login;
    }
    public final void setLogin(String _login) {
        this.login = _login;
    }

    public final String getUserEmail() {
        return userEmail;
    }
    public final void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public final String getUserFullname() {
        return userFullname;
    }
    public final void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public final String getPassword() {
        return password;
    }
    public final void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
		return newPassword;
	}
    
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getPermissionGroupName() {
    	return groupName;
    }

    public void setPermissionGroupName(String _groupName) {
    	this.groupName = _groupName;
    }

    public String getCustom1() {
    	return this.custom1;
    }

    public void setCustom1(String _custom1) {
    	this.custom1 = _custom1;
    }

    public String getCustom2() {
    	return this.custom2;
    }

    public void setCustom2(String _custom2) {
    	this.custom2 = _custom2;
    }

    public String getCustom3() {
    	return this.custom3;
    }

    public void setCustom3(String _custom3) {
    	this.custom3 = _custom3;
    }


}
