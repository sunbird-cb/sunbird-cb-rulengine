package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.StepSettings;

@Repository
public class StepSettingsDao extends AbstractSettingsDao<StepSettings, StepSettings> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<StepSettings> getClassType() {
		return StepSettings.class;
	}

	@Override
	public String getCriteriaForVo(StepSettings stepSettings) {
		String query = "";

		if (stepSettings.getId() != null) {
			query = query + "and (stepSetting.ID)= :id ";
		}
		if (stepSettings.getStepId() != null) {
			query = query + "and (stepSetting.STEP_ID)= :stepId ";
		}
		if (stepSettings.getClientId() != null) {
			query = query + "and (stepSetting.CLIENT_ID)= :clientId ";
		}

		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, StepSettings stepSettings) {

		if (stepSettings.getId() != null) {
			queryJpa.setParameter("id", stepSettings.getId());
		}
		if (stepSettings.getStepId() != null) {
			queryJpa.setParameter("stepId", stepSettings.getStepId());
		}
		if (stepSettings.getClientId() != null) {
			queryJpa.setParameter("clientId", stepSettings.getClientId());
		}
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<StepSettings> root, StepSettings example) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.addAll(Arrays.asList(super.getSearchPredicates(root, example)));
		if (example.getStepId() != null) {
			Path<Long> p = root.get("stepId");
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getStepId()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
