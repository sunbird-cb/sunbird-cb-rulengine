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
import org.sunbird.ruleengine.model.Role;

import org.sunbird.ruleengine.common.CommonUtil;
import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class RoleDao extends AbstractDAO<Role, Role>{

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<Role> getClassType() {
		return Role.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<Role> root, Role example) {
		List<Predicate> predicates= new ArrayList<>();
		if(CommonUtil.isNotBlank(example.getRoleName()))
		{
			Path<String> p=root.get("roleName");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.like(cb.lower(p), "%"+example.getRoleName().toLowerCase()+"%"));
		}
		
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}
