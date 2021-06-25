package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.SystemJobDetail;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class SystemJobDetailNewDao extends AbstractDAO<SystemJobDetail, SystemJobDetail> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<SystemJobDetail> getClassType() {
		return SystemJobDetail.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<SystemJobDetail> root, SystemJobDetail example) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
	
		if (example.getStatus() != null) {
			Path<Long> p = root.get("status");
			predicates.add(cb.equal(p, example.getStatus()));
		}

		if (example.getId() != null) {
			Path<Long> p = root.get("id");
			predicates.add(cb.equal(p, example.getId()));
		}
		if (example.getClientId() != null) {
			Path<Long> p = root.get("clientId");
			predicates.add(cb.equal(p, example.getClientId()));
		}
		if (example.getJobName() != null) {
			Path<String> p = root.get("jobName");
			predicates.add(cb.like(p, '%'+example.getJobName()+'%'));
		}


		
		
		if (example.getToDateTime() != null) {
			Expression<Date> expression = root.get("nextRunTime");
			Predicate currencyMasterPredicate = cb.lessThanOrEqualTo(expression, example.getToDateTime());
			predicates.add(currencyMasterPredicate);
		}

		return predicates.toArray(new Predicate[predicates.size()]);

	}

	@Override
	public String getCriteriaForVo(SystemJobDetail SystemJobDetail) {
		String query = "";

		if (SystemJobDetail.getId() != null) {
			query = query + "and (jd.ID)= :id ";
		}
		if (SystemJobDetail.getJobName() != null && !SystemJobDetail.getJobName().isEmpty()) {
			query = query + "and (jd.JOB_NAME)= :jobName ";
		}

		if (SystemJobDetail.getClientId() != null) {
			query = query + "and (jd.CLIENT_ID)= :clientId ";
		}
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, SystemJobDetail SystemJobDetail) {

		if (SystemJobDetail.getId() != null) {
			queryJpa.setParameter("id", SystemJobDetail.getId());
		}
		if (SystemJobDetail.getJobName() != null && !SystemJobDetail.getJobName().isEmpty()) {
			queryJpa.setParameter("jobName", SystemJobDetail.getJobName());
		}
		if (SystemJobDetail.getClientId() != null) {
			queryJpa.setParameter("clientId", SystemJobDetail.getClientId());
		}
	}

}
