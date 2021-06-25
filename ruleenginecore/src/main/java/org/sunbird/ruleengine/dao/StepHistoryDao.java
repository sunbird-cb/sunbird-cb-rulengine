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
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.model.StepHistory;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
@Transactional
public class StepHistoryDao extends AbstractDAO<StepHistory, StepHistory> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<StepHistory> getClassType() {
		return StepHistory.class;
	}

	@Override
	public String getCriteriaForVo(StepHistory stepHistory) {
		String query = "";

		if (stepHistory.getId() != null) {
			query = query + "and (igFail.ID)= :id ";
		}
		if (stepHistory.getStepId() != null) {
			query = query + "and (igFail.STEP_ID)= :stepId ";
		}
		if (stepHistory.getStatus() != null) {
			query = query + "and (igFail.STEP_HISTORY_STATUS)= :status ";
		}
		if (stepHistory.getClientId() != null) {
			query = query + "and (igFail.CLIENT_ID)= :clientId ";
		}
		if (stepHistory.getJobId() != null) {
			query = query + "and (igFail.JOB_ID)= :jobId ";
		}
		if (stepHistory.getLastUpdateDate() != null) {
			query = query + "and cast(igFail.LAST_UPDATE_DATE as date)= :lastUpdateDate ";
		}
		
		if (stepHistory.getRequest() != null && !stepHistory.getRequest().isEmpty()) {
			query = query + "and lower(igFail.SEARCH_CRITERIA) = :request ";
		}
		if (stepHistory.getResponse() != null && !stepHistory.getResponse().isEmpty()) {
			query = query + "and lower(igFail.RESPONSE) like :response ";
		}
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, StepHistory stepHistory) {

		if (stepHistory.getId() != null) {
			queryJpa.setParameter("id", stepHistory.getId());
		}
		if (stepHistory.getStepId() != null) {
			queryJpa.setParameter("stepId", stepHistory.getStepId());
		}
		if (stepHistory.getStatus() != null) {
			queryJpa.setParameter("status", stepHistory.getStatus().toString());
		}
		if (stepHistory.getClientId() != null) {
			queryJpa.setParameter("clientId", stepHistory.getClientId());
		}
		if (stepHistory.getJobId() != null) {
			queryJpa.setParameter("jobId", stepHistory.getJobId());
		}
		if (stepHistory.getLastUpdateDate() != null) {
			queryJpa.setParameter("lastUpdateDate", stepHistory.getLastUpdateDate());
		}
		if (stepHistory.getRequest()!= null && !stepHistory.getRequest().isEmpty()) {
			queryJpa.setParameter("request", stepHistory.getRequest().toLowerCase());
		}
		if (stepHistory.getResponse() != null && !stepHistory.getResponse().isEmpty()) {
			queryJpa.setParameter("response", "%"+stepHistory.getResponse().toLowerCase()+"%");
		}
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<StepHistory> root, StepHistory stepHistory) {
		
		List<Predicate> predicates = new ArrayList<>();
		if(stepHistory.getResponse() != null && !stepHistory.getResponse().isEmpty())
		{
			Path<String> p=root.get("response");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.like(cb.lower(p), "%"+stepHistory.getResponse().toLowerCase()+"%"));
		}
		if (stepHistory.getRequest() != null && !stepHistory.getRequest().isEmpty()) {
			Path<String> p=root.get("request");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.like(cb.lower(p), "%"+stepHistory.getResponse().toLowerCase()+"%"));
		}	
		if (stepHistory.getId() != null) {
			Path<Long> p=root.get("id");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, stepHistory.getId()));
		}
		if (stepHistory.getStepId() != null) {
			Path<Long> p=root.get("stepId");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, stepHistory.getStepId()));
		}
		if (stepHistory.getStatus() != null) {
			Path<Long> p=root.get("status");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, stepHistory.getStatus()));
		}
		if (stepHistory.getClientId() != null) {
			Path<Long> p=root.get("clientId");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, stepHistory.getClientId()));
		}
		
		if (stepHistory.getLastUpdateDate() != null) {
			Path<Long> p=root.get("lastUpdateDate");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, stepHistory.getLastUpdateDate()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
