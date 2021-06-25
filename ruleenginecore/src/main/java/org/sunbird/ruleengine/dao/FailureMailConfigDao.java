package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.FailureMailConfig;



@Repository
public class FailureMailConfigDao extends AbstractDAO<FailureMailConfig, FailureMailConfig>{

	@PersistenceContext
	EntityManager entitymanager;
	
	@Override
	public EntityManager getEntityManager() {
		return entitymanager;
	}

	@Override
	public Class<FailureMailConfig> getClassType() {
		return FailureMailConfig.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<FailureMailConfig> root, FailureMailConfig example) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		if (example.getJobDetailId() != null) {
			Path<Long> p = root.get("jobDetailId");
			predicates.add(cb.equal(p, example.getJobDetailId()));
		}
		if (example.getId() != null) {
			Path<Long> p = root.get("id");
			predicates.add(cb.equal(p, example.getId()));
		}
		if (example.getClientId() != null) {
			Path<Long> p = root.get("clientId");
			predicates.add(cb.equal(p, example.getClientId()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public String getCriteriaForVo(FailureMailConfig criteria) {
		String query = "";

		if (criteria.getId() != null) {
			query = query + "and (failureMailConfig.ID)= :id ";
		}
		if (criteria.getClientId() != null ) {
			query = query + "and (failureMailConfig.CLIENT_ID)= :clientId ";
		}
		if (criteria.getJobDetailId() != null) {
			query = query + "and (failureMailConfig.JOB_DETAIL_ID)= :jobDetailId ";
		}

		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, FailureMailConfig criteria) {
		if (criteria.getId() != null) {
			queryJpa.setParameter("id", criteria.getId());
		}
		if (criteria.getClientId() != null ) {
			queryJpa.setParameter("clientId", criteria.getClientId());
		}
		if (criteria.getJobDetailId() != null) {
			queryJpa.setParameter("jobDetailId", criteria.getJobDetailId());
		}
	}
	
	
	
	
	
	
	
	
	
}
