package org.sunbird.ruleengine.rest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.util.CommonMessages;
import org.sunbird.ruleengine.common.util.JobException;
import org.sunbird.ruleengine.common.util.JsonUtil;
import org.sunbird.ruleengine.contracts.Config;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.contracts.RequestObject;
import org.sunbird.ruleengine.model.AsyncRequest;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.JobRunDetail;
import org.sunbird.ruleengine.model.Step;
import org.sunbird.ruleengine.model.AsyncRequest.Status;
import org.sunbird.ruleengine.model.JobDetail.IntervalType;
import org.sunbird.ruleengine.model.JobDetail.TimeIntervalType;
import org.sunbird.ruleengine.service.AsyncRequestService;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.GenericCron;
import org.sunbird.ruleengine.service.IntegrationInstanceFailureService;
import org.sunbird.ruleengine.service.JobDetailService;
import org.sunbird.ruleengine.service.JobRunDetailService;
import org.sunbird.ruleengine.service.StepService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sunbird.ruleengine.common.Response;

@RestController
@RequestMapping("{clientCode}/invoke")
public class JobInvoker {
	private static final Logger logger = LogManager.getLogger(JobInvoker.class);
	@Autowired
	GenericCron genericCron;

	@Autowired
	JobDetailService jobDetailService;

	@Autowired
	StepService stepService;

	@Autowired
	AsyncRequestService asyncRequestService;

	@Autowired
	IntegrationInstanceFailureService integrationInstanceFailureService;

	@Autowired
	ClientService clientService;

	@Autowired
	JobRunDetailService jobRunDetailService;

	@RequestMapping()
	public String invoke() {
		genericCron.startCron();
		return "ok";
	}

	@RequestMapping(value = "/{id}")
	public String invoke(@PathVariable("clientCode") String clientCode, @PathVariable("id") BigInteger id)
			throws Exception {
		System.out.println("Cron Started");
		logger.info("Cron Started");
		JobDetail jobDetail = new JobDetail();
		jobDetail.setId(id);
		jobDetail.setStatus(JobDetail.Status.STOPPED);

		List<JobDetail> jobDetails = jobDetailService.getListByCriteria(jobDetail, -1, 0);
		genericCron.processJobDetails(jobDetails);
		genericCron.startCron();
		return "ok";
	}

	@RequestMapping(value = "retry/{id}")
	public ResponseEntity<Void> retry(@PathVariable("clientCode") String clientCode, @PathVariable("id") BigInteger id)
			throws Exception {
		IntegrationInstanceFailure integrationInstanceFailure = integrationInstanceFailureService.get(id);
		Step olderSep = stepService.get(integrationInstanceFailure.getStepId());
		JobDetail jobDetail = jobDetailService.get(olderSep.getFailureJobDetailId());
		Step stepSeearch = new Step();
		stepSeearch.setJobDetailId(olderSep.getFailureJobDetailId());
		stepSeearch.setParentNull(true);
		Map<String, Boolean> orderBy = new HashMap<>();
		orderBy.put("sequence", Boolean.TRUE);
		List<Step> steps = stepService.getListByCriteria(stepSeearch, 0, 10, orderBy, false, null);
		ObjectHolder objectHolder = new ObjectHolder();
		objectHolder.getConfigs().put("0", new Config());
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectHolder.getConfigs().get("0").getValues().put("0",
					objectMapper.readValue(integrationInstanceFailure.getNextRequestJson(), Object.class));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception"), e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception"), e);
		}
		objectHolder.getConfigs().get("0").getValues().put("integrationInstanceFailureId", id);
		objectHolder.getConfigs().get("0").getValues().put("failureStepId", olderSep.getFailureStepId());
		for (Step step : steps) {
			try {
				Object object = genericCron.processStep(jobDetail, step, objectHolder, 0);
				if (object == null) {
					break;
				}
			} catch (JobException e) {
				return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "test", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
	public String test() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Response>\r\n    <Result>1</Result>\r\n    <Reason/>\r\n</Response>";
	}

	@RequestMapping(value = "job-async/{id}")
	public ResponseEntity<Void> jobAsync(@PathVariable("clientCode") String clientCode,
			@PathVariable("id") BigInteger jobId, @RequestBody LinkedHashMap<?, ?> body) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		AsyncRequest asyncRequest = new AsyncRequest();
		try {
			asyncRequest.setRequestJson(objectMapper.writeValueAsString(body));
		} catch (JsonProcessingException e) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		asyncRequest.setJobDetailId(jobId);
		asyncRequest.setStatus(Status.PENDING);
		asyncRequest.setCreationDate(new Date());
		asyncRequest.setLastUpdateDate(new Date());
		asyncRequestService.save(asyncRequest);
		genericCron.processAsyncRequest(asyncRequest, jobId, body);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "dry-run/{id}/{stepId}")
	public ResponseEntity<ObjectHolder> dryRun(@PathVariable("clientCode") String clientCode,
			@PathVariable("stepId") BigInteger stepId, @PathVariable("id") BigInteger jobId,
			@RequestParam(value = "uniqueKey", required = false) String uniqueKey,
			@ModelAttribute LinkedHashMap<?, ?> body) throws Exception {
		Step stepSeearch = new Step();
		stepSeearch.setJobDetailId(jobId);
		stepSeearch.setParentNull(true);
		Map<String, Boolean> orderBy = new HashMap<>();
		orderBy.put("sequence", Boolean.TRUE);
		JobDetail jobDetail = jobDetailService.get(jobId);
		List<Step> steps = stepService.getListByCriteria(stepSeearch, 0, 10, orderBy, false, null);
		ObjectHolder objectHolder = new ObjectHolder();
		objectHolder.setDryRun(true);
		// objectHolder.setJobRunningId(UUID.randomUUID());
		objectHolder.getConfigs().put("0", new Config());
		objectHolder.getConfigs().get("0").getValues().put("0", body);
		objectHolder.getConfigs().get("0").getValues().put("dryRunStepId", body);
		if (uniqueKey != null) {
			objectHolder.getConfigs().get("0").getValues().put("dryRunStepUniqueId", uniqueKey);
		}
		for (Step step : steps) {
			try {
				genericCron.processStep(jobDetail, step, objectHolder, 0);
			} catch (JobException e) {
				e.printStackTrace();
				logger.error(MarkerFactory.getMarker("Exception"), e);
				return new ResponseEntity<ObjectHolder>(objectHolder, HttpStatus.OK);
			}
		}

		return new ResponseEntity<ObjectHolder>(objectHolder, HttpStatus.OK);
	}

	@RequestMapping(value = "job-sync")
	public @ResponseBody Response<Object> jobSync(@PathVariable("clientCode") String clientCode,
			@RequestBody RequestObject requestObject) throws Exception {

		JobDetail criteria = new JobDetail();
		BigInteger clientId = clientService.getClientIdByCode(clientCode);
		if (clientId == null)
			return new Response<Object>(false, null, CommonMessages.CLIENT_CODE_DOESNOT_EXISTS);
		criteria.setClientId(clientId);
		if (requestObject.getJobCode().isEmpty() || requestObject.getJobCode() == null)
			return new Response<Object>(false, null, CommonMessages.EMPTY_JOB_CODE);
		criteria.setJobCode(requestObject.getJobCode());
		List<JobDetail> list = jobDetailService.getListByCriteria(criteria, -1, 0);
		if (list.size() == 0)
			return new Response<Object>(false, null, CommonMessages.JOB_DOESNOT_EXISTS);
		JobDetail jobDetail = list.get(0);

		Step stepSeearch = new Step();
		stepSeearch.setJobDetailId(jobDetail.getId());
		stepSeearch.setParentNull(true);
		Map<String, Boolean> orderBy = new HashMap<>();
		orderBy.put("sequence", Boolean.TRUE);
		List<Step> steps = stepService.getListByCriteria(stepSeearch, 0, 10, orderBy, false, null);
		ObjectHolder objectHolder = new ObjectHolder();

		JobRunDetail jobRunDetail = new JobRunDetail();
		jobRunDetail.setJobDetailId(jobDetail.getId());
		jobRunDetail.setJobName(jobDetail.getJobName());
		jobRunDetail.setStartTime(new Date());
		jobRunDetail.setCreationDate(new Date());
		jobRunDetail.setClientId(jobDetail.getClientId());
		JobRunDetail savedjobRunDetail = jobRunDetailService.update(jobRunDetail);
		objectHolder.setJobRunningId(savedjobRunDetail.getId());
		objectHolder.setJobRunFailureCount(0);
		objectHolder.setJobRunSuccessCount(0);
		objectHolder.getConfigs().put("0", new Config());
		objectHolder.getConfigs().get("0").getValues().put("0", requestObject.getData());
		System.out.println(JsonUtil.toString(objectHolder));
		Object lastResponse = null;
		for (Step step : steps) {
			try {
				lastResponse = genericCron.processStep(jobDetail, step, objectHolder, 0);
				if (lastResponse == null) {
					updateJobRunDetail(savedjobRunDetail.getId(), objectHolder);
					return new Response<Object>(false, lastResponse, CommonMessages.SOME_ERROR_OCCURED);
				}
				//System.out.println("Deepesh Jain Object Value" + JsonUtil.toString(lastResponse));
				//objectHolder.getConfigs().get("0").getValues().put("0", lastResponse.getObject());
			} catch (JobException e) {
				updateJobRunDetail(savedjobRunDetail.getId(), objectHolder);
				return new Response<Object>(false, e);
			}
		}
		updateJobRunDetail(savedjobRunDetail.getId(), objectHolder);
		return new Response<Object>(true, lastResponse);
	}

	@RequestMapping(value = "heartBeat", method = RequestMethod.POST)
	public @ResponseBody Response<Object> heartBeat(@PathVariable("clientCode") String clientCode) throws Exception {
		JobDetail criteria = new JobDetail();
		BigInteger clientId = clientService.getClientIdByCode(clientCode);
		if (clientId == null)
			return new Response<Object>(false, null, CommonMessages.CLIENT_CODE_DOESNOT_EXISTS);
		criteria.setClientId(clientId);
		criteria.setToDateTime(new Date());
		criteria.setStatus(JobDetail.Status.STOPPED);
		criteria.setAgentJob(true);
		List<JobDetail> list = jobDetailService.getListByCriteria(criteria, 0, 1);
		if (list.size() == 0)
			return new Response<Object>(true, null);
		JobDetail jobDetail = list.get(0);
		jobDetail.setLastStartTime(new Date());
		jobDetail.setStatus(JobDetail.Status.RUNNING);
		jobDetailService.update(jobDetail);

		ObjectHolder objectHolder = new ObjectHolder();
		objectHolder.setLastScheduledStartTime(jobDetail.getLastScheduledStartTime());
		objectHolder.setNextRunTime(jobDetail.getNextRunTime());

		JobRunDetail jobRunDetail = new JobRunDetail();
		jobRunDetail.setJobDetailId(jobDetail.getId());
		jobRunDetail.setJobName(jobDetail.getJobName());
		jobRunDetail.setStartTime(new Date());
		jobRunDetail.setCreationDate(new Date());
		jobRunDetail.setClientId(jobDetail.getClientId());
		JobRunDetail savedjobRunDetail = jobRunDetailService.update(jobRunDetail);
		objectHolder.setJobRunningId(savedjobRunDetail.getId());
		objectHolder.setJobRunFailureCount(0);
		objectHolder.setJobRunSuccessCount(0);
		objectHolder.getConfigs().put("0", new Config());

		System.out.println(JsonUtil.toString(objectHolder));
		Object lastResponse = null;

		Step stepSeearch = new Step();
		stepSeearch.setJobDetailId(jobDetail.getId());
		stepSeearch.setParentNull(true);
		stepSeearch.setBeforeAgentCall(true);
		Map<String, Boolean> orderBy = new HashMap<>();
		orderBy.put("sequence", Boolean.TRUE);
		List<Step> steps = stepService.getListByCriteria(stepSeearch, 0, 10, orderBy, false, null);
		for (Step step : steps) {
			try {
				lastResponse = genericCron.processStep(jobDetail, step, objectHolder, 0);
				if (lastResponse == null) {
					updateJobRunDetail(savedjobRunDetail.getId(), objectHolder);
					updateJobDetailToStopped(jobDetail);
					break;
				}
			} catch (JobException e) {
				updateJobRunDetail(savedjobRunDetail.getId(), objectHolder);
				updateJobDetailToStopped(jobDetail);
				e.printStackTrace();
				break;
			}
		}
		return new Response<Object>(true, lastResponse);
	}

	@RequestMapping(value = "afterAgent", method = RequestMethod.POST)
	public @ResponseBody Response<Object> afterAgent(@PathVariable("clientCode") String clientCode,
			@RequestBody RequestObject requestObject) throws Exception {
		JobDetail criteria = new JobDetail();
		BigInteger clientId = clientService.getClientIdByCode(clientCode);
		if (clientId == null)
			return new Response<Object>(false, null, CommonMessages.CLIENT_CODE_DOESNOT_EXISTS);
		criteria.setClientId(clientId);
		if (requestObject.getJobCode().isEmpty() || requestObject.getJobCode() == null)
			return new Response<Object>(false, null, CommonMessages.EMPTY_JOB_CODE);
		criteria.setJobCode(requestObject.getJobCode());
		criteria.setAgentJob(true);
		List<JobDetail> list = jobDetailService.getListByCriteria(criteria, 0, 1);
		if (list.size() == 0)
			return new Response<Object>(true, null);
		JobDetail jobDetail = list.get(0);

		ObjectHolder objectHolder = new ObjectHolder();
		JobRunDetail savedjobRunDetail = jobRunDetailService.get(requestObject.getJobRunId());// jobRunningId
		objectHolder.setJobRunningId(savedjobRunDetail.getId());
		objectHolder.setJobRunFailureCount(0);
		objectHolder.setJobRunSuccessCount(0);
		objectHolder.getConfigs().put("0", new Config());
		objectHolder.getConfigs().get("0").getValues().put("0", requestObject.getData());

		System.out.println(JsonUtil.toString(objectHolder));
		Object lastResponse = null;
		Step stepSeearch = new Step();
		stepSeearch.setJobDetailId(jobDetail.getId());
		stepSeearch.setParentNull(true);
		stepSeearch.setBeforeAgentCall(false);
		Map<String, Boolean> orderBy = new HashMap<>();
		orderBy.put("sequence", Boolean.TRUE);
		List<Step> steps = stepService.getListByCriteria(stepSeearch, 0, 10, orderBy, false, null);
		for (Step step : steps) {
			try {
				lastResponse = genericCron.processStep(jobDetail, step, objectHolder, 0);
				if (lastResponse == null) {
					break;
				}
			} catch (JobException e) {
				e.printStackTrace();
				break;
			}
		}
		updateJobRunDetail(savedjobRunDetail.getId(), objectHolder);
		updateJobDetailToStopped(jobDetail);
		return new Response<Object>(true, lastResponse);
	}

	public void updateJobDetailToStopped(JobDetail jobDetail) {
		jobDetail.setStatus(JobDetail.Status.STOPPED);
		jobDetail.setLastEndTime(new Date());
		jobDetail.setLastScheduledStartTime(jobDetail.getNextRunTime());
		if (jobDetail.getTimeIntervalType() != null && jobDetail.getTimeIntervalType() == TimeIntervalType.MONTHLY) {
			Calendar cal = Calendar.getInstance();
			if (jobDetail.getIntervalType() == IntervalType.INTERVAL_AFTER_LAST_FINISH) {
				cal.add(Calendar.MONTH, 1);
			} else {
				cal.setTime(jobDetail.getNextRunTime());
				cal.add(Calendar.MONTH, 1);
			}
			jobDetail.setNextRunTime(new Date(cal.getTimeInMillis()));
		} else {

			if (jobDetail.getIntervalType() == IntervalType.INTERVAL_AFTER_LAST_FINISH) {
				jobDetail.setNextRunTime(new Date(System.currentTimeMillis() + jobDetail.getInterval() * 60 * 1000));
			} else {
				jobDetail.setNextRunTime(
						new Date(jobDetail.getNextRunTime().getTime() + jobDetail.getInterval() * 60 * 1000));
			}

		}
		jobDetailService.update(jobDetail);
	}

	@RequestMapping(value = "retryAll")
	public ResponseEntity<List<IntegrationInstanceFailure>> retryAll(@PathVariable("clientCode") String clientCode,
			@RequestParam(required = false) List<BigInteger> stepIdList) {
		for (BigInteger stepId : stepIdList) {
			try {
				retry(clientCode, stepId);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public void updateJobRunDetail(BigInteger id, ObjectHolder objectHolder) {
		JobRunDetail updatedjobRunDetail = jobRunDetailService.get(id);
		updatedjobRunDetail.setEndTime(new Date());
		updatedjobRunDetail.setSuccessCount(BigInteger.valueOf(objectHolder.getJobRunSuccessCount()));
		updatedjobRunDetail.setFailureCount(BigInteger.valueOf(objectHolder.getJobRunFailureCount()));
		jobRunDetailService.update(updatedjobRunDetail);
	}

}
