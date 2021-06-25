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
import org.sunbird.ruleengine.model.RolePermissionJoin;
import org.sunbird.ruleengine.vo.RolePermissionJoinVo;

import org.sunbird.ruleengine.dao.AbstractRolePermissionJoinDAO;

@Repository
public class RolePermissionJoinDAO extends AbstractRolePermissionJoinDAO<RolePermissionJoin, RolePermissionJoinVo> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {

		return entityManager;
	}

	@Override
	public Class<RolePermissionJoin> getClassType() {

		return RolePermissionJoin.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<RolePermissionJoin> root,
			RolePermissionJoinVo example) {
		List<Predicate> predicates= new ArrayList<>();

		if(example.getRoleId()!=null)
		{
			Path<Long> p=root.get("roleId");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getRoleId()));
		}


		if(example.getOrgId()!=null)
		{
			Path<Long> p=root.get("orgId");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getRoleId()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public String getCriteriaForVo(RolePermissionJoinVo criteria) {
		String query="";


		if(criteria.getRoleId()!=null)
		{
			query=query+"and (rolePermission.ROLE_ID)= :roleId ";

		}

		if(criteria.getOrgId()!=null)
		{
			query=query+"and (rolePermission.ORG_ID)= :orgId ";

		}
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa,
			RolePermissionJoinVo criteria) {
		if(criteria.getRoleId()!=null)
		{
			queryJpa.setParameter("roleId",criteria.getRoleId());
		}
		if(criteria.getOrgId()!=null)
		{
			queryJpa.setParameter("orgId",criteria.getOrgId());
		}
	}

}
