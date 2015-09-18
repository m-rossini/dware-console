/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 04/09/2008
 */
package br.com.auster.dware.console.session;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.security.model.User;

/**
 * This class will control that current logged users wont log again.
 *
 * It will handle a shared Map of users vs. sessionIds, which will be added whenever a new session is created, and
 * 	 will be removed as soon as the user logs out.
 *
 * Note that, since we are using the <code>ServletContext</code> instance to share such object, this will not work
 *   for distributed (marked so in web.xml) web applications.
 *
 * @author framos
 * @version $Id$
 *
 */
public class UniqueUserInApplicationSessionListener implements HttpSessionListener {


	private static final Logger log = Logger.getLogger(UniqueUserInApplicationSessionListener.class);
	private static final I18n i18n = I18n.getInstance(UniqueUserInApplicationSessionListener.class);



	public static final String USER_LOGIN_CONTROL_ATTR = "appl.login.control";
	public static final String USER_LOGIN_CONTROL_REVERSED_ATTR = "appl.login.control.rever";
	public static final String USER_LOGIN_TIMECONTROL_ATTR = "appl.login.control.timeout";



	/**
	 * Here we will add the user and sessionId information, to make sure duplicate logins wont access the application
	 *
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent _event) {
		HttpSession session = _event.getSession();
		ServletContext context = session.getServletContext();
		synchronized (context) {
			Map control = (Map) context.getAttribute(USER_LOGIN_CONTROL_ATTR);
			// should only happen on the first login attempt
			if (control == null) {
				log.info("Control and Reverse maps created.");
				control = new HashMap();
				context.setAttribute(USER_LOGIN_CONTROL_ATTR, control);
				context.setAttribute(USER_LOGIN_CONTROL_REVERSED_ATTR, new HashMap());
				context.setAttribute(USER_LOGIN_TIMECONTROL_ATTR, new HashMap());
			}

			// getting user info from session
			User userInfo = (User) session.getAttribute(SessionScopeConstants.SESSION_USERINFO_KEY);
			// userInfo not set??? This is strange
			try {
				this.registerUser(session, userInfo);
			} catch (DuplicateLoginException dle) {
				throw new PortalRuntimeException(dle);
			}
		}
	}

	/**
	 * Here we will remove user from control map
	 *
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent _event) {
		HttpSession session = _event.getSession();
		ServletContext context = session.getServletContext();
		synchronized (context) {
			Map control = (Map) context.getAttribute(USER_LOGIN_CONTROL_ATTR);
			// should never happen here
			if (control == null) {
				log.error("Must have control map already created.");
				return;
			}

			// getting user info from session
			User userInfo = (User) session.getAttribute(SessionScopeConstants.SESSION_USERINFO_KEY);
			// unregister user
			try {
				this.unregisterUser(session, userInfo);
			} catch (DuplicateLoginException dle) {
				throw new PortalRuntimeException(dle);
			}
		}
	}



	// -- business for this controller

	public void registerUser(HttpSession _session, User _userInfo) throws DuplicateLoginException {
		ServletContext context = _session.getServletContext();
		synchronized (context) {
			Map control = (Map) context.getAttribute(USER_LOGIN_CONTROL_ATTR);
			// if control is null, means we didnt configure this listener and do not want to block duplicate logins
			if (control == null) {
				log.debug("Control map already created. Is UniqueListener correctly configured in web.xml file?");
				return;
			}
			Map reversed = (Map) context.getAttribute(USER_LOGIN_CONTROL_REVERSED_ATTR);
			Map timecontrol = (Map) context.getAttribute(USER_LOGIN_TIMECONTROL_ATTR);

			// userInfo not set??? This is strange
			if (_userInfo != null) {
				// checking that user can really log
				if (!control.containsKey(_userInfo.getLogin())) {
					control.put(_userInfo.getLogin(), _session.getId());
					reversed.put(_session.getId(), _userInfo.getLogin());
					timecontrol.put(_session.getId(), Calendar.getInstance().getTime());
					log.debug("User " + _userInfo.getLogin() + "  registered under session " + _session.getId());
				} else {
					String sid = (String) control.get(_userInfo.getLogin());
					// check if new session must override the previous one, by timeout
					Date dt = (Date) timecontrol.get( sid );
					// should never happen
					if (dt == null) {
						throw new PortalRuntimeException("Session id " +  sid + " for user " + _userInfo.getLogin() + " does not exist.");
					}
					Date now = Calendar.getInstance().getTime();
					if ((now.getTime()-dt.getTime()) > UniqueUserControllerFilter.TIMELIMIT_ATTR) {
						control.put(_userInfo.getLogin(), _session.getId());
						reversed.remove(sid);
						reversed.put(_session.getId(), _userInfo.getLogin());
						timecontrol.remove(sid);
						timecontrol.put(_session.getId(), Calendar.getInstance().getTime());
						log.debug("User " + _userInfo.getLogin() + "  registered under session " + _session.getId());
					} else {
						// user is already logged					
						log.warn("User " + _userInfo.getLogin() + "  already logged under session " + _session.getId());
						throw new DuplicateLoginException(i18n.getString("duplicateLogin"));
					}
				}
			} else {
				// TODO log
			}
		}
	}

	public void unregisterUser(HttpSession _session, User _userInfo) throws DuplicateLoginException {
		ServletContext context = _session.getServletContext();
		synchronized (context) {
			Map control = (Map) context.getAttribute(USER_LOGIN_CONTROL_ATTR);
			// if control is null, means we didnt configure this listener and do not want to block duplicate logins
			if (control == null) {
				log.debug("Control map already created. Is UniqueListener correctly configured in web.xml file?");
				return;
			}
			Map reversed = (Map) context.getAttribute(USER_LOGIN_CONTROL_REVERSED_ATTR);
			Map timecontrol = (Map) context.getAttribute(USER_LOGIN_TIMECONTROL_ATTR);

			String userId = (String) reversed.remove(_session.getId());
			// userId is null???? This is strange
			if (userId != null) {
				control.remove(userId);
				timecontrol.remove(_session.getId());
				log.debug("User disconnected: " + _userInfo.getLogin());
			} else {
				log.warn("User not registered: " + _userInfo.getLogin());
			}
		}
	}
}
