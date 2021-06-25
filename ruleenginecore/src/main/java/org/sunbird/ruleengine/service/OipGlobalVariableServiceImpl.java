package org.sunbird.ruleengine.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sunbird.ruleengine.dao.OipGlobalVariableDAO;
import org.sunbird.ruleengine.model.OipGlobalVariable;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class OipGlobalVariableServiceImpl extends GenericServiceImpl<OipGlobalVariable, OipGlobalVariable> implements OipGlobalVariableService{

	@Autowired
	OipGlobalVariableDAO oipGlobalVariableDao;
	
	@Override
	public AbstractDAO<OipGlobalVariable, OipGlobalVariable> getDAO() {
		
		return oipGlobalVariableDao;
	}
}
