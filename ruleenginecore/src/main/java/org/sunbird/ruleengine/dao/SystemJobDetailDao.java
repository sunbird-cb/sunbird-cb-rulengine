package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.SystemJobDetail;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class SystemJobDetailDao extends AbstractDAO<SystemJobDetail, SystemJobDetail> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return entityManager;
	}

	@Override
	public Class<SystemJobDetail> getClassType() {
		// TODO Auto-generated method stub
		return SystemJobDetail.class;
	}

	@Override
	public List<SystemJobDetail> getListByCriteria(SystemJobDetail systemJobDetail, int firstResult, int maxResult) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SystemJobDetail> q = cb.createQuery(SystemJobDetail.class);
		Root<SystemJobDetail> c = q.from(SystemJobDetail.class);
		List<Predicate> predicates = setCriteriaForCurrencyMasterSearch(systemJobDetail, cb, c);
		q.select(c);
		if (predicates.size() != 0) {
			Predicate[] pre = new Predicate[predicates.size()];
			Predicate predicate = cb.and(predicates.toArray(pre));
			q.where(predicate);
		}
		q.orderBy(cb.desc(c.get("id")));
		if (firstResult < 0) {
			return entityManager.createQuery(q).getResultList();
		} else {
			return entityManager.createQuery(q).setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		}
	}

	private List<Predicate> setCriteriaForCurrencyMasterSearch(SystemJobDetail systemJobDetail, CriteriaBuilder cb,
			Root<SystemJobDetail> c) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (systemJobDetail.getJobName() != null) {
			Expression<String> expression = c.get("jobName");
			Predicate currencyMasterPredicate = cb.like(expression, systemJobDetail.getJobName());
			predicates.add(currencyMasterPredicate);
		}
		if (systemJobDetail.getStatus() != null) {
			Expression<String> expression = c.get("status");
			Predicate currencyMasterPredicate = cb.equal(expression, systemJobDetail.getStatus());
			predicates.add(currencyMasterPredicate);
		}
		if (systemJobDetail.getToDateTime() != null) {
			Expression<Date> expression = c.get("nextRunTime");
			Predicate currencyMasterPredicate = cb.lessThanOrEqualTo(expression, systemJobDetail.getToDateTime());
			predicates.add(currencyMasterPredicate);
		}
		return predicates;
	}

	public Long getCountByCriteria(SystemJobDetail systemJobDetail) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<SystemJobDetail> c = q.from(SystemJobDetail.class);
		q.select(cb.count(c));
		List<Predicate> predicates = setCriteriaForCurrencyMasterSearch(systemJobDetail, cb, c);
		if (!predicates.isEmpty()) {
			Predicate[] pre = new Predicate[predicates.size()];
			Predicate predicate = cb.and(predicates.toArray(pre));
			q.where(predicate);
		}
		return entityManager.createQuery(q).getSingleResult();
	}

}
