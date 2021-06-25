package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.JobRunDetailDao;
import org.sunbird.ruleengine.model.JobRunDetail;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class JobRunDetailServiceImpl extends GenericServiceImpl<JobRunDetail, JobRunDetail>
		implements JobRunDetailService {

	@Autowired
	JobRunDetailDao jobRunDetailDao;

	@Override
	public AbstractDAO<JobRunDetail, JobRunDetail> getDAO() {
		return jobRunDetailDao;
	}

}
