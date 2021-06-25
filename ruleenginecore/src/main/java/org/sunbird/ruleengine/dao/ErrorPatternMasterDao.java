package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.ErrorPatternMaster;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class ErrorPatternMasterDao extends AbstractDAO<ErrorPatternMaster, ErrorPatternMaster>{

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<ErrorPatternMaster> getClassType() {
		return ErrorPatternMaster.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<ErrorPatternMaster> root, ErrorPatternMaster example) {
		List<Predicate> predicates = new ArrayList<>();
		
		
		if (example.getClientId() != null  ) {
			Path<Long> p = root.get("clientId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getClientId()));
		}
		
		if (example.getErrorCode() != null  ) {
			Path<String> p = root.get("errorCode");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.like(p, "%"+example.getErrorCode()+"%"));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	
	
	
	
	
	
}
