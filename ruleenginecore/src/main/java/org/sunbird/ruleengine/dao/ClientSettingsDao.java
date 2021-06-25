package org.sunbird.ruleengine.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.ClientSettings;
import org.sunbird.ruleengine.vo.ClientSettingsVo;

@Repository
public class ClientSettingsDao extends AbstractSettingsDao<ClientSettings, ClientSettingsVo>{

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<ClientSettings> getClassType() {
		return ClientSettings.class;
	}


	
	
	
}
