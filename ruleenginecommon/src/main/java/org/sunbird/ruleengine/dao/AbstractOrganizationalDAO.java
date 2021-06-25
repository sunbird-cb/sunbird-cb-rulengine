package org.sunbird.ruleengine.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.common.Auditable;
import org.sunbird.ruleengine.common.SoftDelete;
import org.sunbird.ruleengine.model.AbstractEntity;
import org.sunbird.ruleengine.model.AuditStateChangeHistory;
import org.sunbird.ruleengine.model.AuditUserAction;
public abstract class AbstractOrganizationalDAO<T,U> {

	public abstract EntityManager getEntityManager();

	public abstract Class<T> getClassType();
	

	public T getById(Object id) {
		return getEntityManager().find(getClassType(), id);
	}
	
	public void detach(Object object) {
		 getEntityManager().detach(object);
	}
  
	public void save(T t) {
		getEntityManager().persist(t);
	}

	public T update(T t) {
		//return getEntityManager().merge(t);
		if(t instanceof Auditable)
		{
			String tableName = (t.getClass().getAnnotation(Table.class)).name();
			BigInteger rowId=((AbstractEntity)t).getId();
			auditState((auditAction(((Auditable) t).getLastUpdatedBy(),((Auditable) t).getClientId(),tableName,rowId,"UPDATE")),getFieldsForAudit(t),t);
		}
		if(t instanceof SoftDelete)
		{
			getEntityManager().persist(t);
		}
		else
		{
			getEntityManager().merge(t);
		}
			
		return t;
	}

	public void delete(Object id) {
		getEntityManager().remove(getById(id));
	}
/*
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		Query query = getEntityManager().createQuery("SELECT e FROM " + getClassType().getSimpleName() + " e", getClassType());
		return (List<T>) query.getResultList();
	}*/

	/*@SuppressWarnings("unchecked")
	public List<T> getAll(Long clientId) {
		Query query = getEntityManager().createQuery("SELECT e FROM " + getClassType().getSimpleName() + " e where clientId=" + clientId, getClassType());
		return (List<T>) query.getResultList();
	}
	*/
		
	
	public boolean isUnique(String propertyName,
			Object propertyValue) {
		return getEntityManager()
				.createQuery(
						String.format(
								"select count(c) from %s c where %s = :propertyValue",
								getClassType().getSimpleName(), propertyName),
						Long.class)
				.setParameter("propertyValue", propertyValue) //
				.getSingleResult() == 0;
	}

	
	public boolean isUniqueExceptThis(
			Object id, String propertyName, Object propertyValue) {
			return getEntityManager()
				.createQuery(
						String.format(
								"select count(c) from %s c where %s = :propertyValue and %s != :id", //
								getClassType().getSimpleName(), propertyName, "id"),
						Long.class) //
				.setParameter("propertyValue", propertyValue) //
				.setParameter("id",id) //
				.getSingleResult() == 0;
	}
	
	public boolean isUnique(Object id, String propertyName,
			Object propertyValue) {
		if(id==null)
		{
			return isUnique(propertyName, propertyValue);
		}
		else{
			return isUniqueExceptThis(id, propertyName, propertyValue);
		}
	}
	
	
	public  List<T> getListByColumnNameAndValue(Class<T> clazz, String filterFieldName,
			Object filterFieldValue) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> r = cq.from(clazz);
		if (filterFieldValue == null) {
			cq.where(cb.isNull(r.get(filterFieldName)));
		} else {
			cq.where(cb.equal(r.get(filterFieldName), filterFieldValue));
		}
		cq.orderBy(cb.desc(r.get("id")));
		return getEntityManager().createQuery(cq).getResultList();
	}
	
	public  T getEntityByColumnNameAndValue( String filterFieldName,
			Object filterFieldValue) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClazz());
		Root<T> r = cq.from(getEntityClazz());
		if (filterFieldValue == null) {
			cq.where(cb.isNull(r.get(filterFieldName)));
		} else {
			cq.where(cb.equal(r.get(filterFieldName), filterFieldValue));
		}
		cq.orderBy(cb.desc(r.get("id")));
		return getEntityManager().createQuery(cq).getSingleResult();
	}
	
	public boolean updateByColumn(String table, Long id, String column,
			Object value,String type) {
		
		Query query = getEntityManager().createNativeQuery(
				"UPDATE " + table + " SET " + column + " = ? WHERE id = ?");
		query.setParameter(1, value);
		query.setParameter(2, id);
		int noOfUpdate = query.executeUpdate();
		if (noOfUpdate > 0)
			return true;
		else
			return false;
	}
	
	public Long getCount(U example) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> root=cq.from(getEntityClazz());
		cq.select(cb.count(root));
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate[] array = new Predicate[predicates.size()];
		array = predicates.toArray(array);
		cq.where(cb.and(getSearchPredicates(root, example)));
		return getEntityManager().createQuery(cq).getSingleResult();
	}
	
	 protected  Predicate[] getSearchPredicates(Root<T> root, U example){
		  Predicate[] predicates=new  Predicate[0];
		  return predicates;
		 }
	
	public  Class<T> getEntityClazz(){
		@SuppressWarnings("unchecked")
		Class<T> persistentClass = (Class<T>)
				   ((ParameterizedType)getClass().getGenericSuperclass())
				      .getActualTypeArguments()[0];
					return persistentClass;
		};
	
	public  Class<U> getCriteriaClazz(){
		@SuppressWarnings("unchecked")
		Class<U> persistentClass = (Class<U>)
				   ((ParameterizedType)getClass().getGenericSuperclass())
				      .getActualTypeArguments()[1];
					return persistentClass;
	};
	
	public  Object getListByCriteria(U criteria, String query, String orderBy,  int firstResult,
			int maxResult) {
		
		 query=query+getCriteriaForVo(criteria)+" "+(orderBy==null?"": orderBy);
		Query queryJpa=getEntityManager().createNativeQuery(query);
		setBindParameterForVo(queryJpa, criteria);
		Object list;
		if (firstResult < 0) {

			list = queryJpa.getResultList();
		} else {

			list = queryJpa.setFirstResult(firstResult)
					.setMaxResults(maxResult).getResultList();
		}
		return list;
	}
	
	public  List<T> getListByCriteria(U criteria, String query, String orderBy,  int firstResult,
			int maxResult, AbstractObjectToEntityMapper<List<T>> abstractMapper) {
		 query=query+getCriteriaForVo(criteria)+" "+orderBy;
		Query queryJpa=getEntityManager().createNativeQuery(query);
		setBindParameterForVo(queryJpa, criteria);
		Object list;
		if (firstResult < 0) {

			list = queryJpa.getResultList();
		} else {

			list = queryJpa.setFirstResult(firstResult)
					.setMaxResults(maxResult).getResultList();
		}
		return abstractMapper.map(list);
	}
	
	public  Number getCount(U criteria, String query) {
		 	query=query+getCriteriaForVo(criteria);
			Query queryJpa=getEntityManager().createNativeQuery(query);
			setBindParameterForVo(queryJpa, criteria);
			
			return (Number) queryJpa
				     .getSingleResult();
	}
	
	public String getCriteriaForVo(U criteria){
		return "";
	}
	
	public void setBindParameterForVo(Query queryJpa, U criteria){
	}


	
	public  List<T> getListByCriteria(U criteriaPopulator2, int firstResult,
			int maxResult, Map<String, Boolean> orderBy, boolean distinct,
			Map<String, Set<String>> joinTableWithFieldsToLoad,
			String... fieldsToLoad) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClazz());
		Root<T> r = cq.from(getEntityClazz());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Predicate[] array = new Predicate[predicates.size()];
		array = predicates.toArray(array);
		cq.where(cb.and(getSearchPredicates(r, criteriaPopulator2)));

		List<Selection<?>> selections = new ArrayList<Selection<?>>();
		if (fieldsToLoad != null) {
			for (String fieldToLoad : fieldsToLoad) {
				selections.add(r.get(fieldToLoad));

			}
		}
		if (joinTableWithFieldsToLoad != null) {
			for (String joinTable : joinTableWithFieldsToLoad.keySet()) {
				Join<Object, Object> join = (Join<Object, Object>) r.join(
						joinTable, JoinType.LEFT);

				for (String fieldsToLoad2 : joinTableWithFieldsToLoad
						.get(joinTable)) {

					selections.add(join.get(fieldsToLoad2));
				}
			}
		}
		if (!selections.isEmpty()) {
			cq.multiselect(selections);
		}
		if (orderBy == null) {
			cq.orderBy(cb.desc(r.get("id")));
		} else {
			for (Map.Entry<String, Boolean> order : orderBy.entrySet()) {
				if (order.getValue() == false) {
					cq.orderBy(cb.desc(r.get(order.getKey())));
				} else {
					cq.orderBy(cb.asc(r.get(order.getKey())));
				}

			}
		}
		if (distinct) {
			cq.distinct(true);
		}
		TypedQuery<T> typedQuery = getEntityManager().createQuery(cq);
		if (firstResult >= 0) {
			typedQuery.setMaxResults(maxResult);
			typedQuery.setFirstResult(firstResult);
		}

		return typedQuery.getResultList();
	}

	/**
	 * Checks whether the user for this client is having the permission to performa any 
	 * given task/function.
	 * 
	 * @author chirodip.p
	 * @since 1.1
	 */
	
	public boolean userInRole(Integer userId, Integer clientId, String FUNCTION_NAME)
	{
		
		String query="select count(c.FUNCTION_NAME) from USER_MASTER a, USER_ROLE_JOIN b, ROLE_FUNCTION_JOIN c WHERE a.ID = b.USER_ID AND c.ROLE_ID = b.ROLE_ID AND a.ID :userId AND a.CLIENT_ID :clientId AND c.FUNCTION_NAME :funcName ";
		
		Query queryJpa=getEntityManager().createNativeQuery(query);
		queryJpa.setParameter("userId", userId);
		queryJpa.setParameter("clientId", clientId);
		queryJpa.setParameter("funcName", FUNCTION_NAME);
		int count=((Integer) queryJpa.getSingleResult()).intValue();
		if(count > 0)	   
		return true;
		
		return false;
		
	}
	

	 public BigDecimal getNextSequenceNo(String seqName)
    {
      return (BigDecimal)getEntityManager().createNativeQuery("select "+seqName+".nextval from dual").getSingleResult();
    }
	
	 
	 /**
		 * This method is used for CRUD Audit Purpose. 
		 */
	     private AuditUserAction auditAction(BigInteger userId,BigInteger clientId, String tablename,BigInteger fieldId,String AUDIT_ACTION)
	     {
	  		AuditUserAction auditAction =new AuditUserAction();
	 		auditAction.setTableName(tablename);
	 		auditAction.setAction(AUDIT_ACTION);
	 		auditAction.setRowId(fieldId);
	 		auditAction.setActionBy(userId);
	 		auditAction.setActionDate(new java.sql.Date(System.currentTimeMillis()));
	 		auditAction.setClientId(clientId);
	 		//auditAction.setFieldValue(getFieldsValueForField(t,fieldName));
	 		auditAction.setCreatedBy(userId);
	 		auditAction.setLastUpdatedBy(userId);
	 		auditAction.setCreationDate(new Date());
	 		auditAction.setLastUpdateDate(new Date());
	 		auditAction.setOrgId(new BigInteger("1"));
	 		
	 		getEntityManager().persist(auditAction);
	    	return auditAction;
	     }
	     
	 	/**
	 	 * This method is used for State Audit Purpose. 
	 	 */
	      private boolean auditState(AuditUserAction action,List<String> columnnames,T t)
	      {
	    	    boolean result= false;
	    	  	String queryAuditConfig="SELECT FIELD_NAME FROM CONFIG_AUDIT_STATE_CHANGE "+
	    			  "WHERE TABLE_NAME =:tableName AND FIELD_NAME IN (:fieldNames)";
		  		
		  		Query queryJpa=getEntityManager().createNativeQuery(queryAuditConfig);
		  		queryJpa.setParameter("tableName",action.getTableName());
		  		queryJpa.setParameter("fieldNames",columnnames);
		  		List<String> columnList=queryJpa.getResultList();
		  		
		  		for(String fieldName : columnList)
		    	 {
		  			 AuditStateChangeHistory newAuditField = new AuditStateChangeHistory();
		  			 newAuditField.setAuditActionId(action.getId());
		  			 newAuditField.setFieldName(fieldName);
		  			 newAuditField.setFieldValue(getFieldsValueForField(t,fieldName));
		  			newAuditField.setCreatedBy(((Auditable)t).getLastUpdatedBy());
		  			newAuditField.setLastUpdatedBy(((Auditable)t).getLastUpdatedBy());
		  			newAuditField.setCreationDate(new Date());
		  			newAuditField.setLastUpdateDate(new Date());
		   			 getEntityManager().persist(newAuditField);
		   			 result = true;
		   		 }
		  		
		  		return result;
	      }
	      
	 /**
      * Get ENTITY COLUMN Names
      * @param t : Entity
      * @return  : List of Entity COLUMNS
      */
     private List<String> getFieldsForAudit(T t)
     {
    	 List<String> fieldsForAudit =new ArrayList<String>();
    	 Field[] fields = t.getClass().getDeclaredFields();
    	 for(Field f : fields)
    	 {
    		 Column col = f.getAnnotation(Column.class);
    		 if(col!=null && col.name() != null)
   			 fieldsForAudit.add(col.name());    	 
   		 }
    	 return fieldsForAudit;
     }
     
     /**
      * Get a Column Value
      * @param t : Entity
      * @param fieldName : Database Column Name
      * @return  : List of Entity COLUMNS
      */
     private String getFieldsValueForField(T t,String fieldName)
     {
    	 String retValue=null;
    	 List<String> fieldsForAudit =new ArrayList<String>();
    	 try
    	 {	
	    	 Field[] fields = t.getClass().getDeclaredFields();
	    	 for(int i=0;i<fields.length;i++)
	    	 {
	    		 Field f = fields[i];
	    		 Column col = f.getAnnotation(Column.class);
	    		 if(col!=null && col.name() != null && col.name().equalsIgnoreCase(fieldName))
	    			 retValue = f.get(t).toString();
	   		 }
    	 }
    	 catch(Exception ex)
    	 {
    		 retValue = null;
    	 }
    	 return retValue;
     }	
     
     public Predicate setClientFilter(Root<T> root,BigInteger clientId) {
 			Path<Long> p = root.get("clientId");
 			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
 			return cb.equal(p, clientId);
 		
     }

}

