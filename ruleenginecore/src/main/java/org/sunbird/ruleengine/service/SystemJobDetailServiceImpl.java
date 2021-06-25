package org.sunbird.ruleengine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.SystemJobDetailDao;
import org.sunbird.ruleengine.model.SystemJobDetail;
import org.sunbird.ruleengine.model.SystemJobDetail.Status;

@Service
@Transactional
public class SystemJobDetailServiceImpl implements SystemJobDetailService {
	private static final Logger logger = LogManager.getLogger(SystemJobDetailServiceImpl.class);
	@Autowired
	SystemJobDetailDao systemJobDetailDao;
	
	@Autowired
	ApplicationContext applicationContext;

	@Override
	public void save(SystemJobDetail systemJobDetail) {
		systemJobDetailDao.save(systemJobDetail);
	}


	@Override
	public SystemJobDetail update(SystemJobDetail systemJobDetail) {
		return systemJobDetailDao.update(systemJobDetail);
		
	}


	@Override
	public SystemJobDetail get(String id) {
		return systemJobDetailDao.getById(id);
	}

	@Override
	public List<SystemJobDetail> searchByCriteria(SystemJobDetail systemJobDetail, int firstResult, int maxResult) {
		List<SystemJobDetail> systemJobDetails = systemJobDetailDao.getListByCriteria(systemJobDetail, -1, 0);
		return systemJobDetails;
	}


	@Override
	public void startCron(ApplicationContext applicationContext) {
		SystemJobDetail jobDetail = new SystemJobDetail();
		jobDetail.setToDateTime(new Date());
		jobDetail.setStatus(SystemJobDetail.Status.STOPPED);

		List<SystemJobDetail> jobDetails = systemJobDetailDao.getListByCriteria(jobDetail, -1, 0);
		Iterator<SystemJobDetail> jobIterator = jobDetails.iterator();
		List<SystemJobDetail> workingJobDetails = new ArrayList<SystemJobDetail>();
		try {
			for (; jobIterator.hasNext();) {
				SystemJobDetail jobDetail2 = jobIterator.next();
				jobDetail2.setLastStartTime(new Date());
				jobDetail2.setStatus(Status.RUNNING);
				systemJobDetailDao.update(jobDetail2);
				workingJobDetails.add(jobDetail2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
			;
			jobIterator.remove();
		}
		for (SystemJobDetail jobDetail2 : workingJobDetails) {
			Job job = (Job) applicationContext.getBean(jobDetail2.getJobName());
			job.runJob();
		}

		for (SystemJobDetail jobDetail2 : workingJobDetails) {
			jobDetail2.setLastEndTime(new Date());
			jobDetail2.setNextRunTime(new Date(new Date().getTime() + jobDetail2.getInterval() * 60 * 1000));
			jobDetail2.setStatus(Status.STOPPED);
			systemJobDetailDao.update(jobDetail2);
		}
		
	}
	
	
	
	@Override
	public void runCron(List<SystemJobDetail> systemJobDetails){
	
		Iterator<SystemJobDetail> jobIterator = systemJobDetails.iterator();
		List<SystemJobDetail> workingJobDetails = new ArrayList<SystemJobDetail>();
		
		try {
			
			for (; jobIterator.hasNext();) {
				
				SystemJobDetail jobDetail2 = jobIterator.next();
				jobDetail2.setLastStartTime(new Date());
				jobDetail2.setStatus(Status.RUNNING);
				systemJobDetailDao.update(jobDetail2);
				workingJobDetails.add(jobDetail2);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
			;
			jobIterator.remove();
		}
		for (SystemJobDetail jobDetail2 : workingJobDetails) {
			Job job = (Job) applicationContext.getBean(jobDetail2.getJobName());
			job.runJob();
		}

		for (SystemJobDetail jobDetail2 : workingJobDetails) {
			jobDetail2.setLastEndTime(new Date());
			jobDetail2.setNextRunTime(new Date(new Date().getTime() + jobDetail2.getInterval() * 60 * 1000));
			jobDetail2.setStatus(Status.STOPPED);
			systemJobDetailDao.update(jobDetail2);
		}
		
	}

}
