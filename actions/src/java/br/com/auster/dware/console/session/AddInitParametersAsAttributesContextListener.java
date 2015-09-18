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
 * Created on 05/09/2008
 */
package br.com.auster.dware.console.session;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.auster.facelift.services.ServiceLocator;

/**
 * This listener will port all init parameters in servlet context level, into servlet context attributes. Such is
 *    needed since all struts tags handle only servlet context attributes.
 *
 * @author framos
 * @version $Id$
 *
 */
public class AddInitParametersAsAttributesContextListener implements ServletContextListener {

	/**
	 * Here we will remove all initParameters previously added into attributes
	 *
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent _event) {
		ServletContext context = _event.getServletContext();
		Enumeration en = context.getInitParameterNames();
		while (en.hasMoreElements()) {
			String paramName = (String)en.nextElement();
			context.removeAttribute(paramName);
		}
		ServiceLocator.getInstance().getAuditService().audit("application.finished");
	}

	/**
	 * Here we will add all initParameters into attributes
	 *
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent _event) {
		ServletContext context = _event.getServletContext();
		Enumeration en = context.getInitParameterNames();
		while (en.hasMoreElements()) {
			String paramName = (String)en.nextElement();
			context.setAttribute(paramName, context.getInitParameter(paramName));
		}
		ServiceLocator.getInstance().getAuditService().audit("application.started");
	}

}
