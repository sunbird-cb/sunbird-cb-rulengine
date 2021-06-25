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
import org.sunbird.ruleengine.model.Step;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class StepDAO extends AbstractDAO<Step, Step> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<Step> getClassType() {
		return Step.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<Step> root, Step example) {
		List<Predicate> predicates = new ArrayList<>();
		/*if (true) {
			Path<Long> p = root.get("clientId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getClientId()));
		}*/
		if (example.getParentNull() != null && example.getParentNull() ) {
			Path<Long> p = root.get("parentId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.isNull(p));
		}
		if (example.getParentId() != null  ) {
			Path<Long> p = root.get("parentId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getParentId()));
		}
		if (example.getJobDetailId() != null ) {
			Path<Long> p = root.get("jobDetailId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getJobDetailId()));
		}
		if (example.getBeforeAgentCall() != null ) {
			Path<Long> p = root.get("beforeAgentCall");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getBeforeAgentCall()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public String getCriteriaForVo(Step step) {
		String query="";

		if(step.getId()!= null)
		{
			query=query+"and (step.ID)= :id ";
		}
		if(step.getJobDetailId()!= null)
		{
			query=query+"and (step.JOB_DETAIL_ID)= :jobDetailId ";
		}
		if(step.getClientId()!= null)
		{
			query=query+"and (step.CLIENT_ID)= :clientId ";
		}

		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, Step step) {

		if(step.getId()!=null)
		{
			queryJpa.setParameter("id",step.getId());
		}
		if(step.getJobDetailId()!=null)
		{
			queryJpa.setParameter("jobDetailId",step.getJobDetailId());
		}
		if(step.getClientId()!=null)
		{
			queryJpa.setParameter("clientId",step.getClientId());
		}
	}

}
