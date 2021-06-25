package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.UserEvent;

import org.sunbird.ruleengine.common.DateUtil;
import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class UserEventDao extends
		AbstractDAO<UserEvent, UserEvent> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<UserEvent> getClassType() {
		return UserEvent.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<UserEvent> root,
			UserEvent example) {

		List<Predicate> predicates = new ArrayList<>();
		if (example.getId() != null) {
			Path<Long> p = root.get("id");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getId()));
		}
		if ( example.getFullName()!= null && !example.getFullName().isEmpty()) {
			Path<String> p = root.get("fullName");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.like(cb.lower(p), "%"+example.getFullName().toLowerCase()+"%"));
		}
		if ( example.getUserName()!= null && !example.getUserName().isEmpty()) {
			Path<String> p = root.get("userName");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.like(cb.lower(p), "%"+example.getUserName().toLowerCase()+"%"));
		}
		if ( example.getFromDate()!= null ) {
			Path<Date> p = root.get("creationDate");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add((cb.greaterThan(p, DateUtil.fromDate(example.getFromDate()))));
		}
		if ( example.getToDate()!= null ) {
			Path<Date> p = root.get("creationDate");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add((cb.lessThan(p, DateUtil.toDate(example.getToDate()))));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public String getCriteriaForVo(UserEvent userLoginEvent) {
		String query = "";
		if (userLoginEvent.getCreatedBy() != null) {
			query = query + "and (userLoginEvent.CREATED_BY) = :createdBy ";

		}

		if (userLoginEvent.getUserName() != null) {
			query = query + "and (userLoginEvent.USER_NAME) = :userName ";
		}
		if (userLoginEvent.getFullName() != null) {
			query = query + "and (userLoginEvent.FULL_NAME) = :fullName ";

		}
		
		/*if (userLoginEvent.getFromDate() != null) {
			query = query + "and (userLoginEvent.FROM_DATE) = :fromDate ";

		}
		if (userLoginEvent.getToDate() != null) {
			query = query + "and (userLoginEvent.TO_DATE) = :toDate ";

		}*/
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa,
			UserEvent userLoginEvent) {

		if (userLoginEvent.getCreatedBy() != null) {
			queryJpa.setParameter("createdBy",
					userLoginEvent.getCreatedBy());
		}
		
		if (userLoginEvent.getUserName() != null) {
			queryJpa.setParameter("userName", userLoginEvent.getUserName());
		}
		
		if (userLoginEvent.getFullName() != null) {
			queryJpa.setParameter("fullName", userLoginEvent.getFullName());
		}
		/*if (userLoginEvent.getFromDate() != null) {
			queryJpa.setParameter("fromDate",
					userLoginEvent.getFromDate());
		}
		if (userLoginEvent.getToDate() != null) {
			queryJpa.setParameter("toDate",
					userLoginEvent.getToDate());
		}*/
	}

}
