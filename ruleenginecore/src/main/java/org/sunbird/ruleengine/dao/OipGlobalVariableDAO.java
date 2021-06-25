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
import org.sunbird.ruleengine.model.OipGlobalVariable;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
public class OipGlobalVariableDAO extends AbstractDAO<OipGlobalVariable, OipGlobalVariable> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {

		return entityManager;
	}

	@Override
	public Class<OipGlobalVariable> getClassType() {

		return OipGlobalVariable.class;
	}

	protected Predicate[] getSearchPredicates(Root<OipGlobalVariable> root, OipGlobalVariable example) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		if (example.getJobDetailId() != null) {

			Path<Long> p = root.get("jobDetailId");
			predicates.add(cb.equal(p, example.getJobDetailId()));
		}
		
		if (example.getGlobalVariableName() != null) {

			Path<Long> p = root.get("globalVariableName");
			predicates.add(cb.equal(p, example.getGlobalVariableName()));
		}

		if (example.getId() != null) {

			Path<Long> p = root.get("id");
			predicates.add(cb.equal(p, example.getId()));
		}

		if (example.getClientId() != null) {

			Path<Long> p = root.get("clientId");
			predicates.add(cb.equal(p, example.getClientId()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);

	}

	@Override
	public String getCriteriaForVo(OipGlobalVariable oipGlobalVariable) {

		String query = "";

		if (oipGlobalVariable.getId() != null) {

			query = query + "AND oipGlobalVariables.ID = :id ";
		}

		if (oipGlobalVariable.getClientId() != null) {

			query = query + "AND oipGlobalVariables.CLIENT_ID = :clientId ";
		}

		if (oipGlobalVariable.getJobDetailId() != null) {

			query = query + "AND oipGlobalVariables.JOB_DETAIL_ID = :jobDetailId ";
		}

		if (oipGlobalVariable.getGlobalVariableName() != null && !oipGlobalVariable.getGlobalVariableName().isEmpty()) {

			query = query + "AND oipGlobalVariables.GLOBAL_VARIABLE_NAME = :globalVariableName ";
		}

		return query;

	}

	@Override
	public void setBindParameterForVo(Query queryJpa, OipGlobalVariable oipGlobalVariable) {

		if (oipGlobalVariable.getId() != null) {

			queryJpa.setParameter("id", oipGlobalVariable.getId());
		}

		if (oipGlobalVariable.getClientId() != null) {

			queryJpa.setParameter("clientId", oipGlobalVariable.getClientId());
		}

		if (oipGlobalVariable.getJobDetailId() != null) {

			queryJpa.setParameter("jobDetailId", oipGlobalVariable.getJobDetailId());
		}
		
		if (oipGlobalVariable.getGlobalVariableName() != null && !oipGlobalVariable.getGlobalVariableName().isEmpty()) {

			queryJpa.setParameter("globalVariableName", oipGlobalVariable.getGlobalVariableName());
		}
	}
}
