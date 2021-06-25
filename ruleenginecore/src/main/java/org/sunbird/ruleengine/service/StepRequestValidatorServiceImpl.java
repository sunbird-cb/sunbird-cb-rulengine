package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.StepRequestValidatorDao;
import org.sunbird.ruleengine.model.StepRequestValidator;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class StepRequestValidatorServiceImpl extends GenericServiceImpl<StepRequestValidator, StepRequestValidator> implements StepRequestValidatorService {

	@Autowired
	StepRequestValidatorDao StepRequestValidatorDao;

	@Override
	public AbstractDAO<StepRequestValidator, StepRequestValidator> getDAO() {
		return StepRequestValidatorDao;
	}

	

}
