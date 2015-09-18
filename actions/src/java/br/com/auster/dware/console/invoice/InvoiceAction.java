/*
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
 * Created on 25/04/2007
 *
 * @(#)InvoiceAction.java 25/04/2007
 */
package br.com.auster.dware.console.invoice;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import br.com.auster.billcheckout.consequence.telco.GeographicDimension;
import br.com.auster.billcheckout.thresholds.CustomerType;
import br.com.auster.billcheckout.thresholds.InvoiceThreshold;
import br.com.auster.billcheckout.thresholds.OCCThreshold;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.db.DBAccess;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.persistence.FetchCriteria;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;

/** The class <code>InvoiceAction</code> it is responsible in
 * @author Danilo Oliveira
 * @version $Id$
 * @since JDK1.4
 * @date 25/04/2007
 */
public class InvoiceAction extends DispatchAction {

	private FetchCriteria fetch = new FetchCriteria();

	private static final String FORWARD_DISPLAYLIST = "list";
	private static final String FORWARD_DONE        = "done";
	private static final String FORWARD_DETAIL      = "detail";



	/**
	 * Used to list all Invoice.
	 * @param mapping
	 * @param form
	 * @param _request
	 * @param response
	 * @return
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest _request, HttpServletResponse response) {

		List invoiceThresholdList = null;
		try {

			// defining paging information
			String pageId = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_PAGEID_KEY);
			if (pageId == null) {
				pageId = "1";
			}
			String moveTo = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_MOVETO_KEY);
			if (moveTo == null) {
				moveTo = "0";
			}
			// order field and orientation
			String orderBy = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERBY_KEY);
			if ((orderBy == null) || (orderBy.trim().length() <= 0)) {
				orderBy = "customerType";
			}
			String orderWay = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERWAY_KEY);
			if (orderWay == null) {
				orderWay = RequestScopeConstants.REQUEST_ORDERFORWARD_KEY;
			}

			int displayLength = 20;
			int pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pageId), Integer.parseInt(moveTo));
			int offset = IndexingUtils.getStartingElement(pageNbr, displayLength);
			fetch.clearOrder();
			fetch.setOffset(offset);
			fetch.setSize(displayLength);
			fetch.addOrder(orderBy, orderWay.equals(RequestScopeConstants.REQUEST_ORDERFORWARD_KEY));

			DBAccess db = new DBAccess(InvoiceThreshold.class);
			invoiceThresholdList = db.select(fetch);

			int totalPages = db.count();

			totalPages = IndexingUtils.getNumberOfPages(totalPages, displayLength);
			_request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
			_request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERBY_KEY, (orderBy == null ? "customerType" : orderBy) );
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERWAY_KEY, orderWay);

			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFINVOICE_KEY, invoiceThresholdList);
		} catch (Exception e) {

			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);

            throw new PortalRuntimeException(e);
		}
		return mapping.findForward(FORWARD_DISPLAYLIST);
	}

	/**
	 * Handles displaying the details from a Invoice.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward detail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		InvoiceThreshold invoiceThreshold = null;
		try {
			// Search de Customer Types
			DBAccess db = new DBAccess(CustomerType.class);
			Collection customerTypeList = db.selectAll();
			request.setAttribute(RequestScopeConstants.REQUEST_LISTOFCUSTOMERTYPE_KEY, customerTypeList);

			// Search de Geographic Dimension
			db = new DBAccess(GeographicDimension.class);
			Collection geographicDMList = db.selectAll();
			request.setAttribute(RequestScopeConstants.REQUEST_LISTOFGEOGRAPHICDM_KEY, geographicDMList);
			//
			Object key = WebUtils.findRequestAttribute(request, RequestScopeConstants.REQUEST_INVOICE_ID_KEY);
			// recupera o id da web request do form
			if (key != null && !key.equals("")) { // Update
				long id = Long.parseLong((String) key);
				invoiceThreshold = new InvoiceThreshold(); // Não funciona new CustomerType(id);
				invoiceThreshold.setUid(id);
				//
				db = new DBAccess(InvoiceThreshold.class);
				invoiceThreshold = (InvoiceThreshold) db.selectByPK(invoiceThreshold);
				request.getSession().setAttribute(RequestScopeConstants.REQUEST_INVOICE_ID_KEY, invoiceThreshold);
				request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE);
			} else {
				invoiceThreshold = new InvoiceThreshold();
				request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT);
			}
			request.setAttribute(RequestScopeConstants.REQUEST_INVOICE_ID_KEY, invoiceThreshold);
			if (invoiceThreshold != null) {
				if (invoiceThreshold.getCustomerType() != null) {
					long customerTypeUid = invoiceThreshold.getCustomerType().getUid();
					String ctUid = String.valueOf(customerTypeUid);
					request.setAttribute(RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY, ctUid);
				}
				if (invoiceThreshold.getUF() != null) {
					long stateUid = invoiceThreshold.getUF().getUid();
					String gdUid = String.valueOf(stateUid);
					request.setAttribute(RequestScopeConstants.REQUEST_GEOGRAPHICDM_ID_KEY, gdUid);
				}
			}
		} catch (Exception e) {

			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);

            throw new PortalRuntimeException(e);
		}

		return mapping.findForward(FORWARD_DETAIL);
	}

	/**
	 * Handles when adding a new Invoice.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		InvoiceThreshold invoiceThreshold = null;
		Collection invoiceThresholdList = null;
		try {

			DynaActionForm dyForm = (DynaActionForm) form;
			invoiceThreshold = getInvoiceThreshold(dyForm);
			//
			DBAccess db = new DBAccess(InvoiceThreshold.class);
			// verifica se ja existe algum atributo "todos"
			InvoiceThreshold invoiceTh = null;
			invoiceThresholdList = db.selectAll();


			if(invoiceThreshold.getUF() == null && invoiceThreshold.getCustomerType() == null){
				if(invoiceThresholdList != null){
					for(Iterator itInvoice = invoiceThresholdList.iterator();itInvoice.hasNext();){
						invoiceTh = (InvoiceThreshold) itInvoice.next();
						//
						if(invoiceTh.getCustomerType() == null && invoiceTh.getUF() == null){
							throw new Exception("Um limite para esta configuração já está definido.");
						}
					}
				}
			}

			db.save(invoiceThreshold);
			request.setAttribute(RequestScopeConstants.REQUEST_INVOICE_ID_KEY, invoiceThreshold);
			request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT);

			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.insert.ok", this.getClass().getPackage().getName(), SessionHelper.getUsername(request));

			return mapping.findForward(FORWARD_DONE);
		} catch (Exception e) {
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.insert.error", this.getClass().getPackage().getName(), SessionHelper.getUsername(request), e);
			// exception handling
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);

            throw new PortalRuntimeException(e);
		}
	}

	/**
	 * Executes the update operation of the selected Invoice.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 *
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		InvoiceThreshold invoiceThreshold = null;
		InvoiceThreshold invoiceThreshold2 = null;
		try {
			DynaActionForm dyForm = (DynaActionForm) form;
			invoiceThreshold2 = getInvoiceThreshold(dyForm);

			DBAccess db = new DBAccess(InvoiceThreshold.class);

			invoiceThreshold = (InvoiceThreshold) request.getSession().getAttribute(RequestScopeConstants.REQUEST_INVOICE_ID_KEY);

			invoiceThreshold.setLowerAmount(invoiceThreshold2.getLowerAmount());
			invoiceThreshold.setUpperAmount(invoiceThreshold2.getUpperAmount());
			invoiceThreshold.setHintMessage(invoiceThreshold2.getHintMessage());


			db.update(invoiceThreshold);
			request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE);
			request.setAttribute(RequestScopeConstants.REQUEST_INVOICE_ID_KEY, invoiceThreshold);
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.update.ok", this.getClass().getPackage().getName(), SessionHelper.getUsername(request));
		} catch (Exception e) {
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.update.error", this.getClass().getPackage().getName(), SessionHelper.getUsername(request), e);
			// exception handling
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);

            throw new PortalRuntimeException(e);
		}
		return mapping.findForward(FORWARD_DONE);
	}

	/**
	 * Handles when a invoice has to be removed.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		InvoiceThreshold invoiceThreshold = null;
		try {
			Object key = WebUtils.findRequestAttribute(request, RequestScopeConstants.REQUEST_INVOICE_ID_KEY);
			// recupera o id da web request do form
			long id = Long.parseLong((String) key);
			invoiceThreshold = new InvoiceThreshold(); // Não funciona new CycleDates(id)
			invoiceThreshold.setUid(id);

			DBAccess db = new DBAccess(InvoiceThreshold.class);
			invoiceThreshold = (InvoiceThreshold) db.selectByPK(invoiceThreshold);
			if (invoiceThreshold != null) {
				db.delete(invoiceThreshold);
			}

			request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE);
			request.setAttribute(RequestScopeConstants.REQUEST_INVOICE_ID_KEY, invoiceThreshold);
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.delete.ok", this.getClass().getPackage().getName(), SessionHelper.getUsername(request));

		} catch (Exception e) {
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.delete.error", this.getClass().getPackage().getName(), SessionHelper.getUsername(request), e);
			// exception handling
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);

            throw new PortalRuntimeException(e);
		}
		return mapping.findForward(FORWARD_DONE);
	}

	/**
	 *
	 * Private helper methods.
	 *
	 * @param dyForm
	 * @return
	 */
	private InvoiceThreshold getInvoiceThreshold(DynaActionForm dyForm) {
		InvoiceThreshold invoiceThreshold = new InvoiceThreshold();
		if (dyForm != null) {
			Object uidObj = null;
			Long uidLong = new Long(0);
			try {
				uidObj = dyForm.get("uid");
				uidLong = (Long) uidObj;
				invoiceThreshold.setUid(uidLong.longValue());
			} catch (Exception e) {}

			Long   customerType = (Long)   dyForm.get("customerType");
			Long   uf           = (Long)   dyForm.get("UF");
			//Double upperAmount  = (Double) dyForm.get("upperAmount");
			Double upperAmount = dyForm.get("upperAmount").equals("") ? new Double(OCCThreshold.DISABLED_LIMIT) : new Double((String) dyForm.get("upperAmount"));
			Double lowerAmount = dyForm.get("lowerAmount").equals("") ? new Double(OCCThreshold.DISABLED_LIMIT) : new Double((String) dyForm.get("lowerAmount")) ;
			//Double lowerAmount  = (Double) dyForm.get("lowerAmount");
			String hintMessage  = (String) dyForm.get("hintMessage");

			invoiceThreshold.setUpperAmount(upperAmount.doubleValue());
			invoiceThreshold.setLowerAmount(lowerAmount.doubleValue());
			invoiceThreshold.setHintMessage(hintMessage);

			if (customerType != null && customerType.longValue() != 0) {
				CustomerType ct = new CustomerType(customerType.longValue());
				invoiceThreshold.setCustomerType(ct);
			}
			if (uf != null && uf.longValue() != 0) {
				GeographicDimension gd = new GeographicDimension(uf.longValue());
				invoiceThreshold.setUF(gd);
			}
		}
		return invoiceThreshold;
	}
}