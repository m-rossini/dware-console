/*
 *
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 16/03/2007
 *
 * @(#)RemakeReportsAction.java 16/03/2007
 */
package br.com.auster.dware.console.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.request.queue.SendMessageQueue;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;

/**
 * The class <code>RemakeReportsAction</code> it is responsible in
 * <p>
 * TODO To improve the commentaries.
 * </p>
 *
 * @author Gilberto Brandão
 * @version $Id$
 * @since JDK1.4
 */
public class RemakeReportsAction extends Action {

	/**
	 * Used to store the values of  <code>FORWARD_REMAKE_SUCCESSFU</code>.
	 * Value <code>list-request-accounts</code>
	 */
	public static final String  FORWARD_REMAKE_SUCCESSFUL = "list-request";

	/**
	 * Used to store the values of  <code>FORWARD_REMAKE_ERROR</code>.
	 * Value <code>error</code>
	 */
	public static final String  FORWARD_REMAKE_ERROR  = "error";


	/**
	 *
	 * It responsible in to remake a reports from a specific web-request.
	 *
	 * @param _mapping
	 * @param _form
	 * @param _request
	 * @param _response
	 * @return
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping _mapping, ActionForm _form,  HttpServletRequest _request, HttpServletResponse _response) {

	   DynaActionForm form = (DynaActionForm) _form;
	   if ((form.get("reqIdReport") == null)) {
	       return _mapping.findForward("error");
	   }
	   // recupera o id da web request do form
	   long requestId = ((Long) form.get("reqIdReport")).longValue();
	   // ActionMessages para mensagens de erro
	   ActionMessages messages = new ActionMessages();

	   String[] auditInfo = {this.getClass().getPackage().getName(), SessionHelper.getUsername(_request), (String) form.get("reqIdReport")} ;
	   try {

	       SendMessageQueue messageQueue = new SendMessageQueue();
	       messageQueue.sendMessageToTopic(requestId);

           // added audit message
           ServiceLocator.getInstance().getAuditService().audit("request.rebuildReport.ok", auditInfo);

           return _mapping.findForward(FORWARD_REMAKE_SUCCESSFUL);

	    } catch (PluginRuntimeException prex) {
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("request.rebuildReport.error", auditInfo, prex);
            // handling exception
	    	throw new PortalRuntimeException(prex);
	    }catch (Exception e) {
            // added audit message
            ServiceLocator.getInstance().getAuditService().audit("request.rebuildReport.error", auditInfo, e);
            // handling exception
	    	throw new PortalRuntimeException(e);
	    }

	}

}
