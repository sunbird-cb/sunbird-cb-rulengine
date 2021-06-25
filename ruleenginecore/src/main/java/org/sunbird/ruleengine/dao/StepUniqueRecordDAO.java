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
import org.sunbird.ruleengine.model.StepUniqueRecord;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class StepUniqueRecordDAO extends AbstractDAO<StepUniqueRecord, StepUniqueRecord>{


	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<StepUniqueRecord> getClassType() {
		return StepUniqueRecord.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<StepUniqueRecord> root, StepUniqueRecord example) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		/*if (true) {
			Path<Long> p = root.get("clientId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getClientId()));
		}*/
		if (example.getUniqueKey()!= null && !example.getUniqueKey().isEmpty()  ) {
			Path<Long> p = root.get("uniqueKey");
			predicates.add(cb.equal(p, example.getUniqueKey()));
		}
		
		if (example.getStepId()!= null  ) {
			Path<Long> p = root.get("stepId");
			predicates.add(cb.equal(p, example.getStepId()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	
	}
	
	

	
}
