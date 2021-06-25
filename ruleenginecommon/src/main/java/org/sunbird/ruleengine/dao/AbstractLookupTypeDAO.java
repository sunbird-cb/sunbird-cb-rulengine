package org.sunbird.ruleengine.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.AbstractLookupType;


@Repository
public abstract class AbstractLookupTypeDAO<T extends AbstractLookupType, U extends AbstractLookupType> extends AbstractDAO<T, U> {

	
	public List<T> getListByCriteria(T role, int firstResult,
			int maxResult) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<T> q = cb.createQuery(getEntityClazz());
		Root<T> c = q.from(getEntityClazz());
		List<Predicate> predicates = setCriteriaForRole(role, cb, c);
		q.distinct(true);
		q.select(c);
		if (predicates.size() != 0) {
			Predicate[] pre = new Predicate[predicates.size()];
			Predicate predicate = cb.and(predicates.toArray(pre));
			q.where(predicate);
		}
         if(firstResult<0)
         {
        	 return getEntityManager().createQuery(q).getResultList();
         }
         else{
        		return getEntityManager().createQuery(q).setFirstResult(firstResult)
        				.setMaxResults(maxResult).getResultList();
         }
		
	
	}

	private List<Predicate> setCriteriaForRole(T t, CriteriaBuilder cb,
			Root<T> c) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		
		return predicates;
	}


	public Long getCountByCriteria(T t) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<T> c = q.from(getEntityClazz());
		q.select(cb.count(c));

		List<Predicate> predicates = setCriteriaForRole(t, cb, c);
		if (!predicates.isEmpty()) {
			Predicate[] pre = new Predicate[predicates.size()];
			Predicate predicate = cb.and(predicates.toArray(pre));
			q.where(predicate);
		}
		return getEntityManager().createQuery(q).getSingleResult();
	}
	

	
}
