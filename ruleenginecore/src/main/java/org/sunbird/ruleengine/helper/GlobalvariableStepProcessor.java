package org.sunbird.ruleengine.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import org.sunbird.ruleengine.model.StepHistory.Status;
import org.sunbird.ruleengine.service.IntegrationInstanceFailureService;
import org.sunbird.ruleengine.service.JobSettingsService;
import org.sunbird.ruleengine.service.StepHistoryService;
import org.sunbird.ruleengine.service.StepService;
import org.sunbird.ruleengine.service.StepSettingsService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GlobalvariableStepProcessor implements StepProcessor {
	private static final Logger logger = LogManager.getLogger(NashornStepProcessor.class);

	@Autowired
	TemplateParser templateParser;

	@Autowired
	IntegrationInstanceFailureService integrationInstanceFailureService;

	@Autowired
	StepHistoryService stepHistoryService;
	
	@Autowired
	JobSettingsService jobSettingsService;

	@Autowired
	NashornHelper nashornHelper;
	
	@Autowired
	Map<String, GlobalVariableHandlers> globalVariableHandler = new HashMap<>();

	@Autowired
	StepService stepService;

	@Autowired
	StepSettingsService stepSettingsService;

	ObjectMapper mapper = new ObjectMapper();

	@Override
	public Object processStep(JobDetail jobDetail, Step step, ObjectHolder config, Integer level)
			throws JobException, Exception {

		try {
			
			
			Map values = globalVariableHandler.get(step.getSubType() + "Handler").processVariable(step,config, level);
			
			
			if ((boolean) values.get("success")) {
				Object response = values.get("response");
				if (step.getResponseFormatterJs() != null && !step.getResponseFormatterJs().isEmpty()) {
					response = nashornHelper.process(step.getResponseFormatterJs(), config, values.get("response"));
				}
				if (isItLastStep(step))
					config.setJobRunSuccessCount(
							config.getJobRunSuccessCount() == null ? 1 : config.getJobRunSuccessCount() + 1);
				if (step.isLoggingEnabled())
					saveStepHistory(step, config, JsonUtil.toString(values.get("request")), values.get("response"),
							null, Status.SUCCESS);
				return response;

			} else {

				saveStepHistory(step, config, JsonUtil.toString(values.get("request")), values.get("response"), "",
						Status.FAILURE);
				failOverHandler(jobDetail, true, step, config, values.get("request"), values.get("response").toString(),
						level);
				config.setJobRunFailureCount(
						config.getJobRunFailureCount() == null ? 1 : config.getJobRunFailureCount() + 1);
				return null;

			}

		}
		catch (Throwable e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception"), e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			String string = new String(e.toString());
			System.out.println(string);
			e.printStackTrace(pw);
			saveStepHistory(step, config, JsonUtil.toString(config), sw.toString(), null, Status.FAILURE);
			failOverHandler(jobDetail, true, step, config, null, sw.toString(), level);
			config.setJobRunFailureCount(
					config.getJobRunFailureCount() == null ? 1 : config.getJobRunFailureCount() + 1);

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
