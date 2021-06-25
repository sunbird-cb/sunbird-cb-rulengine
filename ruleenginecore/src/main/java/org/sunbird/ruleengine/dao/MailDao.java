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
import org.sunbird.ruleengine.model.Mail;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class MailDao extends AbstractDAO<Mail, Mail> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<Mail> getClassType() {
		return Mail.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<Mail> root, Mail example) {
		List<Predicate> predicates = new ArrayList<>();
		if (example.getJobDetailId() != null) {
			Path<Long> p = root.get("jobDetailId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getJobDetailId()));
		}
		if (example.getClientId() != null) {
			Path<Long> p = root.get("clientId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getClientId()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public String getCriteriaForVo(Mail customerAccountVo) {
		String query = "";

		return query;
	}

}
