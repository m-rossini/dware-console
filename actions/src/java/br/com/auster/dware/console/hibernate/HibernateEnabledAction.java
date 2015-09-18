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
 * Created on 25/10/2007
 */
package br.com.auster.dware.console.hibernate;

import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.struts.actions.DispatchAction;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.persistence.FetchCriteria;
import br.com.auster.persistence.OrderClause;

/**
 * @author framos
 * @version $Id$
 *
 */
public abstract class HibernateEnabledAction extends DispatchAction {


	protected FetchCriteria fetch = new FetchCriteria();





	/**
	 * Opens database connections for the JNDI bounded SessionFactory
	 */
	protected Session openConnection() throws Exception {
		try {
			Context ctx = new InitialContext();
	    	SessionFactory sf = (SessionFactory) ctx.lookup(ServiceConstants.HIBERNATE_SESSION_JNDI);
			return sf.openSession();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}



	protected void configureCriteria(Criteria _criteria, FetchCriteria _fetch) throws Exception {
		configureCriteria(_criteria, _fetch, null);
	}

	protected void configureCriteria(Criteria _criteria, FetchCriteria _fetch, Long _pk) throws Exception {
		// PK filter
		if (_pk != null) {
			log.debug("filtering by pk field with value " + _pk);
			_criteria.add(Restrictions.like("uid", _pk));
		}
		if (_fetch == null) { return; }
		// order by
		for (Iterator it = _fetch.orderIterator(); it.hasNext();) {
			OrderClause orderClause = (OrderClause) it.next();
			log.debug("sorting by field " + orderClause.getFieldName() + " in asc. order? " + orderClause.isAscending());
			if (orderClause.isAscending()) {
				_criteria.addOrder(Order.asc(orderClause.getFieldName()));
			} else {
				_criteria.addOrder(Order.desc(orderClause.getFieldName()));
			}
		}
		// length
		if (_fetch.getSize() > 0) {
			log.debug("result size set to " + _fetch.getSize());
			_criteria.setMaxResults(_fetch.getSize());
		}
		if (_fetch.getOffset() > 0) {
			log.debug("result offset set to " + _fetch.getOffset());
			_criteria.setFirstResult(_fetch.getOffset());
		}
	}
}
