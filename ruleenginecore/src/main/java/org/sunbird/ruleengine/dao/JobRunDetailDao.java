package org.sunbird.ruleengine.dao;

import java.sql.Date;
import java.util.ArrayList;
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
import org.sunbird.ruleengine.model.JobRunDetail;

@Repository
public class JobRunDetailDao extends AbstractDAO<JobRunDetail, JobRunDetail> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<JobRunDetail> getClassType() {
		return JobRunDetail.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<JobRunDetail> root, JobRunDetail example) {

		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		if (example.getId() != null) {
			Path<Long> p = root.get("id");
			predicates.add(cb.equal(p, example.getId()));
		}

		if (example.getJobDetailId() != null) {
			Path<Long> p = root.get("jobDetailId");
			predicates.add(cb.equal(p, example.getJobDetailId()));
		}
		if (example.getClientId() != null) {
			Path<Long> p = root.get("clientId");
			predicates.add(cb.equal(p, example.getClientId()));
		}
		if ( example.getStartDate()!=null) {
			Expression<Date> p= root.get("creationDate");
			predicates.add(cb.greaterThanOrEqualTo(p, example.getStartDate()));	
		}
		if (example.getEndDate() != null ) {
			Expression<Date> p= root.get("creationDate");
			predicates.add(cb.lessThanOrEqualTo(p, example.getEndDate() ));
		}
		if (example.getStartTime() != null ) {
			Expression<Date> p= root.get("startTime");
			predicates.add(cb.greaterThanOrEqualTo(p, example.getStartTime() ));
		}
		return predicates.toArray(new Predicate[predicates.size()]);

	}

	@Override
	public String getCriteriaForVo(JobRunDetail JobRunDetail) {
		String query = "";

		if (JobRunDetail.getId() != null) {
			query = query + "and (jobRunDetail.ID)= :id ";
		}
		if (JobRunDetail.getJobName() != null && !JobRunDetail.getJobName().isEmpty()) {
			query = query + "and (jobRunDetail.JOB_NAME)= :jobName ";
		}
		if (JobRunDetail.getJobDetailId() != null) {
			query = query + "and (jobRunDetail.JOB_DETAIL_ID)= :jobDetailId ";
		}

		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, JobRunDetail JobRunDetail) {

		if (JobRunDetail.getId() != null) {
			queryJpa.setParameter("id", JobRunDetail.getId());
		}
		if (JobRunDetail.getJobName() != null && !JobRunDetail.getJobName().isEmpty()) {
			queryJpa.setParameter("jobName", JobRunDetail.getJobName());
		}
		if (JobRunDetail.getJobDetailId() != null) {
			queryJpa.setParameter("jobDetailId", JobRunDetail.getJobDetailId());
		}
	}

}
