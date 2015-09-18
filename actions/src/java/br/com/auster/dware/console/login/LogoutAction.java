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
 * Created on Sep 19, 2004
 */
package br.com.auster.dware.console.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.security.model.User;


/**
 * @author Frederico A Ramos
 * @version $Id: LogoutAction.java 640 2008-09-18 13:44:50Z framos $
 */
public class LogoutAction extends Action {


	private static final Logger log = Logger.getLogger(LogoutAction.class);


    public static final String FORWARD_LOGOUT = "login";


    //########################################
    // instance methods
    //########################################

    public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

    	String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), "n/a"} ;
    	try {
			HttpSession session = _request.getSession(false);
			if (session != null) {
			    if (session.getAttribute(SessionScopeConstants.SESSION_USERINFO_KEY) != null) {
				    auditInfo[2] = String.valueOf(session.getId());
				    ServiceLocator.getInstance().getAuditService().audit("login.session.finished", auditInfo);
			    }
			    session.removeAttribute(SessionScopeConstants.SESSION_USERINFO_KEY);
			    // NOTE: since we are invalidating the session here, we do not need to call UniqueLogin listener
			    //         unregister method
			    session.invalidate();
			}
		} catch (Exception e) {
			log.error("Exception while logging off", e);
		}
        return _mapping.findForward(FORWARD_LOGOUT);
    }
}
