package org.sunbird.ruleengine.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.common.util.JobException;
import org.sunbird.ruleengine.common.util.JsonUtil;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.model.ErrorPatternMaster;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.Step;
import org.sunbird.ruleengine.model.StepHistory;
import org.sunbird.ruleengine.model.StepSettings;
import org.sunbird.ruleengine.model.StepUniqueRecord;
import org.sunbird.ruleengine.model.StepHistory.Status;
import org.sunbird.ruleengine.service.IntegrationInstanceFailureService;
import org.sunbird.ruleengine.service.StepHistoryService;
import org.sunbird.ruleengine.service.StepService;
import org.sunbird.ruleengine.service.StepSettingsService;
import org.sunbird.ruleengine.service.StepUniqueRecordService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FreemarkerStepProcessor implements StepProcessor {
	private static final Logger logger = LogManager.getLogger(FreemarkerStepProcessor.class);
	
	@Autowired
	TemplateParser templateParser;

	@Autowired
	IntegrationInstanceFailureService integrationInstanceFailureService;

	@Autowired
	StepHistoryService stepHistoryService;

	@Autowired
	NashornHelper nashornHelper;

	@Autowired
	StepService stepService;
	
	@Autowired
	ClientTranslationHelper clientTranslationHelper;

	@Autowired
	StepSettingsService stepSettingsService;
	
	@Autowired
	StepUniqueRecordService stepUniqueRecordService;

	ObjectMapper mapper = new ObjectMapper();

	
	
	
	@Override
	public Object processStep(JobDetail jobDetail, Step step, ObjectHolder objectHolder, Integer level)
			throws JobException, Exception {

		System.out.println(JsonUtil.toString(objectHolder));
		logger.info(JsonUtil.toString(objectHolder));

		StepSettings stepSettings = new StepSettings();
		stepSettings.setStepId(step.getId());
		stepSettings.setClientId(step.getClientId());
		List<StepSettings> stepSettingsList = stepSettingsService.getListByCriteria(stepSettings, -1, 0);
		/* System.out.println( objectHolder.getobjectHolders().get("0").getValues()); */
		try {
			clientTranslationHelper.fillTranslatorObject(step, stepSettingsList, objectHolder, level);
			System.out.println(JsonUtil.toString(objectHolder));
			logger.info(JsonUtil.toString(objectHolder));
		} catch (TranslatorNotFoundException e) {
			objectHolder.setJobRunFailureCount(
					objectHolder.getJobRunFailureCount() == null ? 1 : objectHolder.getJobRunFailureCount() + 1);
			if (step.isLoggingEnabled()) {
				System.out.println(JsonUtil.toString(objectHolder));
				logger.info(JsonUtil.toString(objectHolder));
				saveStepHistory(step, objectHolder, new Object(), e.getMessage() + " : " + e.getTranslatorCode(),
						new Object(), Status.FAILURE);
				failOverHandler(jobDetail, true, step, objectHolder, null,
						e.getMessage() + " : " + e.getTranslatorCode(), level);
			}
			return null;
		}
		
		
			try {

				if (step.getRequestTemplate() != null && !step.getRequestTemplate().trim().isEmpty()) {
					Object response =  templateParser.parse(step.getClientId().toString(), "Freemaker_Step_Processor_Request_template_" + step.getId().toString(),step.getRequestTemplate(), objectHolder);
					
					if  (response!= null && (step.getSuccessKey() == null ||  step.getSuccessKey().isEmpty() || templateParser.parse(step.getClientId().toString(), "success_key_template_" + step.getId().toString(),step.getSuccessKey(), objectHolder)
							.equals(step.getSuccessValue()))) {
						
						if (step.isDuplicateCheckEnabled()) {
							String stepUniqeKey = templateParser.parse(step.getClientId().toString(),
									"step_unique_key_" + step.getId().toString(), step.getUniqueKeyTemplate(), objectHolder);
							StepUniqueRecord stepUniqueRecord = new StepUniqueRecord();
							stepUniqueRecord.setStepId(step.getId());
							stepUniqueRecord.setUniqueKey(stepUniqeKey);
							stepUniqueRecord.setClientId(step.getClientId());
							stepUniqueRecord.setCreationDate(new Date());
							stepUniqueRecord.setLastUpdateDate(new Date());
							stepUniqueRecordService.save(stepUniqueRecord);
						}
						if (objectHolder.getConfigs().get("0").getValues().containsKey("integrationInstanceFailureId")) {
							IntegrationInstanceFailure integrationInstanceFailure = integrationInstanceFailureService
									.get((BigInteger) objectHolder.getConfigs().get("0").getValues()
											.get("integrationInstanceFailureId"));
							if (step.getId().equals(objectHolder.getConfigs().get("0").getValues().get("failureStepId"))) {
								integrationInstanceFailureService.delete(integrationInstanceFailure.getId());
							}

						}

						if (isItLastStep(step))
							objectHolder.setJobRunSuccessCount(
									objectHolder.getJobRunSuccessCount() == null ? 1 : objectHolder.getJobRunSuccessCount() + 1);
						
						if(step.isLoggingEnabled())
						saveStepHistory(step, objectHolder, JsonUtil.toString(objectHolder), response, null, Status.SUCCESS);
						return response;
					}
					else {
						objectHolder.setJobRunFailureCount(objectHolder.getJobRunFailureCount() == null ? 1
								: objectHolder.getJobRunFailureCount() + 1);
						saveStepHistory(step, objectHolder, JsonUtil.toString(objectHolder), response, null,
								Status.FAILURE);
						failOverHandler(jobDetail, true, step, objectHolder, null, response+"", level);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
				logger.error(MarkerFactory.getMarker("Exception"), e);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				objectHolder.setJobRunFailureCount(
						objectHolder.getJobRunFailureCount() == null ? 1 : objectHolder.getJobRunFailureCount() + 1);
				saveStepHistory(step, objectHolder, JsonUtil.toString(objectHolder), sw.toString(), null, Status.FAILURE);
				failOverHandler(jobDetail, true, step, objectHolder, null, sw.toString(), level);

			}
		return null;
	}

	
	private void saveStepHistory(Step step, ObjectHolder objectHolder, Object request, Object response, Object header,
			StepHistory.Status status) {
		StepHistory stepHistory = new StepHistory();
		// stepHistory.setConfigObjectJson(JsonUtil.toString(objectHolder));

		stepHistory.setRequest(JsonUtil.toString(request));
		if (request != null)
			stepHistory.setRequest(request.toString());
		stepHistory.setResponse(JsonUtil.toString(response));
		stepHistory.setHeader(JsonUtil.toString(header));

		stepHistory.setStatus(status);
		stepHistory.setCreationDate(new Date());
		stepHistory.setLastUpdateDate(new Date());
		stepHistory.setStepId(step.getId());
		stepHistory.setClientId(step.getClientId());
		stepHistory.setJobId(step.getJobDetailId());
		stepHistory.setJobRunId(objectHolder.getJobRunningId());
		stepHistory.setUniqueKeyTemplate(step.getUniqueKeyTemplate());
		stepHistoryService.save(stepHistory);
	}

	private void failOverHandler(JobDetail jobDetail, boolean serverSideExcepiton, Step step, ObjectHolder objectHolder,
			Object repsonseBody, String e, Integer level) throws JobException {

		if (jobDetail.isRetryEnabled()) {
			IntegrationInstanceFailure instanceFailureHistory = new IntegrationInstanceFailure();
			instanceFailureHistory.setJobDetailId(jobDetail.getId());
			Object nextStepRequest = repsonseBody;

			if (step.getErrorFormatterJs() != null && !step.getErrorFormatterJs().isEmpty()) {
				nextStepRequest = nashornHelper.process(step.getErrorFormatterJs(), objectHolder, repsonseBody);
			}
			objectHolder.getConfigs().get("" + level).getValues().put("" + step.getSequence(), nextStepRequest);

			String configObjectJson = null;
			String nextStepRequestJson = null;
			try {
				configObjectJson = mapper.writeValueAsString(objectHolder);
				nextStepRequestJson = mapper.writeValueAsString(nextStepRequest);
				mapper.writeValueAsString(repsonseBody);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}

			if (objectHolder.getConfigs().get("0").getValues().containsKey("integrationInstanceFailureId")) {
				instanceFailureHistory = integrationInstanceFailureService.get((BigInteger) objectHolder.getConfigs()
						.get("0").getValues().get("integrationInstanceFailureId"));
				try {
					if (nextStepRequestJson.equals(
							mapper.writeValueAsString(objectHolder.getConfigs().get("0").getValues().get("0")))) {
						instanceFailureHistory.setNoOfRetries(instanceFailureHistory.getNoOfRetries() + 1);
					} else {
						instanceFailureHistory.setNoOfRetries(0);
					}
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
			}
			instanceFailureHistory.setClientId(step.getClientId());
			instanceFailureHistory.setNextRequestJson(nextStepRequestJson);
			if (!(serverSideExcepiton
					&& objectHolder.getConfigs().get("0").getValues().containsKey("integrationInstanceFailureId"))) {
				instanceFailureHistory.setNextRequestJson(nextStepRequestJson);
			}

			// instanceFailureHistory.setConfigObjectJson(configObjectJson);
			instanceFailureHistory.setCreationDate(new Date());
			instanceFailureHistory.setLastUpdateDate(new Date());
			instanceFailureHistory.setErrorResponse(e);
			ErrorPatternMaster errorPatternMaster = integrationInstanceFailureService
					.getFailureErrorPattern(step.getClientId(), e);
			if (errorPatternMaster != null) {
				instanceFailureHistory.setErrorPatternId(errorPatternMaster.getId());
				instanceFailureHistory.setErrorCode(errorPatternMaster.getErrorCode());
			} else {
				instanceFailureHistory.setErrorCode("Default");
			}

			instanceFailureHistory.setStepId(step.getId());
			instanceFailureHistory.setJobRunId(objectHolder.getJobRunningId());
			integrationInstanceFailureService.update(instanceFailureHistory);
			// kafkaProducerService.sendMessage(instanceFailureHistory,"integrationInstanceFailure");
		}
		// throw new JobException("Error From Rest Step",e);
	}

	public boolean isItLastStep(Step step) {
		BigInteger stepId = stepService.getLastStepId(step.getJobDetailId());
		if (step.getId().compareTo(stepId) == 0)
			return true;
		return false;
	}
	
	
	
	
}
