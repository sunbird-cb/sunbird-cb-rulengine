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
import org.sunbird.ruleengine.model.TranslationMapper;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class TranslationMapperDAO extends AbstractDAO<TranslationMapper, TranslationMapper>{

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return entityManager;
	}

	@Override
	public Class<TranslationMapper> getClassType() {
		// TODO Auto-generated method stub
		return TranslationMapper.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<TranslationMapper> root, TranslationMapper example) {
		List<Predicate> predicates = new ArrayList<>();
		if(example.getCode()!=null && !example.getCode().isEmpty())
		{
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			Path<Long> p = root.get("code");
			predicates.add(cb.equal(p, example.getCode()));
		
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	
	
	
	
	
	
	
}
