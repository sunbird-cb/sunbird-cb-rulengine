package org.sunbird.ruleengine.dao;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sunbird.ruleengine.model.ApprovalHierarcy;

public abstract class ApprovalHierarcyDAO<T extends ApprovalHierarcy, U extends ApprovalHierarcy> extends AbstractDAO<T, U> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	@Override
	public abstract Class<T> getClassType() ;
	
	public List<T> getListByCriteria(T approvalhierarcy, int firstResult, int maxResult){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		 
		  CriteriaQuery<T> q = cb.createQuery(getClassType());
		  Root<T> c = q.from(getClassType());
		  List<Predicate> predicates= setCriteria(approvalhierarcy, cb, c);
		  q.select(c);
		  if(predicates.size()!=0)
		  {
			Predicate[] pre=new Predicate[predicates.size()];
			Predicate predicate=cb.and(predicates.toArray(pre));
			q.where(predicate);
		  }
		  if(firstResult<0)
		  {
			  return entityManager.createQuery(q).getResultList();
		  }
		  else{
		  return entityManager.createQuery(q).setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		  }
	}
	private List<Predicate> setCriteria(T approvalhierarcy, CriteriaBuilder cb,
			Root<T> c) {
		List<Predicate> predicates = new ArrayList<Predicate>();
	/*	if (bank.getNameSearch() != null && !bank.getNameSearch().isEmpty()) {
			Expression<String> expression = c.get("name");
			Predicate rolePredicate = cb.equal(expression, bank.getNameSearch()
					.trim());
			predicates.add(rolePredicate);
		}
		if (bank.getOrderId() != null ) {
			Expression<Long> expression = c.get("orderId");
			Predicate rolePredicate = cb.equal(expression, bank.getOrderId());
			predicates.add(rolePredicate);
		}*/
		return predicates;
	}
	public Long getCountByCriteria(T approvalHierarcy){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		 
		  CriteriaQuery<Long> q = cb.createQuery(Long.class);
		  Root<T> c = q.from(getClassType());
		  q.select(cb.count(c));
		  
		  List<Predicate> predicates=  setCriteria(approvalHierarcy, cb, c);
		  if(!predicates.isEmpty())
		  {
		    Predicate[] pre=new Predicate[predicates.size()];
			Predicate predicate=cb.and(predicates.toArray(pre));
			q.where(predicate);
		  }
		  return entityManager.createQuery(q).getSingleResult();
	}
	
}
