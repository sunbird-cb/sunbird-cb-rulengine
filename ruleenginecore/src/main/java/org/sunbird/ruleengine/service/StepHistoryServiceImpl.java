package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.StepHistoryDao;
import org.sunbird.ruleengine.model.StepHistory;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class StepHistoryServiceImpl
extends GenericServiceImpl<StepHistory, StepHistory> implements StepHistoryService { 

	@Autowired
	StepHistoryDao stepHistoryDao;

	@Override
	public AbstractDAO<StepHistory, StepHistory> getDAO() {
		return stepHistoryDao;

	}

}
