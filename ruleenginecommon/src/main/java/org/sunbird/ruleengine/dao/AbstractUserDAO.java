package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.AbstractUser;

@Repository
public abstract class AbstractUserDAO<T extends AbstractUser, U extends AbstractUser> extends AbstractDAO<T, U>{

	
	

	private List<Predicate> setCriteriaForRole(T t, CriteriaBuilder cb,
			Root<T> c) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		
		return predicates;
	}


	
}
