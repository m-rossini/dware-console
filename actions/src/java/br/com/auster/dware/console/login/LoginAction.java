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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.dware.console.error.ErrorMessageException;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.session.DuplicateLoginException;
import br.com.auster.dware.console.session.UniqueUserInApplicationSessionListener;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.facelift.users.security.MaxIncorrectLoginAttemptsException;
import br.com.auster.facelift.users.security.PasswordExpiredException;
import br.com.auster.facelift.users.security.PasswordInvalidatedException;
import br.com.auster.security.base.SecurityException;
import br.com.auster.security.base.UserLockedException;
import br.com.auster.security.model.User;

/**
 * @author Frederico A Ramos
 * @version $Id: LoginAction.java 644 2008-09-22 15:16:52Z wsoares $
 */
public class LoginAction extends Action {

	private static final Logger log = Logger.getLogger(LoginAction.class);
	private static final I18n i18n = I18n.getInstance(LoginAction.class);


	private UniqueUserInApplicationSessionListener uniqueValidator = new UniqueUserInApplicationSessionListener();



	public static final String	FORWARD_LOGIN_SUCCESSFUL	= "user-validated";
	public static final String	FORWARD_LOGIN_ERROR	     = "error";

	// ########################################
	// Instance methods
	// ########################################

	public ActionForward execute(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {

		LoginForm form = (LoginForm) _form;
		String userName = form.getLogin();
		String[] auditInfo = {this.getClass().getPackage().getName(), userName} ;
    	try {
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
			if ( manager.authenticate(form.getLogin(), form.getPassword()) ) {

				HttpSession session = _request.getSession();
				User userInfo = manager.loadUserDetails(form.getLogin());
				// user and passwd are ok. Now lets check it user is not duplicated
				uniqueValidator.registerUser(session, userInfo);
				// if we got here, means the user is not duplicated
				session.setAttribute(SessionScopeConstants.SESSION_USERINFO_KEY, userInfo);
				//session.setAttribute(SessionScopeConstants.SESSION_USERSECURITY_KEY, token);
				ServiceLocator.getInstance().getAuditService().audit("login.authentication.ok", auditInfo);
				// marking if user should be warned to change the password
				if (manager.inWarnRange(form.getLogin())) {
					_request.setAttribute(RequestScopeConstants.REQUEST_USER_SELECTED_UPDATEACTION_KEY, "true");
				}
				return _mapping.findForward(FORWARD_LOGIN_SUCCESSFUL);
			} else {
				// user and password do not match
				throw new SecurityException(i18n.getString("userNotAuthenticated"));
			}

		} catch (DuplicateLoginException dle) {
			ServiceLocator.getInstance().getAuditService().audit("login.authentication.error", auditInfo, dle);
			// this is needed to "mark" which type of portal runtime exception we are dealing with
			throw new PortalRuntimeException(dle);
		} catch (PasswordInvalidatedException pie) {
			ServiceLocator.getInstance().getAuditService().audit("login.authentication.error", auditInfo, pie);
			throw new PortalRuntimeException(pie);
		} catch (PasswordExpiredException pee) {
			ServiceLocator.getInstance().getAuditService().audit("login.authentication.error", auditInfo, pee);
			throw new PortalRuntimeException(pee);
		} catch (MaxIncorrectLoginAttemptsException milae) {
			ServiceLocator.getInstance().getAuditService().audit("login.authentication.error", auditInfo, milae);
			// locking user
			try {
				UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
				manager.lockUser(userName, null);
			} catch (Exception e) {
				log.error("", e);
			}
			throw new PortalRuntimeException(milae);
		} catch (UserLockedException ule) {
			ServiceLocator.getInstance().getAuditService().audit("login.authentication.error", auditInfo, ule);
			throw new PortalRuntimeException(new ErrorMessageException(ule.getMessage(), ule));
		} catch (SecurityException se) {
			ServiceLocator.getInstance().getAuditService().audit("login.authentication.error", auditInfo, se);
			// this is needed to "mark" which type of portal runtime exception we are dealing with
			UserNotRegisteredException cause = new UserNotRegisteredException(se);
			throw new PortalRuntimeException(cause);
		}
	}
}
