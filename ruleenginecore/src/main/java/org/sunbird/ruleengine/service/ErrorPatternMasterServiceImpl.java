package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.ErrorPatternMasterDao;
import org.sunbird.ruleengine.model.ErrorPatternMaster;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class ErrorPatternMasterServiceImpl extends GenericServiceImpl<ErrorPatternMaster, ErrorPatternMaster> implements ErrorPatternMasterService {

	@Autowired
	ErrorPatternMasterDao errorPatternMasterDao;
	
	@Override
	public AbstractDAO<ErrorPatternMaster, ErrorPatternMaster> getDAO() {
		return errorPatternMasterDao;
	}

}
