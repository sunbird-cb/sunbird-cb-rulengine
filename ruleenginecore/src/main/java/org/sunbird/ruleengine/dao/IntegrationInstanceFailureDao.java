package org.sunbird.ruleengine.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
@Transactional
public class IntegrationInstanceFailureDao extends AbstractDAO<IntegrationInstanceFailure, IntegrationInstanceFailure> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<IntegrationInstanceFailure> getClassType() {
		return IntegrationInstanceFailure.class;
	}

	@Override
	public String getCriteriaForVo(IntegrationInstanceFailure integrationInstanceFailure) {
		String query = "";

		if (integrationInstanceFailure.getId() != null) {
			query = query + "and (igFail.ID)= :id ";
		}
		if (integrationInstanceFailure.getStepId() != null) {
			query = query + "and (igFail.STEP_ID)= :stepId ";
		}
		if (integrationInstanceFailure.getClientId() != null) {
			query = query + "and (igFail.CLIENT_ID)= :clientId ";
		}
		if (integrationInstanceFailure.getJobDetailId() != null) {
			query = query + "and (igFail.JOB_DETAIL_ID)= :jobDetailId ";
		}
		if (integrationInstanceFailure.getJobRunId() != null) {
			query = query + "and (igFail.JOB_RUN_ID)= :jobRunId ";
		}
		if (integrationInstanceFailure.getNextRequestJson() != null && !integrationInstanceFailure.getNextRequestJson().isEmpty()) {
			query = query + "and (igFail.NEXT_REQUEST_JSON) like :nextRequestJson ";
		}
		
		if (integrationInstanceFailure.getErrorCode() != null && !integrationInstanceFailure.getErrorCode().isEmpty()) {
			query = query + "and (igFail.ERROR_CODE) like :errorCode ";
		}
		
		
		
		
		
		
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, IntegrationInstanceFailure integrationInstanceFailure) {

		if (integrationInstanceFailure.getId() != null) {
			queryJpa.setParameter("id", integrationInstanceFailure.getId());
		}
		if (integrationInstanceFailure.getStepId() != null) {
			queryJpa.setParameter("stepId", integrationInstanceFailure.getStepId());
		}
		if (integrationInstanceFailure.getClientId() != null) {
			queryJpa.setParameter("clientId", integrationInstanceFailure.getClientId());
		}
		if (integrationInstanceFailure.getJobDetailId() != null) {
			queryJpa.setParameter("jobDetailId", integrationInstanceFailure.getJobDetailId());
		}
		if (integrationInstanceFailure.getJobRunId() != null) {
			queryJpa.setParameter("jobRunId", integrationInstanceFailure.getJobRunId());
		}
		if (integrationInstanceFailure.getNextRequestJson() != null && !integrationInstanceFailure.getNextRequestJson().isEmpty()) {
			queryJpa.setParameter("nextRequestJson", "%"+integrationInstanceFailure.getNextRequestJson()+"%");
		}
		if (integrationInstanceFailure.getErrorCode() != null && !integrationInstanceFailure.getErrorCode().isEmpty()) {
			queryJpa.setParameter("errorCode", integrationInstanceFailure.getErrorCode());
		}
	}

	public List<IntegrationInstanceFailure> getIntegrationFailureList(IntegrationInstanceFailure i) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<IntegrationInstanceFailure> q = cb.createQuery(IntegrationInstanceFailure.class);
		Root<IntegrationInstanceFailure> c = q.from(IntegrationInstanceFailure.class);
		q.select(c);
		Expression<String> literal = cb.literal("%" + i.getErrorResponse() + "%");
		q.where(cb.equal(c.get("stepId").as(BigInteger.class), i.getStepId()), cb.equal(c.get("done"), i.isDone()),
				cb.like(c.<String>get("errorResponse").as(String.class), literal)

		);
		TypedQuery<IntegrationInstanceFailure> tq = entityManager.createQuery(q);
		List<IntegrationInstanceFailure> failureList = tq.getResultList();
		return failureList;
	}

	public List<IntegrationInstanceFailure> getIntegrationFailureListByDate(IntegrationInstanceFailure i,
			Date startDate, Date endDate,int firstResult, int maxResult, List<Object[]> notInErrorpatternIds) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<IntegrationInstanceFailure> q = cb.createQuery(IntegrationInstanceFailure.class);
	
		Root<IntegrationInstanceFailure> c = q.from(IntegrationInstanceFailure.class);
		q.select(c);
	if(notInErrorpatternIds.isEmpty())
		q.where(cb.equal(c.get("clientId").as(BigInteger.class), i.getClientId()),
				cb.equal(c.get("jobDetailId").as(BigInteger.class), i.getJobDetailId()),
				cb.equal(c.get("done"), i.isDone()),
				cb.between(c.get("creationDate").as(Date.class), startDate, endDate));
	else
		q.where(cb.equal(c.get("clientId").as(BigInteger.class), i.getClientId()),
				cb.equal(c.get("jobDetailId").as(BigInteger.class), i.getJobDetailId()),
				cb.equal(c.get("done"), i.isDone()),
				cb.between(c.get("creationDate").as(Date.class), startDate, endDate),
				cb.or(c.get("errorPatternId").as(BigInteger.class).isNull(),
					  c.get("errorPatternId").as(BigInteger.class).in(notInErrorpatternIds).not())
				);
	
		TypedQuery<IntegrationInstanceFailure> tq = entityManager.createQuery(q).setFirstResult(firstResult).setMaxResults(maxResult);
		List<IntegrationInstanceFailure> failureList = tq.getResultList();
		return failureList;
	}

	
	public List<IntegrationInstanceFailure> getIntegrationFailureListByDateOnly(IntegrationInstanceFailure i,
			Date startDate, Date endDate,int firstResult, int maxResult) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<IntegrationInstanceFailure> q = cb.createQuery(IntegrationInstanceFailure.class);
	
		Root<IntegrationInstanceFailure> c = q.from(IntegrationInstanceFailure.class);
		q.select(c);
	
		q.where(cb.equal(c.get("clientId").as(BigInteger.class), i.getClientId()),
				cb.between(c.get("creationDate").as(Date.class), startDate, endDate));
		TypedQuery<IntegrationInstanceFailure> tq = null;
		if(firstResult<0)
		{
			tq = entityManager.createQuery(q);
		}
		else
		{
			tq = entityManager.createQuery(q).setFirstResult(firstResult).setMaxResults(maxResult);
		}
		List<IntegrationInstanceFailure> failureList = tq.getResultList();
		return failureList;
	}
}
