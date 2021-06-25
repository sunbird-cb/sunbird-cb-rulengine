package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.StepRequestValidator;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class StepRequestValidatorDao extends AbstractDAO<StepRequestValidator, StepRequestValidator>{


	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<StepRequestValidator> getClassType() {
		return StepRequestValidator.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<StepRequestValidator> root, StepRequestValidator example) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		
		if (example.getRequestValidation()!= null && !example.getRequestValidation().isEmpty()  ) {
			Path<Long> p = root.get("requestValidation");
			predicates.add(cb.equal(p, example.getRequestValidation()));
		}
		
		if (example.getResultFlag()!= null  ) {
			Path<Long> p = root.get("resultFlag");
			predicates.add(cb.equal(p, example.getResultFlag()));
		}
		
		if (example.getStepId()!= null  ) {
			Path<Long> p = root.get("stepId");
			predicates.add(cb.equal(p, example.getStepId()));
		}
		
	/*	if (example.getId()!= null  ) {
			Path<Long> p = root.get("id");
			predicates.add(cb.equal(p, example.getId()));
		}
		*/
		if (example.getSequence()!= null  ) {
			Path<Long> p = root.get("sequence");
			predicates.add(cb.equal(p, example.getSequence()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	
	}
	
	
}
