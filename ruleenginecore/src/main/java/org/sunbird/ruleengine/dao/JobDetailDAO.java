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
import org.sunbird.ruleengine.model.JobDetail;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class JobDetailDAO extends AbstractDAO<JobDetail, JobDetail> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<JobDetail> getClassType() {
		return JobDetail.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<JobDetail> root, JobDetail example) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		/*
		 * if (true) { Path<Long> p = root.get("clientId"); CriteriaBuilder cb =
		 * getEntityManager().getCriteriaBuilder(); predicates.add(cb.equal(p,
		 * example.getClientId())); }
		 */
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
		if (example.getJobCode() != null && !example.getJobCode().isEmpty()) {
			Path<Long> p = root.get("jobCode");
			predicates.add(cb.equal(p, example.getJobCode()));
		}
		
		if (example.getToDateTime() != null) {
			Expression<Date> expression = root.get("nextRunTime");
			Predicate currencyMasterPredicate = cb.lessThanOrEqualTo(expression, example.getToDateTime());
			predicates.add(currencyMasterPredicate);
		}
		if (example.getAgentJob() != null) {
			Path<Long> p = root.get("agentJob");
			predicates.add(cb.equal(p, example.getAgentJob()));
			
		}
		/*
		 * if (example.getParentId() != null ) { Path<Long> p =
		 * root.get("parentId"); CriteriaBuilder cb =
		 * getEntityManager().getCriteriaBuilder(); predicates.add(cb.equal(p,
		 * example.getParentId())); } if (example.getJobDetailId() != null ) {
		 * Path<Long> p = root.get("jobDetailId"); CriteriaBuilder cb =
		 * getEntityManager().getCriteriaBuilder(); predicates.add(cb.equal(p,
		 * example.getJobDetailId())); }
		 */
		return predicates.toArray(new Predicate[predicates.size()]);

	}

	@Override
	public String getCriteriaForVo(JobDetail jobDetail) {
		String query = "";

		if (jobDetail.getId() != null) {
			query = query + "and (jd.ID)= :id ";
		}
		if (jobDetail.getJobName() != null && !jobDetail.getJobName().isEmpty()) {
//			query = query + "and (jd.JOB_NAME)= :jobName ";
			query = query + "and jd.JOB_NAME LIKE  concat('%',:jobName,'%') ";

		}

		if (jobDetail.getClientId() != null) {
			query = query + "and (jd.CLIENT_ID)= :clientId ";
		}
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, JobDetail jobDetail) {

		if (jobDetail.getId() != null) {
			queryJpa.setParameter("id", jobDetail.getId());
		}
		if (jobDetail.getJobName() != null && !jobDetail.getJobName().isEmpty()) {
			queryJpa.setParameter("jobName", jobDetail.getJobName());
		}
		if (jobDetail.getClientId() != null) {
			queryJpa.setParameter("clientId", jobDetail.getClientId());
		}
	}

}
