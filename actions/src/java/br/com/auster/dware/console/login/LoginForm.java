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
 * Created on Sep 4, 2004
 */
package br.com.auster.dware.console.login;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * @author Frederico A Ramos
 * @version $Id: LoginForm.java 321 2007-02-13 16:26:23Z mtengelm $
 */
public class LoginForm extends ActionForm implements Serializable {
		private Logger log = Logger.getLogger(LoginForm.class);
	
    //########################################
    // instance variables
    //########################################

    private String login;
    private String password;

    public static final String ERROR_LOGIN_REQUIRED = "text.login.required";
    public static final String ERROR_PASSWORD_REQUIRED = "text.password.required";
    
    
    //########################################
    // instance methods
    //########################################

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public ActionErrors validate(ActionMapping _mapping, HttpServletRequest _request) {

        ActionErrors errors = new ActionErrors();
        if ((login == null) || (login.length() < 1)) {
        		log.error("User Login has null lenght");
            errors.add("login", new ActionMessage(ERROR_LOGIN_REQUIRED, login));
        }       
        if ((password == null) || (password.length() < 1)) {
        	log.error("User Password has null lenght");
        	errors.add("password", new ActionMessage(ERROR_PASSWORD_REQUIRED));
        }            

        return (errors);
    }
    
}
