/*
 * Created on Jan 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.auster.dware.console.error;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.SessionScopeConstants;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;
import br.com.auster.security.model.User;



/**
 * @author framos
 * @version $$Id: GeneralExceptionHandler.java 640 2008-09-18 13:44:50Z framos $$
 */
public class GeneralExceptionHandler extends ExceptionHandler {

	private static final Logger log = Logger.getLogger(GeneralExceptionHandler.class);

    /**
     * <p>Handle the <code>Exception</code>.
     * Return the <code>ActionForward</code> instance (if any) returned by
     * the called <code>ExceptionHandler</code>.
     *
     * @param ex The exception to handle
     * @param ae The ExceptionConfig corresponding to the exception
     * @param mapping The ActionMapping we are processing
     * @param formInstance The ActionForm we are processing
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception ServletException if a servlet exception occurs
     *
     * @since Struts 1.1
     */
    public ActionForward execute(
        Exception ex,
        ExceptionConfig ae,
        ActionMapping mapping,
        ActionForm formInstance,
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException {

        ActionForward forward = super.execute(ex, ae, mapping, formInstance, request, response);
        // logging exception
        log.error("Exception handled:", ex);

        HttpSession session = request.getSession(false);

        ActionMessages messages = new ActionMessages();
        boolean setMessage = false;
        // if we got an exception
		if (ex != null) {
			setMessage = true;
            if (ex.getCause() != null) {
				Throwable cause = ex.getCause();
				// if its a plugin exception, the real exception should be as initCause()
				if ((cause instanceof PluginRuntimeException) && (cause.getCause() != null)) {
		            messages.add(Globals.MESSAGE_KEY, new ActionMessage(cause.getCause().getClass().getName()));
				} else if ((cause instanceof ErrorMessageException) && (cause.getCause() != null)) {
					request.setAttribute(ExceptionConstants.EXCEPTION_MESSAGE, cause.getMessage());
				} else {
		            messages.add(Globals.MESSAGE_KEY, new ActionMessage(cause.getClass().getName()));
				}
            } else {
            	messages.add(Globals.MESSAGE_KEY, new ActionMessage(ex.getClass().getName()));
            }
            saveMessages(request, messages);
		}

		 // despite exception exists, if no info in session, session does not exists or exception is null
		if ((session == null) || (session.isNew())) {
            request.setAttribute(RequestScopeConstants.REQUEST_NOTLOGGED_KEY, "true");
            //messages.add(Globals.MESSAGE_KEY, new ActionMessage(ExceptionConstants.USERNOTLOGGED_MESSAGE_KEY));
            //saveMessages(request, messages);

        // if session exists but no user is stored in it
        } else if ((session.getAttribute(SessionScopeConstants.SESSION_USERINFO_KEY) == null) ||
			    (!(session.getAttribute(SessionScopeConstants.SESSION_USERINFO_KEY) instanceof User))) {

                request.setAttribute(RequestScopeConstants.REQUEST_NOTLOGGED_KEY, "true");

                if (!setMessage) {
                	messages.add(Globals.MESSAGE_KEY, new ActionMessage(ExceptionConstants.USERNOTLOGGED_MESSAGE_KEY));
                	saveMessages(request, messages);
                }

        // to any other kind of error
        } else {
        	if (!setMessage) {
        		request.setAttribute(ExceptionConstants.EXCEPTION_MESSAGE, "generic.exception");
        		saveMessages(request, messages);
        	}
        }

        return forward;
    }

    protected void saveMessages(HttpServletRequest _request, ActionMessages _messages) {
    	_request.setAttribute(Globals.MESSAGE_KEY, _messages);
    }

}
