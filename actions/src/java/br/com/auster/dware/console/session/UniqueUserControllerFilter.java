/*
 * Copyright (c) 2004-2009 Auster Solutions. All Rights Reserved.
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
 * Created on 06/11/2009
 */
package br.com.auster.dware.console.session;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class UniqueUserControllerFilter implements Filter {

	
	private static final I18n i18n = I18n.getInstance(UniqueUserControllerFilter.class);
	
	
	public static final long TIMELIMIT_ATTR = 30000;
	
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain _chain) throws IOException, ServletException {
		if (((HttpServletRequest) _request).getRequestURI().indexOf("login") < 0) {
			HttpSession session = ((HttpServletRequest) _request).getSession(false);
			if (session != null) {
				ServletContext context = session.getServletContext();
				HashMap map = (HashMap) context.getAttribute(UniqueUserInApplicationSessionListener.USER_LOGIN_TIMECONTROL_ATTR);
				// getting user info from session
				if (!map.containsKey(session.getId())) {
					session.removeAttribute(SessionScopeConstants.SESSION_USERINFO_KEY);
					session.invalidate();
//					DuplicateLoginException dle = new DuplicateLoginException(i18n.getString("duplicateLogin"));
//					throw new PortalRuntimeException(dle);
				} else {
					map.put(session.getId(), Calendar.getInstance().getTime());
				}
			}
		}
		_chain.doFilter(_request, _response);
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig _config) throws ServletException {
	}

}
