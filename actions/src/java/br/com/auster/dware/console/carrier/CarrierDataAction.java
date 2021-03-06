/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package br.com.auster.dware.console.carrier;

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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.auster.billcheckout.consequence.telco.CarrierDimension;
import br.com.auster.billcheckout.model.CarrierData;
import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.commons.SessionHelper;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.facelift.services.ServiceLocator;

/**
 * Creation date: 03-23-2007
 */
public class CarrierDataAction extends HibernateEnabledAction {
	/** Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		CarrierDimension carrierDimension = null;
		CarrierData carrierData = null;
		CarrierData carrierDataLD = null;
		Session session = null;
		try {
			session = this.openConnection();
			// validating form
			DynaActionForm carrierDataForm = (DynaActionForm) form;
			carrierDimension = getCarrier(carrierDataForm);
			carrierData = getCarrierData(carrierDataForm);
			carrierDataLD = getCarrierLDData(carrierDataForm);
			// loading carrier data if exists
			Criteria criteria = session.createCriteria(CarrierData.class);
			criteria.add(Restrictions.like("carrierDimension", carrierDimension));
			List carriersDataList = criteria.list();
			// if carrier data found, then update with UID
			if ((carriersDataList != null) && (carriersDataList.size() > 0)) {
				for (Iterator it = carriersDataList.iterator(); it.hasNext(); ) {
					CarrierData tmp = (CarrierData) it.next();
					if (tmp.isLocal()) {
						carrierData.setUid( tmp.getUid() );
					} else {
						carrierDataLD.setUid( tmp.getUid() );
					}
				}
			}

			session.flush();
			session.close();
			session = this.openConnection();
			session.saveOrUpdate(carrierData);

			session.flush();
			session.close();
			session = this.openConnection();
			session.saveOrUpdate(carrierDataLD);

			request.setAttribute(RequestScopeConstants.REQUEST_CARRIER_ID_KEY, carrierData.getCarrierDimension());
			request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE);
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.update.ok", this.getClass().getPackage().getName(), SessionHelper.getUsername(request));
			return mapping.findForward("done");
		} catch (Exception e) {
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.update.error", this.getClass().getPackage().getName(), SessionHelper.getUsername(request), e);
			// exception handling
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}

	/** Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		CarrierDimension carrierDimension = null;
		CarrierData carrierData = null;
		CarrierData carrierDataLD = null;
		Session session = null;
		try {
			session = this.openConnection();

			DynaActionForm carrierDataForm = (DynaActionForm) form;
			carrierDimension = getCarrier(carrierDataForm);
			carrierData = getCarrierData(carrierDataForm, carrierDimension);
			carrierDataLD = getCarrierLDData(carrierDataForm, carrierDimension);

			session.save(carrierData);
			session.save(carrierDataLD);
			request.setAttribute(RequestScopeConstants.REQUEST_CARRIER_ID_KEY, carrierData.getCarrierDimension());
			request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT);
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.insert.ok", this.getClass().getPackage().getName(), SessionHelper.getUsername(request));
			return mapping.findForward("done");
		} catch (Exception e) {
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.insert.error", this.getClass().getPackage().getName(), SessionHelper.getUsername(request), e);
			// exception handling
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}

	/** Method delete
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		CarrierData carrierData = null;
		Session session = null;
		try {
			session = this.openConnection();

			DynaActionForm carrierDataForm = (DynaActionForm) form;
			carrierData = getCarrierData(carrierDataForm);
			session.delete(carrierData);
			request.setAttribute(RequestScopeConstants.REQUEST_CARRIER_ID_KEY, carrierData.getCarrierDimension());
			request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE);
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.delete.ok", this.getClass().getPackage().getName(), SessionHelper.getUsername(request));
			return mapping.findForward("done");
		} catch (Exception e) {
			// auditing
			ServiceLocator.getInstance().getAuditService().audit("config.delete.error", this.getClass().getPackage().getName(), SessionHelper.getUsername(request), e);
			// exception handling
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}

	/** Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("detail");
	}

	/** Method unspecified
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("cancel");
	}

	private CarrierData getCarrierData(DynaActionForm carrierDataForm) throws Exception {
		return getCarrierData(carrierDataForm, null);
	}

	private CarrierData getCarrierData(DynaActionForm carrierDataForm, CarrierDimension carrierDimension) throws Exception {
		CarrierData carrierData = null;
		if (carrierDataForm != null) {
			carrierData = new CarrierData();
			carrierData.setUid(((Long)carrierDataForm.get("uid")).longValue());
			carrierData.setAddressCity(carrierDataForm.getString("addressCity"));
			carrierData.setAddressComplement(carrierDataForm.getString("addressComplement"));
			carrierData.setAddressEmail(carrierDataForm.getString("addressEmail"));
			carrierData.setAddressNumber(carrierDataForm.getString("addressNumber"));
			carrierData.setAddressStreet(carrierDataForm.getString("addressStreet"));
			carrierData.setAddressWeb(carrierDataForm.getString("addressWeb"));
			carrierData.setAddressZip(carrierDataForm.getString("addressZip"));
			carrierData.setFullName(carrierDataForm.getString("fullName"));
			carrierData.setStateEnrollNumber(carrierDataForm.getString("stateEnrollNumber"));
			carrierData.setTaxId(carrierDataForm.getString("taxId"));
			carrierData.setCityEnrollNumber(carrierDataForm.getString("cityEnrollNumber"));
			carrierData.setLocal(true);
			if (carrierDimension == null) {
				Long carrierUidLong = (Long)carrierDataForm.get("carrierUid");
				carrierDimension = getCarrier(carrierUidLong);
			}
			carrierData.setCarrierDimension(carrierDimension);
		}
		return carrierData;
	}

	private CarrierData getCarrierLDData(DynaActionForm carrierDataForm) throws Exception {
		return getCarrierLDData(carrierDataForm, null);
	}

	private CarrierData getCarrierLDData(DynaActionForm carrierDataForm, CarrierDimension carrierDimension) throws Exception {
		CarrierData carrierData = null;
		if (carrierDataForm != null) {
			carrierData = new CarrierData();
			carrierData.setUid(((Long)carrierDataForm.get("uid")).longValue());
			carrierData.setAddressCity(carrierDataForm.getString("ldAddressCity"));
			carrierData.setAddressComplement(carrierDataForm.getString("ldAddressComplement"));
			carrierData.setAddressEmail(carrierDataForm.getString("ldAddressEmail"));
			carrierData.setAddressNumber(carrierDataForm.getString("ldAddressNumber"));
			carrierData.setAddressStreet(carrierDataForm.getString("ldAddressStreet"));
			carrierData.setAddressWeb(carrierDataForm.getString("ldAddressWeb"));
			carrierData.setAddressZip(carrierDataForm.getString("ldAddressZip"));
			carrierData.setFullName(carrierDataForm.getString("ldFullName"));
			carrierData.setStateEnrollNumber(carrierDataForm.getString("ldStateEnrollNumber"));
			carrierData.setTaxId(carrierDataForm.getString("ldTaxId"));
			carrierData.setCityEnrollNumber(carrierDataForm.getString("ldCityEnrollNumber"));
			carrierData.setLocal(false);
			if (carrierDimension == null) {
				Long carrierUidLong = (Long)carrierDataForm.get("carrierUid");
				carrierDimension = getCarrier(carrierUidLong);
			}
			carrierData.setCarrierDimension(carrierDimension);
		}
		return carrierData;
	}

	private CarrierDimension getCarrier(Long carrierDimensionUid) throws Exception {
		CarrierDimension carrierDimension = null;
		Session session = null;
		try {
			if (carrierDimensionUid != null) {
				session = this.openConnection();
				Criteria criteria = session.createCriteria(CarrierDimension.class);
				configureCriteria(criteria, null, carrierDimensionUid);
				carrierDimension = (CarrierDimension) criteria.uniqueResult();
			}
			return carrierDimension;
		} catch(Exception e) {
			throw e;
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}

	private CarrierDimension getCarrier(DynaActionForm carrierDataForm) throws Exception {
		CarrierDimension carrierDimension = null;
		Session session = null;
		try {
			if (carrierDataForm != null) {
				session = this.openConnection();
				Object uidObj = null;
				Long uidLong = new Long(0);
				try {
					uidObj = carrierDataForm.get("carrierUid");
					uidLong = (Long) uidObj;
				} catch (Exception e) {
					throw new PortalRuntimeException(e);
				}
				if (uidObj == null || uidLong.intValue() == 0) {
	 				String carrierCode    = (String) carrierDataForm.get("carrierCode");
					String carrierState   = (String) carrierDataForm.get("carrierState");
					String carrierCompany = (String) carrierDataForm.get("carrierCompany");
					carrierDimension = new CarrierDimension();
					carrierDimension.setCarrierCode(carrierCode);
					carrierDimension.setCarrierState(carrierState.toUpperCase());
					carrierDimension.setCarrierCompany(carrierCompany);

					session.save(carrierDimension);
				} else {
					Criteria criteria = session.createCriteria(CarrierDimension.class);
					configureCriteria(criteria, null, uidLong);
					carrierDimension = (CarrierDimension) criteria.uniqueResult();

					String carrierCompany = (String) carrierDataForm.get("carrierCompany");
					carrierDimension.setCarrierCompany(carrierCompany);

					session.update(carrierDimension);
				}
			}
			return carrierDimension;
		} catch(Exception e) {
			throw e;
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
}