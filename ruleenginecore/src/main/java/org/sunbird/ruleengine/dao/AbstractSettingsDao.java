package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sunbird.ruleengine.model.AbstractSettings;

import org.sunbird.ruleengine.dao.AbstractDAO;

public abstract class AbstractSettingsDao<T extends AbstractSettings, U extends AbstractSettings> extends AbstractDAO<T, U> {
	@Override
	protected Predicate[] getSearchPredicates(Root<T> root, U example) {

		List<Predicate> predicates = new ArrayList<>();
		if (true) {
			Path<Long> p = root.get("clientId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getClientId()));
		}
		if (example.getKey() != null) {
			Path<Long> p = root.get("key");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getKey()));
		}
		if (example.getValue() != null && !example.getValue().isEmpty()) {
			Path<Long> p = root.get("value");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getValue()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}
