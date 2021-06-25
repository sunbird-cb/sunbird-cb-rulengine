package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.StepDAO;
import org.sunbird.ruleengine.model.Step;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;


@Service
@Transactional
public class StepServiceImpl  extends GenericServiceImpl<Step, Step>implements StepService{
	private static final Logger logger = LogManager.getLogger(StepServiceImpl.class);
	@Autowired
	StepDAO stepDao;


	@Override
	public void save(Step step) {
		stepDao.save(step);	
	}


	@Override
	public Step update(Step step) {
		return stepDao.update(step);	

	}

	@Override
	public List<Step> getListByCriteria(Step step, int firstResult, int maxResult) {
		List<Step> steps= stepDao.getListByCriteria(step, -1, 0);
		return steps;
	}


	@Override
	public AbstractDAO<Step, Step> getDAO() {
		return stepDao;

	}

	@Override
	public BigInteger getLastStepId(BigInteger jobDetailId) {

		Integer stepIdWithParentNull = (Integer) getCount(new Step(), "SELECT ID FROM STEP where job_detail_id = "
				+ jobDetailId + " and parent_id is null order by sequence desc LIMIT 1");

		Integer stepId = null;
		try {
			stepId = (Integer) getCount(new Step(), "SELECT ID FROM STEP where job_detail_id = " + jobDetailId
					+ " AND PARENT_ID IN(" + stepIdWithParentNull + ") order by sequence desc LIMIT 1");
		} catch (EmptyResultDataAccessException nre) {
			/*nre.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , nre);*/
		}

		if (stepId == null) {
			return BigInteger.valueOf(stepIdWithParentNull.intValue());
		}
		return BigInteger.valueOf(stepId.intValue());
	}

}
