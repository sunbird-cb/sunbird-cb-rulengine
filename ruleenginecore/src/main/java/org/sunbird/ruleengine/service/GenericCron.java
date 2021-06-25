package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.common.util.JobException;
import org.sunbird.ruleengine.common.util.JsonUtil;
import org.sunbird.ruleengine.contracts.Config;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.dao.JobSettingsDao;
import org.sunbird.ruleengine.dao.StepDAO;
import org.sunbird.ruleengine.helper.StepHelper;
import org.sunbird.ruleengine.model.AsyncRequest;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.JobRunDetail;
import org.sunbird.ruleengine.model.JobSettings;
import org.sunbird.ruleengine.model.Step;
import org.sunbird.ruleengine.model.JobDetail.IntervalType;
import org.sunbird.ruleengine.model.JobDetail.Status;
import org.sunbird.ruleengine.model.JobDetail.TimeIntervalType;
import org.sunbird.ruleengine.model.Step.Result;

@Component
public class GenericCron implements ApplicationContextAware {
	 private static final Logger logger = LogManager.getLogger(GenericCron.class);

	@Autowired
	JobDetailService jobDetailService;

	ApplicationContext applicationContext;

	@Autowired
	AsyncRequestService asyncRequestService;
	
	@Autowired
	JobSettingsDao jobSettingsDao;
	
	@Autowired
	JobSettingsService jobSettingsService;

	@Autowired
	StepDAO stepDao;

	@Autowired
	StepHelper stepHelper;
	
	@Autowired
	JobRunDetailService jobRunDetailService;
	
	@Autowired
	StepSettingsService stepSettingsService;
	/*@Autowired
	KafkaProducerService kafkaProducerService;*/
	

 public ApplicationContext getContext() {
        return applicationContext;
    }

	@Scheduled(fixedDelay = 1 * 15 * 1000)
	@Async
	public void startCron() {
		try {
			System.out.println("Cron Started");
			logger.info("Cron Started");
			JobDetail jobDetail = new JobDetail();
			jobDetail.setToDateTime(new Date());
			jobDetail.setStatus(JobDetail.Status.STOPPED);
			jobDetail.setAgentJob(false);
			Map<String, Boolean> orderBy = new HashMap<>();
			orderBy.put("nextRunTime", Boolean.TRUE);
			List<JobDetail> jobDetails = jobDetailService.getListByCriteria(jobDetail, 0, 1, orderBy, false, null);
					//getListByCriteria(jobDetail, 0, 1);
			processJobDetails(jobDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}

	}

	public void processJobDetails(List<JobDetail> jobDetails) throws Exception {
		Iterator<JobDetail> jobIterator = jobDetails.iterator();
		List<JobDetail> workingJobDetails = new ArrayList<JobDetail>();
		try {
			while (jobIterator.hasNext()) {
				JobDetail jobDetail2 = jobIterator.next();
				jobDetail2.setLastStartTime(new Date());
				jobDetail2.setStatus(Status.RUNNING);
				workingJobDetails.add(jobDetailService.update(jobDetail2));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
			jobIterator.remove();
		}
		for (JobDetail workingJobDetail : workingJobDetails) {
			
			ObjectHolder objectHolder = new ObjectHolder();
			objectHolder.setLastScheduledStartTime(workingJobDetail.getLastScheduledStartTime());
			objectHolder.setNextRunTime(workingJobDetail.getNextRunTime());
			
			
			if (jobSettingsService.equal(workingJobDetail.getClientId(), workingJobDetail.getId(), "RUN_JOB_AGAIN_ENABLED","YES"))
			{
				JobSettings jobSettingsearch = new JobSettings();
				jobSettingsearch.setJobDetailId(workingJobDetail.getId());
				jobSettingsearch.setClientId(workingJobDetail.getClientId());
				List<JobSettings> jobSettingsList = jobSettingsDao.getListByCriteria(jobSettingsearch, 0, 10,null,false,null);
				
					for (JobSettings jobSetting : jobSettingsList) {
						if(jobSetting.getKey().equalsIgnoreCase("JOB_START_SEQUENCE"))
						{
							objectHolder.setStartSequence(jobSetting.getValue());
						}
						if(jobSetting.getKey().equalsIgnoreCase("JOB_BATCH_SIZE"))
						{
							objectHolder.setBatchSize(jobSetting.getValue());
						}
				}
					if(!jobSettingsList.isEmpty())
					{
						objectHolder.setEndSequence(Integer.parseInt(objectHolder.getStartSequence())+Integer.parseInt(objectHolder.getBatchSize())-1+"");
					}
			}
			
			
			do {
				JobRunDetail jobRunDetail = new JobRunDetail();
				jobRunDetail.setJobDetailId(workingJobDetail.getId());
				jobRunDetail.setJobName(workingJobDetail.getJobName());
				jobRunDetail.setStartTime(new Date());
				jobRunDetail.setCreationDate(new Date());
				jobRunDetail.setClientId(workingJobDetail.getClientId());
				JobRunDetail savedjobRunDetail=jobRunDetailService.update(jobRunDetail);
				objectHolder.setJobRunningId(savedjobRunDetail.getId());
				objectHolder.setJobRunFailureCount(0);
				objectHolder.setJobRunSuccessCount(0);
				objectHolder.getConfigs().put("0", new Config());
				objectHolder.setRunAgain(false);
				
				/*if (objectHolder.isRunAgain()==false )
				{   //Send Jobstart mail notification by Kafka
					MessageHolder messageHolder = new MessageHolder();
					messageHolder.setJobRunId(savedjobRunDetail.getId());
					messageHolder.setStatus("STOPPED_TO_RUNNING");
					kafkaProducerService.sendMessage(messageHolder,"test");
				}*/
				
				Step stepSeearch = new Step();
				stepSeearch.setJobDetailId(workingJobDetail.getId());
				stepSeearch.setParentNull(true);
				Map<String, Boolean> orderBy = new HashMap<>();
				orderBy.put("sequence", Boolean.TRUE);
				List<Step> steps = stepDao.getListByCriteria(stepSeearch, 0, 10, orderBy, false, null);
				
				for (Step step : steps) {

					try {
						Object response = processStep(workingJobDetail, step, objectHolder, 0);
						if (response == null) {
							break;
						}
					} catch (JobException e) {
						e.printStackTrace();
						break;
					}

				}
				
				if(objectHolder.isRunAgain())
				{
					objectHolder.setStartSequence(Integer.parseInt(objectHolder.getEndSequence())+1+"");
					objectHolder.setEndSequence(Integer.parseInt(objectHolder.getEndSequence())+Integer.parseInt(objectHolder.getBatchSize())+"");
				}
				JobRunDetail updatedjobRunDetail=jobRunDetailService.get(savedjobRunDetail.getId());
				updatedjobRunDetail.setEndTime(new Date());
				updatedjobRunDetail.setSuccessCount(BigInteger.valueOf(objectHolder.getJobRunSuccessCount()));
				updatedjobRunDetail.setFailureCount(BigInteger.valueOf(objectHolder.getJobRunFailureCount()));
				jobRunDetailService.update(updatedjobRunDetail);
				/*if (objectHolder.isRunAgain()==false )
				{   //Send JobEnd mail notification by Kafka
					MessageHolder messageHolder = new MessageHolder();
					messageHolder.setJobRunId(savedjobRunDetail.getId());
					messageHolder.setStatus("RUNNING_TO_STOPPED");
					kafkaProducerService.sendMessage(messageHolder,"test");
				}*/
			} while (objectHolder.isRunAgain());
		}

		for (JobDetail workingJobDetail : workingJobDetails) {
			workingJobDetail.setLastEndTime(new Date());
			workingJobDetail.setLastScheduledStartTime(workingJobDetail.getNextRunTime());
			if (workingJobDetail.getTimeIntervalType()!=null &&  workingJobDetail.getTimeIntervalType() == TimeIntervalType.MONTHLY) {
				Calendar cal = Calendar.getInstance();
				if (workingJobDetail.getIntervalType() == IntervalType.INTERVAL_AFTER_LAST_FINISH) {
					cal.add(Calendar.MONTH, 1);
				} else {
					cal.setTime(workingJobDetail.getNextRunTime());
					cal.add(Calendar.MONTH, 1);
				}
				workingJobDetail.setNextRunTime(new Date(cal.getTimeInMillis()));
			} else {

				if (workingJobDetail.getIntervalType() == IntervalType.INTERVAL_AFTER_LAST_FINISH) {
					workingJobDetail.setNextRunTime(
							new Date(System.currentTimeMillis() + workingJobDetail.getInterval() * 60 * 1000));
				} else {
					workingJobDetail.setNextRunTime(new Date(
							workingJobDetail.getNextRunTime().getTime() + workingJobDetail.getInterval() * 60 * 1000));
				}

			}
			workingJobDetail.setStatus(Status.STOPPED);
			jobDetailService.update(workingJobDetail);
		}
		System.out.println("JOB_end_time"+ new Date());
		logger.info("JOB_end_time"+ new Date());
	}

	@Async
	public void processAsyncRequest(AsyncRequest asyncRequest, BigInteger jobId, LinkedHashMap<?, ?> body) throws Exception {
		asyncRequest.setStatus(AsyncRequest.Status.RUNNING);
		asyncRequest.setRunStartTime(new Date());
		Step stepSeearch = new Step();
		stepSeearch.setJobDetailId(jobId);
		stepSeearch.setParentNull(true);
		Map<String, Boolean> orderBy = new HashMap<>();
		orderBy.put("sequence", Boolean.TRUE);
		JobDetail jobDetail = jobDetailService.get(jobId);
		List<Step> steps = stepDao.getListByCriteria(stepSeearch, 0, 10, orderBy, false, null);
		ObjectHolder objectHolder = new ObjectHolder();
		objectHolder.getConfigs().put("0", new Config());
		objectHolder.getConfigs().get("0").getValues().put("0", body);
		for (Step step : steps) {
			{
				try {
					processStep(jobDetail, step, objectHolder, 0);
				} catch (JobException e) {
					asyncRequest.setStatus(AsyncRequest.Status.FAILURE);
					break;
				}
			}

		}
		if (asyncRequest.getStatus() != AsyncRequest.Status.FAILURE) {
			asyncRequest.setStatus(AsyncRequest.Status.FAILURE);
		}
		asyncRequestService.update(asyncRequest);
	}

	public Object processStep(JobDetail jobDetail, Step step, ObjectHolder objectHolder, Integer level)
			throws Exception {
		Object object = stepHelper.processStep(jobDetail, step, objectHolder, level);
		objectHolder.getConfigs().get(level.toString()).getValues().put(step.getSequence().toString(), object);
		System.out.println(JsonUtil.toString(objectHolder));
		logger.info(JsonUtil.toString(objectHolder));
		Step stepSeearch = new Step();
		stepSeearch.setJobDetailId(step.getJobDetailId());
		stepSeearch.setParentId(step.getId());
		Map<String, Boolean> orderBy = new HashMap<>();
		orderBy.put("sequence", Boolean.TRUE);
		List<Step> steps = stepDao.getListByCriteria(stepSeearch, 0, 10, orderBy, false, null);
		if (object != null && !object.equals("[]")  ) {
			if (step.getResult() == Result.LIST) {
				Config innerConfig = new Config();
				objectHolder.getConfigs().put(new Integer(level + 1).toString(), innerConfig);
				//for (Object currentValue : (List<?>) object) {
					for(int i = 0;i< ((List<?>) object).size();i++) {
						Object currentValue = ((List<?>) object).get(i);
						innerConfig.setCurrentResponse(currentValue);
					for (Step inner : steps) {
						Object innerResult =	processStep(jobDetail, inner, objectHolder, level + 1);
						if(stepSettingsService.equal(jobDetail.getClientId(), inner.getId(), "LOG_ALL_CHILD_STEPS", "YES"))
						objectHolder.getConfigs().get(new Integer(level + 1).toString()).getValues().put(inner.getSequence().toString().concat("-"+i), innerResult);
					}
				}
			} else {
				for (Step inner : steps) {
					Config innerConfig = new Config();
					objectHolder.getConfigs().put(new Integer(level + 1).toString(), innerConfig);
					innerConfig.setCurrentResponse(object);
					processStep(jobDetail, inner, objectHolder, level + 1);
				}
			}
		}

		return object;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
