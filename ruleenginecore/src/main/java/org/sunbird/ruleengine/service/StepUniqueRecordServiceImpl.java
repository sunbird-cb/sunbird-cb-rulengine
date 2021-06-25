package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.StepUniqueRecordDAO;
import org.sunbird.ruleengine.model.StepUniqueRecord;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class StepUniqueRecordServiceImpl extends GenericServiceImpl<StepUniqueRecord, StepUniqueRecord> implements StepUniqueRecordService {

	@Autowired
	StepUniqueRecordDAO stepUniqueRecordDAO;

	@Override
	public AbstractDAO<StepUniqueRecord, StepUniqueRecord> getDAO() {
		return stepUniqueRecordDAO;
	}

	

}
