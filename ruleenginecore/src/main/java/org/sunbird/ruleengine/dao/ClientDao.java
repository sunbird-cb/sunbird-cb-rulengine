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
import org.sunbird.ruleengine.model.Client;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class ClientDao extends AbstractDAO<Client, Client> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<Client> getClassType() {
		return Client.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<Client> root, Client example) {
		List<Predicate> predicates = new ArrayList<>();
		if(example.getCode()!=null && !example.getCode().isEmpty()  )
		{
			Path<String> p=root.get("code");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.like(cb.lower(p), "%"+example.getCode().toLowerCase()+"%"));
		}
		
		if( example.getName()!=null && !example.getName().isEmpty() )
		{
			Path<String> p=root.get("name");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.like(cb.lower(p), "%"+example.getName().toLowerCase()+"%"));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}


}
