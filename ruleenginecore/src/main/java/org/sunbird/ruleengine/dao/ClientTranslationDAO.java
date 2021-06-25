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
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.model.ClientTranslation;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Repository
@Transactional
public class ClientTranslationDAO extends AbstractDAO<ClientTranslation, ClientTranslation> {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<ClientTranslation> getClassType() {
		return ClientTranslation.class;
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<ClientTranslation> root, ClientTranslation example) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		if (example.getId() != null) {
			Path<Long> p = root.get("id");
			predicates.add(cb.equal(p, example.getId()));
		}
		if (example.getClientId() != null) {
			Path<Long> p = root.get("clientId");
			predicates.add(cb.equal(p, example.getClientId()));
		}
		if (example.getCode() != null) {
			Path<String> p = root.get("code");
			predicates.add(cb.equal(p, example.getCode()));
		}
		if (example.getKey() != null) {
			Path<String> p = root.get("key");
			predicates.add(cb.equal(p, example.getKey()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public String getCriteriaForVo(ClientTranslation stepTranslation) {
		String query = "";

		if (stepTranslation.getId() != null) {
			query = query + "and (st.ID)= :id ";
		}

		if (stepTranslation.getClientId() != null) {
			query = query + "and (st.CLIENT_ID)= :clientId ";
		}
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, ClientTranslation stepTranslation) {

		if (stepTranslation.getId() != null) {
			queryJpa.setParameter("id", stepTranslation.getId());
		}

		if (stepTranslation.getClientId() != null) {
			queryJpa.setParameter("clientId", stepTranslation.getClientId());
		}
	}

	public Long getCountForCodeandKeyAsCombined(String code, String key, BigInteger clientId) {
		BigInteger value = (BigInteger) entityManager
				.createNativeQuery("select count(*) from CLIENT_TRANSLATION where code like '" + code
						+ "' and _key like '" + key + "' and client_id = " + clientId + ";")
				.getSingleResult();
		return value.longValue();

	}

	public Long getCountForCodeandKeyAsCombinedExceptThis(String code, String key, BigInteger id, BigInteger clientId) {
		BigInteger value = (BigInteger) entityManager
				.createNativeQuery("select count(*) from CLIENT_TRANSLATION where code like '" + code
						+ "' and _key like '" + key + "' and id not in (" + id + ") and client_id= " + clientId + ";")
				.getSingleResult();
		return value.longValue();

	}

	public Long getCountForResolutionViaCode(String code, BigInteger clientId,String key) {
		BigInteger value = (BigInteger) entityManager
				.createNativeQuery("select count(*) from CLIENT_TRANSLATION where code like '" + code
						+ "' and RECIPROCAL = 1 and client_id = " + clientId + " and _KEY = '"+key+"' ;")
				.getSingleResult();
		return value.longValue();

	}

	public Long getCountForResolutionViaCodeExceptThis(String code, BigInteger id, BigInteger clientId,String key) {
		BigInteger value = (BigInteger) entityManager
				.createNativeQuery("select count(*) from CLIENT_TRANSLATION where code like '" + code
						+ "' and RECIPROCAL = 1 and id not in (" + id + ") and client_id = " + clientId + " and _KEY = '"+key+"' ;")
				.getSingleResult();
		return value.longValue();

	}

	// To flush previous data of client translation coming from CSV
	public void deleteAllTranslationsFromCsv(String code, BigInteger clientId) {

		Query q = entityManager.createNativeQuery(
				"delete from CLIENT_TRANSLATION where code like '" + code + "'and client_id= " + clientId + ";");
		q.executeUpdate();

	}

}
