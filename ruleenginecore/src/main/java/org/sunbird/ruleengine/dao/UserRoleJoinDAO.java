package org.sunbird.ruleengine.dao;

import java.math.BigInteger;
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
import org.sunbird.ruleengine.model.UserRoleJoin;
import org.sunbird.ruleengine.vo.UserRoleJoinVo;

import org.sunbird.ruleengine.dao.AbstractUserRoleJoinDAO;


@Repository
public class UserRoleJoinDAO extends AbstractUserRoleJoinDAO<UserRoleJoin, UserRoleJoinVo> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {

		return entityManager;
	}

	@Override
	public Class<UserRoleJoin> getClassType() {

		return UserRoleJoin.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<UserRoleJoin> root, UserRoleJoinVo example) {
		List<Predicate> predicates= new ArrayList<>();
		if(example.getOrgId()!=null)
		{
			Path<BigInteger> p=root.get("orgId");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getOrgId()));
		}
		if(example.getUserId()!=null)
		{
			Path<BigInteger> p=root.get("userId");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getUserId()));
		}
		if(example.getRoleId()!=null)
		{
			Path<BigInteger> p=root.get("roleId");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getRoleId()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public String getCriteriaForVo(UserRoleJoinVo criteria) {
		String query="";
		if(criteria.getUserId()!= null)
		{
			query=query+" and (userRole.USER_ID) = :userId ";
			//query=query+"and lower(registration.REGISTRATION_NO) like :registrationNo ";
		}
		if(criteria.getOrgId()!= null)
		{
			query=query+" and (userRole.ORG_ID) = :orgId ";
			//query=query+"and lower(registration.REGISTRATION_NO) like :registrationNo ";
		}
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, UserRoleJoinVo criteria) {
		if(criteria.getUserId()!=null)
		{
			queryJpa.setParameter("userId",criteria.getUserId());
		}
		if(criteria.getOrgId()!=null)
		{
			queryJpa.setParameter("orgId",criteria.getOrgId());
		}
	}

}
