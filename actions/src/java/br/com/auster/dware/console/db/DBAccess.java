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
 * Created on 11/04/2007
 *
 * @(#)DBAccess.java 11/04/2007
 */
package br.com.auster.dware.console.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.om.reference.CustomizableEntity;
import br.com.auster.persistence.FetchCriteria;
import br.com.auster.persistence.OrderClause;

/**
 * The class <code>DBAccess</code> it is responsible in
 * <p>
 * TODO To improve the commentaries.
 * </p>
 *
 * @author Danilo Oliveira
 * @version $Id$
 * @since JDK1.4
 */
public class DBAccess {


	private static final Logger log = Logger.getLogger(DBAccess.class);

	private Criteria criteria = null;
	private Class clazz = null;
	private Session session = null;
	SessionFactory sf = null;

	/** Create a new instance of specified class.
	 * @param clazz Type of Class to access database
	 * @throws Exception
	 */
	public DBAccess(Class clazz) throws Exception {


		try {
			this.clazz = clazz;

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
	}

	/** Open connection on Hibernate
	 */
	private void openSession() throws Exception {
		try {
			Context ctx = new InitialContext();
	    	SessionFactory sf = (SessionFactory) ctx.lookup(ServiceConstants.HIBERNATE_SESSION_JNDI);
			this.session = sf.openSession();
			this.criteria = this.session.createCriteria(this.clazz);

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}

	}

	/** Rebiuld Session
	 * @throws Exception
	 */
	private void rebuildSessionFactory() throws Exception {

		try {
			Context ctx = new InitialContext();
			this.sf = (SessionFactory) ctx.lookup(ServiceConstants.HIBERNATE_SESSION_JNDI);
			// TODO Verificar o problema de perda de sessão
			if (this.sf == null) {
				throw new Exception("Perda de Sessão, favor reiniciar o WebLogic");
			}

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
	}

	/** Select all registers of table without order or offset
	 * @return list with all registers
	 * @throws Exception
	 */
	public List selectAll() throws Exception {

		try {

			List result = null;
			openSession();
			result = this.criteria.list();
			closeSession();
			return result;

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
	}

	/** Select registers with specified params
	 * @param whereClauses Clauses where for select
	 * this map contains: Key = FieldName
	 *                    Value = value of field
	 * @param orderBy Map with odre to output
	 * this Map conteins: key = FieldName
	 *                    Value = boolean for Ascending/Descending order
	 *                    True is Ascending
	 * @param qtd Limit of registers for output
	 * @param offset The first result to retrieve
	 * @return List with selected registers
	 */
	public List select(Map whereClauses, Map orderBy, int qtd, int offset) throws Exception {

		List result = null;

		try {
			openSession();
			// whereClauses
			if (whereClauses != null) {
				Set keySet = whereClauses.keySet();
				for (Iterator iterKeys = keySet.iterator(); iterKeys.hasNext();) {
					String key = (String) iterKeys.next();
					String value = (String) whereClauses.get(key);
					value = "%" + value + "%";
					log.debug("filtering by field " + key + " with value " + value);
					this.criteria.add(Restrictions.like(key, value));
				}
			}
			if (orderBy != null) {
				Set keySet = orderBy.keySet();
				for (Iterator iterOrderBy = keySet.iterator(); iterOrderBy.hasNext();) {
					String propertyName = (String) iterOrderBy.next();
					Boolean ascending = (Boolean) orderBy.get(propertyName);
					log.debug("sorting by field " + propertyName + " in asc. order? " + ascending.toString());
					if (ascending.booleanValue()) {
						this.criteria.addOrder(Order.asc(propertyName));
					} else {
						this.criteria.addOrder(Order.desc(propertyName));
					}
				}
			}
			if (qtd != 0) {
				this.criteria.setMaxResults(qtd);
			}
			if (offset != 0) {
				this.criteria.setFirstResult(offset);
			}
			result = this.criteria.list();
			closeSession();

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
		return result;
	}

	/** Select the unique register based primary Key.
	 * @param fieldName
	 * @param value
	 * @return List with selected registers
	 */
	public List selectByField(String fieldName, Object value) throws Exception {

		List result = null;
		try {
			openSession();
			this.criteria.add(Restrictions.like(fieldName, value));
			result = this.criteria.list();
			closeSession();

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
		return result;
	}

	/** Select the unique register based primary Key of CustomizableEntity.
	 * The FieldName of Primary Key always is "uid".
	 * @param customizableEntity Extends Class of customizableEntity
	 * @return Value of specified "uid"
	 * @throws Exception
	 */
	public Object selectByPK(CustomizableEntity customizableEntity) throws Exception {

		Object ret = null;
		try {
			String fieldNamePK = "uid";
			long idValue = customizableEntity.getUid();
			Long idLongValue = new Long(idValue);
			ret = selectByPK(fieldNamePK, idLongValue);
		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}

		return ret;
	}

	/** Select the unique register based primary Key.
	 * @param fieldNamePK Name of Primary Key Field
	 * @param objectValue Value of specified field
	 * @return register with identificator equals objectValue
	 * @throws Exception
	 */
	public Object selectByPK(String fieldNamePK, Object objectValue) throws Exception {

		Object result = null;
		try {
			openSession();
			this.criteria.add(Restrictions.like(fieldNamePK, objectValue));
			result = this.criteria.uniqueResult();
			closeSession();

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
		return result;
	}

	/** Count total of registers of table
	 * @return Number of registers
	 */
	public int count(Map _whereClauses) throws Exception {

		int resultCount = 0;
		try {
			openSession();
			if (_whereClauses != null) {
				Set keySet = _whereClauses.keySet();
				for (Iterator iterKeys = keySet.iterator(); iterKeys.hasNext();) {
					String key = (String) iterKeys.next();
					String value = (String) _whereClauses.get(key);
					value = "%" + value + "%";
					log.debug("counting by field " + key + " with value " + value);
					this.criteria.add(Restrictions.like(key, value));
				}
			}
			resultCount = this.criteria.list().size();
			closeSession();
		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}

		return resultCount;
	}

	/** Count total of registers of table
	 * @return Number of registers
	 */
	public int count() throws Exception {
		return this.count(null);
	}

	/** Save fields in new register on table.
	 * @param object ValueObject with new value to put in table.
	 * @throws Exception
	 */
	public void save(Object object) throws Exception {

		try {
			openSession();
			this.session.save(object);
			closeSession();
		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
	}

	/** Update fields in current register.
	 * @param object ValueObject with values to put in table.
	 * @throws Exception
	 */
	public void update(Object object) throws Exception {

		try {
			openSession();
			this.session.update(object);
			closeSession();
		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
	}

	/** Delet fields of current register.
	 * @param object ValueObject with values of register to delete.
	 * @throws Exception
	 */
	public void delete(Object object) throws Exception {

		try {
			openSession();
			this.session.delete(object);
			closeSession();

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
	}

	/** This method save or update the fields in register of table.
	 * @param object Register of table
	 * 			Instance of ValueObject
	 * @throws Exception
	 */
	public void saveOrUpdate(Object object) throws Exception {

		try {
			openSession();
			this.session.saveOrUpdate(object);
			closeSession();
		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
	}

	/** Is responsible in close connection on hibernate instance
	 */
	private void closeSession() throws Exception{

		try {
			//this.session.disconnect();
			this.session.beginTransaction().commit();
			this.session.close();

		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
	}

	/**
	 * @param fetch
	 * @deprecated Because the FetchCriteria object don´t have where clauses
	 */
	public List select(FetchCriteria fetch) throws Exception {

		List result = null;
		try {
			int qtd = 0;
			int offset = 0;
			if (fetch != null) {
				HashMap whereClauses = new HashMap();
				if (fetch.getSize() != 0) {
					qtd = fetch.getSize();
				}
				if (fetch.getOffset() != 0) {
					offset = fetch.getOffset();
				}
				HashMap orderBy = null;
				for (Iterator orderIter = fetch.orderIterator(); orderIter.hasNext();) {
					OrderClause orderClause = (OrderClause) orderIter.next();
					if (orderBy == null) {
						orderBy = new HashMap();
					}
					String fieldName = orderClause.getFieldName();
					Boolean ascending = new Boolean(orderClause.isAscending());
					orderBy.put(fieldName, ascending);
				}
				result = select(whereClauses, orderBy, qtd, offset);
			}
		} catch (Exception e) {
			log.error(e);
			log.debug(e);
			throw e;
		}
		return result;
	}
}