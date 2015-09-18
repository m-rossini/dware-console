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
 * Created on 23/04/2007
 */
package br.com.auster.dware.console.cycleDates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import br.com.auster.billcheckout.model.CycleDates;
import br.com.auster.billcheckout.thresholds.CustomerType;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.db.DBAccess;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;

/**
 * @author framos
 * @version $$Id$$
 */
public class CycleDatesAction extends DispatchAction {


	private static final Logger log = Logger.getLogger(CycleDatesAction.class);

	private static final String FORWARD_DISPLAYLIST = "list";
	private static final String FORWARD_DONE = "done";
	private static final String FORWARD_DETAIL = "detail";




	/**
	 *
	 * Used to list all cycle dates.
	 *
	 * @param mapping
	 * @param form
	 * @param _request
	 * @param response
	 * @return
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest _request, HttpServletResponse response) {

		List cycleDates = null;

		try {
			Map whereClauses = null;
			Map orderByMap = null;

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
				orderBy = "endDate";
			}
			String orderWay = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERWAY_KEY);
			if (orderWay == null) {
				orderWay = RequestScopeConstants.REQUEST_ORDERFORWARD_KEY;
			}

			// Filter
			String filterType = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_FILTERBY_KEY);
			String filterCondition = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_FILTERCONDITION_KEY);
			if (filterType != null) {
				whereClauses = new HashMap();
				whereClauses.put(filterType, filterCondition);
			}
			// Order
			orderByMap = new HashMap();
			orderByMap.put(orderBy, (RequestScopeConstants.REQUEST_ORDERFORWARD_KEY.equals(orderWay) ? Boolean.TRUE : Boolean.FALSE) );

			int displayLength = 25;
			int pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pageId), Integer.parseInt(moveTo));
			int offset = IndexingUtils.getStartingElement(pageNbr, displayLength);
			log.debug("Preparing to load page for cycle dates with offset " + offset + " and len " + displayLength);
			log.debug("Chosen sort is " + (orderBy == null ? "endDate" : orderBy) + " in " + orderWay + " order");
			DBAccess db = new DBAccess(CycleDates.class);
			cycleDates = db.select(whereClauses, orderByMap, displayLength, offset);

			int totalPages = db.count(whereClauses);
			log.debug("Calculated a total of " + totalPages + " pages to be displayed.");

			totalPages = IndexingUtils.getNumberOfPages(totalPages, displayLength);
			_request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
			_request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERBY_KEY, (orderBy == null ? "startDate" : orderBy) );
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERWAY_KEY, orderWay);
			_request.setAttribute(RequestScopeConstants.REQUEST_FILTERBY_KEY, (filterType == null ? "" : filterType) );
			_request.setAttribute(RequestScopeConstants.REQUEST_FILTERCONDITION_KEY, (filterCondition == null? "" : filterCondition) );
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFCYCLEDATES_KEY, cycleDates);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);

            throw new PortalRuntimeException(e);
		}
		return mapping.findForward(FORWARD_DISPLAYLIST);
	}

	/**
	 *
	 * Handles when a cycle date has to be removed.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		CycleDates cycleDates = null;
		ActionMessages messages = new ActionMessages();
		try {
			Object key = WebUtils.findRequestAttribute(request, RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY);
			// recupera o id da web request do form
			long id = Long.parseLong((String) key);
			cycleDates = new CycleDates(); // Não funciona new CycleDates(id)
			cycleDates.setUid(id);

			DBAccess db = new DBAccess(CycleDates.class);
			cycleDates = (CycleDates) db.selectByPK(cycleDates);
			if (cycleDates != null) {
				db.delete(cycleDates);
			}

			request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE);
			request.setAttribute(RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY, cycleDates);

			String cycleCode = cycleDates.getCycleCode();
			String cutDate = cycleDates.getEndDate().toString();
			String dueDate = cycleDates.getDueDate().toString();
			String operation = RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE;

			String[] mensagens = new String[4];
			mensagens[0]= cycleCode;
			mensagens[1]= cutDate;
			mensagens[2]= dueDate;
			mensagens[3]= operation;
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("message.result", mensagens));
			saveMessages(request, messages);

			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.delete.ok", this.getClass().getPackage().getName(), SessionHelper.getUsername(request));

		} catch (Exception e) {
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.delete.error", this.getClass().getPackage().getName(), SessionHelper.getUsername(request), e);
			// exception handling
			messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);

            throw new PortalRuntimeException(e);
		}
		return mapping.findForward(FORWARD_DONE);
	}

	/**
	 *
	 * Handles when adding a new cycle date.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		CycleDates cycleDates = null;
		try {
			DynaActionForm dyForm = (DynaActionForm) form;
			cycleDates = getCycleDate(dyForm, false);
//			Caso a informação de "Data de Início" não seja preenchida, a mesma será gerada em tempo de persistência, utilizando-se a seguinte fórmula:
//			Data-de-Início = (Data-de-Corte – 1 mês) + 1 dia
			if (cycleDates.getStartDate() == null) {
				Calendar calculatedDate = new GregorianCalendar();
				calculatedDate.setTime(cycleDates.getEndDate());
				calculatedDate.add(Calendar.MONTH, -1);
				calculatedDate.add(Calendar.DAY_OF_MONTH, 1);
				cycleDates.setStartDate(calculatedDate.getTime());
			}
			DBAccess db = new DBAccess(CycleDates.class);
			db.save(cycleDates);
			request.setAttribute(RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY, cycleDates);
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
	 *
	 * Handles displaying the details from a cycle date.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward detail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		CycleDates cycleDate = null;

		try {
			// Seache de Customer Types
			DBAccess db = new DBAccess(CustomerType.class);
			Collection customerTypeList = db.selectAll();
			request.setAttribute(RequestScopeConstants.REQUEST_LISTOFCUSTOMERTYPE_KEY, customerTypeList);
			Object key = WebUtils.findRequestAttribute(request, RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY);
			// recupera o id da web request do form
			if (key != null && !key.equals("")) { // Update
				long id = Long.parseLong((String) key);
				cycleDate = new CycleDates(); // Não funciona new CycleDates(id);
				cycleDate.setUid(id);
				db = new DBAccess(CycleDates.class);
				cycleDate = (CycleDates) db.selectByPK(cycleDate);
				request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE);
			} else {
				cycleDate = new CycleDates();
				request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT);
			}
		} catch (Exception e) {
			log.error("Error while detailing cycle date", e);

			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);

            throw new PortalRuntimeException(e);
		}
		request.setAttribute(RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY, cycleDate);
		if (cycleDate != null && cycleDate.getCustomerType() != null) {
			long customerTypeUid = cycleDate.getCustomerType().getUid();
			String ctUid = String.valueOf(customerTypeUid);
			request.setAttribute(RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY, ctUid);
		}
		return mapping.findForward(FORWARD_DETAIL);
	}

	/**
	 * Executes the update operation of the selected cycle date.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		CycleDates cycleDates = null;
		try {
			DynaActionForm dyForm = (DynaActionForm) form;
			cycleDates = getCycleDate(dyForm, true);
			DBAccess db = new DBAccess(CycleDates.class);
			db.update(cycleDates);
			request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE);
			request.setAttribute(RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY, cycleDates);
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
	 * Private helper methods
	 */
	private CycleDates getCycleDate(DynaActionForm dyForm, boolean _uidShouldExist) throws Exception {
		CycleDates cycleDates = new CycleDates();
		if (dyForm != null) {
			if (_uidShouldExist) {
				Long uidLong = (Long) dyForm.get("uid");
				cycleDates.setUid(uidLong.longValue());
			}

			String  cycleCode     = (String)  dyForm.get("cycleCode");
			cycleCode = cycleCode.toUpperCase();
			String  startDate     = (String)  dyForm.get("startDate");
			String  endDate       = (String)  dyForm.get("endDate");
			String  issueDate     = (String)  dyForm.get("issueDate");
			String  referenceDate = (String)  dyForm.get("referenceDate");
			String  dueDate       = (String)  dyForm.get("dueDate");
			Long    customerType  = (Long)    dyForm.get("customerType");
			Integer offsetDays    = (Integer) dyForm.get("offsetDays");

			cycleDates.setCycleCode(cycleCode);
			cycleDates.setStartDate(parseDate(startDate));
			cycleDates.setEndDate(parseDate(endDate));
			cycleDates.setIssueDate(parseDate(issueDate));
			cycleDates.setReferenceDate(parseDate(referenceDate));
			cycleDates.setDueDate(parseDate(dueDate));

			if (cycleDates.getDueDate() == null) {
				if (offsetDays != null) {
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(cycleDates.getEndDate());
					calendar.add(Calendar.DAY_OF_MONTH, offsetDays.intValue());
					cycleDates.setDueDate(calendar.getTime());
				}
			}
			// loading customer type information
			if (customerType != null) {
				DBAccess db = new DBAccess(CustomerType.class);
				List result = db.selectByField("uid", customerType);
				// if <= 0 then no customer type was found for such UID and NULL will be saved
				if (result.size() > 0) {
					cycleDates.setCustomerType((CustomerType)result.iterator().next());
				}
			}
		}
		return cycleDates;
	}

	/**
	 *
	 * Is responsible in parse date.
	 *
	 * @param dateStr
	 * @return Date
	 */
	private Date parseDate(String dateStr) {
		Date result = null;
		try {
			String pattern = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			if (dateStr != null && !dateStr.equals("")) {
				result = sdf.parse(dateStr);
			}
		} catch (Exception e) {
			log.error(e);
			log.debug(e);
		}
		return result;
	}


}