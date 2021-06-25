package org.sunbird.ruleengine.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sunbird.ruleengine.dao.FailureMailConfigDao;
import org.sunbird.ruleengine.model.FailureMailConfig;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class FailureMailConfigServiceImpl extends GenericServiceImpl<FailureMailConfig, FailureMailConfig> implements FailureMailConfigService{

	@Autowired
	FailureMailConfigDao failureMailConfigDao;
	
	@Override
	public AbstractDAO<FailureMailConfig, FailureMailConfig> getDAO() {
		return failureMailConfigDao;
	}

}
