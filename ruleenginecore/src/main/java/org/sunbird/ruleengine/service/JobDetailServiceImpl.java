package org.sunbird.ruleengine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.JobDetailDAO;
import org.sunbird.ruleengine.model.JobDetail;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;


@Service
@Transactional
public class JobDetailServiceImpl  extends GenericServiceImpl<JobDetail, JobDetail> implements JobDetailService{

	@Autowired
	JobDetailDAO jobDetailDao;

	@Override
	public void save(JobDetail jobDetail) {
		jobDetailDao.save(jobDetail);
	}


	@Override
	public JobDetail update(JobDetail jobDetail) {
		return jobDetailDao.update(jobDetail);
		
	}



	@Override
	public List<JobDetail> getListByCriteria(JobDetail jobDetail, int firstResult, int maxResult) {
		List<JobDetail> jobDetails= jobDetailDao.getListByCriteria(jobDetail, firstResult, maxResult);
		return jobDetails;
	}


	@Override
	public AbstractDAO<JobDetail, JobDetail> getDAO() {
		return jobDetailDao;
		
	}
	
}
