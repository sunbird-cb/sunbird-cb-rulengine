package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.JobSettings;

@Repository
public class JobSettingsDao extends AbstractSettingsDao<JobSettings, JobSettings>{

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<JobSettings> getClassType() {
		return JobSettings.class;
	}

	@Override
	public String getCriteriaForVo(JobSettings jobSettings) {
		String query="";

		if(jobSettings.getId()!= null)
		{
			query=query+"and (js.ID)= :id ";
		}
		if(jobSettings.getJobDetailId()!= null)
		{
			query=query+"and (js.JOB_DETAIL_ID)= :jobDetailId ";
		}

		if(jobSettings.getClientId()!= null)
		{
			query=query+"and (js.CLIENT_ID)= :clientId ";
		}

		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, JobSettings jobSettings) {

		if(jobSettings.getId()!=null)
		{
			queryJpa.setParameter("id",jobSettings.getId());
		}
		if(jobSettings.getJobDetailId()!=null)
		{
			queryJpa.setParameter("jobDetailId",jobSettings.getJobDetailId());
		}
		if(jobSettings.getClientId()!=null)
		{
			queryJpa.setParameter("clientId",jobSettings.getClientId());
		}
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<JobSettings> root, JobSettings example) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.addAll(Arrays.asList(super.getSearchPredicates(root, example)));
		if (example.getJobDetailId() != null) {
			Path<Long> p = root.get("jobDetailId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getJobDetailId()));
		}
		if (example.getClientId() != null) {
			Path<Long> p = root.get("clientId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getClientId()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
