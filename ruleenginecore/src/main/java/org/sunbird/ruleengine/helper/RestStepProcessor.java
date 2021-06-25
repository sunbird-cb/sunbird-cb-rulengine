package org.sunbird.ruleengine.helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.sunbird.ruleengine.common.util.JobException;
import org.sunbird.ruleengine.common.util.JsonUtil;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.contracts.OipCache;
import org.sunbird.ruleengine.dao.JobSettingsDao;
import org.sunbird.ruleengine.model.ErrorPatternMaster;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.JobSettings;
import org.sunbird.ruleengine.model.Step;
import org.sunbird.ruleengine.model.StepHistory;
import org.sunbird.ruleengine.model.StepRequestValidator;
import org.sunbird.ruleengine.model.StepSettings;
import org.sunbird.ruleengine.model.StepUniqueRecord;
import org.sunbird.ruleengine.model.StepHistory.Status;
import org.sunbird.ruleengine.model.StepRequestValidator.ResultFlag;
import org.sunbird.ruleengine.service.IntegrationInstanceFailureService;
import org.sunbird.ruleengine.service.JobRunDetailService;
import org.sunbird.ruleengine.service.JobSettingsService;
import org.sunbird.ruleengine.service.StepHistoryService;
import org.sunbird.ruleengine.service.StepRequestValidatorService;
import org.sunbird.ruleengine.service.StepService;
import org.sunbird.ruleengine.service.StepSettingsService;
import org.sunbird.ruleengine.service.StepUniqueRecordService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestStepProcessor implements StepProcessor {
	private static final Logger logger = LogManager.getLogger(RestStepProcessor.class);
	@Autowired
	TemplateParser templateParser;

	@Autowired
	JobSettingsService jobSettingsService;

	@Autowired
	IntegrationInstanceFailureService integrationInstanceFailureService;

	@Autowired
	StepHistoryService stepHistoryService;

	@Autowired
	NashornHelper nashornHelper;

	@Autowired
	StepUniqueRecordService stepUniqueRecordService;

	@Autowired
	ClientTranslationHelper clientTranslationHelper;

	@Autowired
	StepSettingsService stepSettingsService;

	@Autowired
	StepRequestValidatorService stepRequestValidatorService;

	@Autowired
	StepService stepService;

	@Autowired
	JobRunDetailService jobRunDetailService;

	@Autowired
	JobSettingsDao jobSettingsDAO;

	/*
	 * @Autowired KafkaProducerService kafkaProducerService;
	 */
	ObjectMapper mapper = new ObjectMapper();
	
	OipCache<String,Object> oipCache = new OipCache<String,Object>(500,500,10);
	
	@SuppressWarnings("unchecked")
	@Override
	public Object processStep(JobDetail jobDetail, Step step, ObjectHolder objectHolder, Integer level)
			throws Exception {
		System.out.println(JsonUtil.toString(objectHolder));
		logger.info(JsonUtil.toString(objectHolder));
		if (step.getDryRunResponse() == null || step.getDryRunResponse().isEmpty() || !objectHolder.isDryRun()) {
			RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory(jobDetail));
			Object genericRequest = new Object();
			Object genericReponse = new Object();
			Object genericHeader = new Object();
			Object entityResponse = null;
			Object entityHeader = null;
			ResponseEntity<?> object = null;

			StepSettings stepSettings = new StepSettings();
			stepSettings.setStepId(step.getId());
			stepSettings.setClientId(step.getClientId());
			List<StepSettings> stepSettingsList = stepSettingsService.getListByCriteria(stepSettings, -1, 0);
			/* System.out.println( objectHolder.getConfigs().get("0").getValues()); */
			
			
			//cache memory code
			List<StepSettings> stepSettingsListForCache = stepSettingsService
					.getStepSettings(step.getClientId(), step.getId(), "CACHE_KEY");
			if ((!stepSettingsListForCache.isEmpty()) && jobSettingsService.equal(jobDetail.getClientId(), jobDetail.getId(),
					"ENABLE_CACHE", "YES")) {

				Object cacheData  = oipCache.get(templateParser.parse(step.getClientId().toString(), "step_" + step.getId().toString(),
						stepSettingsService.getValue(step.getClientId(), step.getId(), "CACHE_KEY"), objectHolder));
				if(cacheData!=null) {
					
					
					System.out.println(JsonUtil.toString(cacheData));
					logger.info(JsonUtil.toString(cacheData));
					if (step.isLoggingEnabled()) {
						saveStepHistory(step, objectHolder, "", cacheData, "",
								Status.SUCCESS);
					}
					if (step.getResponseFormatterJs() != null && !step.getResponseFormatterJs().trim().isEmpty()) {
						Object response = "";
						if (stepSettingsService.equal(jobDetail.getClientId(), step.getId(), "GET_DATA_FROM_HEADER",
								"YES"))
							response = nashornHelper.process(step.getResponseFormatterJs(), objectHolder, cacheData);
						else
							response = nashornHelper.process(step.getResponseFormatterJs(), objectHolder,
									cacheData);
						if (jobSettingsService.equal(jobDetail.getClientId(), jobDetail.getId(),
								"RUN_JOB_AGAIN_ENABLED", "YES") && response != null
								&& (!response.toString().equals("[]"))) {
							objectHolder.setRunAgain(true);
						} else {
							objectHolder.setRunAgain(false);
						}
						return response;
					}
		
			         return cacheData;
					
				}

			}
			
			
			try {
				clientTranslationHelper.fillTranslatorObject(step, stepSettingsList, objectHolder, level);
				System.out.println(JsonUtil.toString(objectHolder));
				logger.info(JsonUtil.toString(objectHolder));
			} catch (TranslatorNotFoundException e) {
				objectHolder.setJobRunFailureCount(
						objectHolder.getJobRunFailureCount() == null ? 1 : objectHolder.getJobRunFailureCount() + 1);
					System.out.println(JsonUtil.toString(objectHolder));
					logger.info(JsonUtil.toString(objectHolder));
					saveStepHistory(step, objectHolder, genericRequest, e.getMessage() + " : " + e.getTranslatorCode(),
							genericHeader, Status.FAILURE);
					failOverHandler(jobDetail, true, step, objectHolder, null,
							e.getMessage() + " : " + e.getTranslatorCode(), level);
				
				return null;
			}
			try {

				if (step.isDuplicateCheckEnabled()) {
					String stepUniqeKey = templateParser.parse(step.getClientId().toString(),
							"step_unique_key_" + step.getId().toString(), step.getUniqueKeyTemplate(), objectHolder);
					StepUniqueRecord stepUniqueRecord = new StepUniqueRecord();
					stepUniqueRecord.setStepId(step.getId());
					stepUniqueRecord.setUniqueKey(stepUniqeKey);
					if (stepUniqueRecordService.getCount(stepUniqueRecord) > 0) {
						if (step.isLoggingEnabled()) {
							saveStepHistory(step, objectHolder, genericRequest,
									"Didn't try rest, as its duplicate record:" + stepUniqeKey, genericHeader,
									Status.SUCCESS);
						}
						return null;
					}
				}

				StepRequestValidator stepRequestValidator = new StepRequestValidator();
				stepRequestValidator.setStepId(step.getId());
				Map<String, Boolean> orderBy = new HashMap<>();
				orderBy.put("sequence", Boolean.TRUE);
				List<StepRequestValidator> stepRequestValidatorList = stepRequestValidatorService
						.getListByCriteria(stepRequestValidator, -1, 0, orderBy, false, null);
				for (StepRequestValidator requestValidator : stepRequestValidatorList) {
					String err = (String) nashornHelper.process(requestValidator.getRequestValidation(), objectHolder,
							objectHolder);
					if (err != null && !err.isEmpty()) {
						if (requestValidator.getResultFlag() == ResultFlag.SUCCESS) {
							if (isItLastStep(step))
								objectHolder.setJobRunSuccessCount(objectHolder.getJobRunSuccessCount() == null ? 1
										: objectHolder.getJobRunSuccessCount() + 1);
							if (step.isLoggingEnabled())
								saveStepHistory(step, objectHolder, genericRequest,
										"Didn't try rest, validation error:" + err, genericHeader, Status.SUCCESS);
						}
						if (requestValidator.getResultFlag() == ResultFlag.FAILURE) {
							objectHolder.setJobRunFailureCount(objectHolder.getJobRunFailureCount() == null ? 1
									: objectHolder.getJobRunFailureCount() + 1);
							saveStepHistory(step, objectHolder, genericRequest,
									"Didn't try rest, validation error:" + err, genericHeader, Status.FAILURE);
							failOverHandler(jobDetail, false, step, objectHolder, objectHolder,
									"Didn't try rest, validation error:" + err, level);
						}
						return null;
					}
				}

				/*
				 * if (step.getRequestValidator() != null &&
				 * !step.getRequestValidator().trim().isEmpty()) { String err = (String)
				 * nashornHelper.process(step.getRequestValidator(), objectHolder,
				 * objectHolder); if (!err.isEmpty()) { if (step.isLoggingEnabled()) {
				 * saveStepHistory(step, objectHolder, genericRequest,
				 * "Didn't try rest, validation error:" + err, genericHeader, Status.SUCCESS); }
				 * return null; } }
				 */
				if (objectHolder.isDryRun()
						&& objectHolder.getConfigs().get("0").getValues().get("dryRunStepId").equals(step.getId())) {
					if (objectHolder.getConfigs().get("0").getValues().get("dryRunStepUniqueId") != null) {
						String stepUniqeKey = templateParser.parse(step.getClientId().toString(),
								"step_unique_key_" + step.getId().toString(), step.getUniqueKeyTemplate(),
								objectHolder);
						if (stepUniqeKey
								.equals(objectHolder.getConfigs().get("0").getValues().get("dryRunStepUniqueId"))) {
							return null;
						}
					}
				}

				if (step.getPath() != null && !step.getPath().isEmpty()) {
					LinkedHashMap<?, ?> headerMap = new LinkedHashMap<>();
					if (step.getHeaderTemplate() != null && !step.getHeaderTemplate().isEmpty()) {
						headerMap = mapper.readValue(templateParser.parse(step.getClientId().toString(),
								"header_" + step.getId().toString(), step.getHeaderTemplate(), objectHolder),
								LinkedHashMap.class);
					}

					HttpHeaders headers = new HttpHeaders();
					for (Map.Entry<?, ?> requestHeaderEntry : headerMap.entrySet()) {
						headers.add(requestHeaderEntry.getKey().toString(), requestHeaderEntry.getValue().toString());
					}
					HttpEntity<?> httpEntity = null;
					headers.setContentType(MediaType.valueOf(step.getSubType().split("\\|")[1]));

					StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(
							StandardCharsets.UTF_8);
					stringHttpMessageConverter.setWriteAcceptCharset(false);
					restTemplate.getMessageConverters().add(0, stringHttpMessageConverter);

					StepSettings stepSettingsSearchForUserNameAndPassword = new StepSettings();
					stepSettingsSearchForUserNameAndPassword.setStepId(step.getId());
					stepSettingsSearchForUserNameAndPassword.setClientId(step.getClientId());
					List<StepSettings> stepSettingsListForUserNameAndPassword = stepSettingsService
							.getListByCriteria(stepSettings, -1, 0);
					if (!stepSettingsListForUserNameAndPassword.isEmpty()) {
						String auth = "USERNAME" + ":" + "PASSWORD";
						Boolean userNamePresent = false;
						Boolean passwordPresent = false;
						for (StepSettings stepSettings2 : stepSettingsListForUserNameAndPassword) {

							if (stepSettings2.getKey().equals("BASIC_AUTHORIZATION_USERNAME")) {
								auth = auth.replace("USERNAME", stepSettings2.getValue());
								userNamePresent = true;
							}
							if (stepSettings2.getKey().equals("BASIC_AUTHORIZATION_PASSWORD")) {
								auth = auth.replace("PASSWORD", stepSettings2.getValue());
								passwordPresent = true;
							}
						}

						if (userNamePresent && passwordPresent) {
							byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
							String authHeader = "Basic " + new String(encodedAuth);
							headers.set("Authorization", authHeader);
						}

					}

					MultiValueMap<String, String> mapRequest = new LinkedMultiValueMap<>();
					String request = "";
					if (step.getRequestFormatterJs() != null && !step.getRequestFormatterJs().trim().isEmpty()) {
						request = (String) nashornHelper.process(step.getRequestFormatterJs(), objectHolder,
								objectHolder);
						if (jobSettingsService.equal(jobDetail.getClientId(), jobDetail.getId(),
								"RUN_JOB_AGAIN_ENABLED", "YES") && request != null && (!request.equals("[]"))) {
							objectHolder.setRunAgain(true);
						} else {
							objectHolder.setRunAgain(false);
						}
						List<String> list = new ArrayList<>();
						list.add(request);
						mapRequest.put(stepSettingsService.getValue(step.getClientId(), step.getId(), "FORM_DATA_KEY"),
								list);
						System.out.println(mapRequest);
						logger.info(mapRequest);
						request = "";
					}
					request = templateParser.parse(step.getClientId().toString(), "step_" + step.getId().toString(),
							step.getRequestTemplate(), objectHolder);
					System.out.println(request);
					logger.info(request);
					if (headers.getContentType().equals(MediaType.APPLICATION_JSON)) {
						httpEntity = new HttpEntity<String>(request, headers);
						genericRequest = request;
					} else if (headers.getContentType().equals(MediaType.APPLICATION_XML)) {
						httpEntity = new HttpEntity<String>(request, headers);
						genericRequest = request;
					} else if (headers.getContentType().equals(MediaType.TEXT_HTML)) {
						httpEntity = new HttpEntity<String>(request, headers);
						genericRequest = request;
					} else if (headers.getContentType().equals(MediaType.APPLICATION_FORM_URLENCODED)) {
						httpEntity = new HttpEntity<String>(request, headers);
						genericRequest = request;
						if (step.getRequestTemplate() == null || step.getRequestTemplate().isEmpty()) {
							httpEntity = new HttpEntity<MultiValueMap<String, String>>(mapRequest, headers);
							genericRequest = mapRequest;
						}
					} else if (headers.getContentType().equals(MediaType.TEXT_XML)) {
						httpEntity = new HttpEntity<String>(request, headers);
						genericRequest = request;
					} else {
						try {
							if (mapRequest != null && !request.isEmpty()) {
								mapRequest = mapper.readValue(request, LinkedMultiValueMap.class);
							}
						} catch (IOException e) {
							e.printStackTrace();

							logger.error(MarkerFactory.getMarker("Exception"), e);
						}
						httpEntity = new HttpEntity<MultiValueMap<String, String>>(mapRequest, headers);
						genericRequest = mapRequest;
					}

					genericHeader = headers;
					if ("XML".equals(step.getResponseType()) || "CUSTOM_XML".equals(step.getResponseType())) {
						MappingJackson2XmlHttpMessageConverter one = new MappingJackson2XmlHttpMessageConverter();
						List<MediaType> mediaTypes = new ArrayList<>();
						mediaTypes.add(MediaType.TEXT_HTML);
						mediaTypes.add(MediaType.TEXT_XML);
						one.setSupportedMediaTypes(mediaTypes);
						restTemplate.getMessageConverters().add(one);
					} else if ("JSON".equals(step.getResponseType())) {
						MappingJackson2HttpMessageConverter one = new MappingJackson2HttpMessageConverter();
						List<MediaType> mediaTypes = new ArrayList<>();
						mediaTypes.add(MediaType.TEXT_HTML);
						one.setSupportedMediaTypes(mediaTypes);
						restTemplate.getMessageConverters().add(one);
					} else if ("TEXT_JSON".equals(step.getResponseType())) {
						MappingJackson2HttpMessageConverter one = new MappingJackson2HttpMessageConverter();
						List<MediaType> mediaTypes = new ArrayList<>();
						mediaTypes.add(MediaType.ALL);
						one.setSupportedMediaTypes(mediaTypes);
						restTemplate.getMessageConverters().add(one);
					}

					if ("CUSTOM_XML".equals(step.getResponseType())) {
						object = restTemplate.exchange(
								templateParser.parse(step.getClientId().toString(),
										"step_path_template_" + step.getId().toString(), step.getPath(), objectHolder),
								HttpMethod.valueOf(step.getSubType().split("\\|")[0]), httpEntity, String.class);
					} else {
						object = restTemplate.exchange(
								templateParser.parse(step.getClientId().toString(),
										"step_path_template_" + step.getId().toString(), step.getPath(), objectHolder),
								HttpMethod.valueOf(step.getSubType().split("\\|")[0]), httpEntity, Object.class);
					}

					if ("CUSTOM_XML".equals(step.getResponseType())) {
						entityResponse = XmlToJsonConverter.convert(object.getBody().toString());
						entityHeader = object.getHeaders();
					} else {
						System.out.println(JsonUtil.toString(object.getBody()));
						logger.info(JsonUtil.toString(object.getBody()));
						entityResponse = object.getBody();
						entityHeader = object.getHeaders();
					}

				}
				if (step.getPath() == null || step.getPath().isEmpty() || ((object.getStatusCode() == HttpStatus.OK
						|| object.getStatusCode() == HttpStatus.CREATED)
						&& (step.getSuccessKey() == null || templateParser.parse(step.getClientId().toString(),
								"success_key_template_" + step.getId().toString(), step.getSuccessKey(), entityResponse)
								.equals(step.getSuccessValue())))) {
					if (step.isDuplicateCheckEnabled()) {
						String stepUniqeKey = templateParser.parse(step.getClientId().toString(),
								"step_unique_key_" + step.getId().toString(), step.getUniqueKeyTemplate(),
								objectHolder);
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
						objectHolder.setJobRunSuccessCount(objectHolder.getJobRunSuccessCount() == null ? 1
								: objectHolder.getJobRunSuccessCount() + 1);

					if (step.getPath() == null || step.getPath().isEmpty())
						entityResponse = objectHolder.getConfigs().get("0").getValues().get("0");
					System.out.println(JsonUtil.toString(entityResponse));
					genericReponse = entityResponse;

					if (step.isLoggingEnabled()) {

						saveStepHistory(step, objectHolder, genericRequest, genericReponse, genericHeader,
								Status.SUCCESS);
					}
					
					if ((!stepSettingsListForCache.isEmpty()) && jobSettingsService.equal(jobDetail.getClientId(), jobDetail.getId(),
							"ENABLE_CACHE", "YES")) {
						oipCache.put(templateParser.parse(step.getClientId().toString(), "step_" + step.getId().toString(),
							stepSettingsService.getValue(step.getClientId(), step.getId(), "CACHE_KEY"), objectHolder) , genericReponse);
					}
					
					if (step.getResponseFormatterJs() != null && !step.getResponseFormatterJs().trim().isEmpty()) {
						Object response = "";
						if (stepSettingsService.equal(jobDetail.getClientId(), step.getId(), "GET_DATA_FROM_HEADER",
								"YES"))
							response = nashornHelper.process(step.getResponseFormatterJs(), objectHolder, entityHeader);
						else
							response = nashornHelper.process(step.getResponseFormatterJs(), objectHolder,
									entityResponse);
						if (jobSettingsService.equal(jobDetail.getClientId(), jobDetail.getId(),
								"RUN_JOB_AGAIN_ENABLED", "YES") && response != null
								&& (!response.toString().equals("[]"))) {
							objectHolder.setRunAgain(true);
						} else {
							objectHolder.setRunAgain(false);
						}

						return response;
					}
					return genericReponse;
				} else {
					genericReponse = entityResponse;
					objectHolder.setJobRunFailureCount(objectHolder.getJobRunFailureCount() == null ? 1
							: objectHolder.getJobRunFailureCount() + 1);
					saveStepHistory(step, objectHolder, genericRequest, genericReponse, genericHeader, Status.FAILURE);
					failOverHandler(jobDetail, false, step, objectHolder, object.getBody(),
							"Validation Error from Rest:" + mapper.writeValueAsString(object.getBody()), level);
					return genericReponse;
				}
			} catch (JobException e) {
				throw e;
			} catch (HttpStatusCodeException e) {
				e.printStackTrace();
				logger.error(MarkerFactory.getMarker("Exception"), e);
				String string = new String(e.getResponseBodyAsString());
				System.out.println(string);
				saveStepHistory(step, objectHolder, genericRequest, e.getResponseBodyAsString(), genericHeader,
						Status.FAILURE);
				failOverHandler(jobDetail, true, step, objectHolder, null, e.getResponseBodyAsString(), level);
				objectHolder.setJobRunFailureCount(
						objectHolder.getJobRunFailureCount() == null ? 1 : objectHolder.getJobRunFailureCount() + 1);
			} catch (Throwable e) {
				e.printStackTrace();
				logger.error(MarkerFactory.getMarker("Exception"), e);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				String string = new String(e.toString());
				System.out.println(string);
				e.printStackTrace(pw);
				saveStepHistory(step, objectHolder, genericRequest, genericReponse, genericHeader, Status.FAILURE);
				failOverHandler(jobDetail, true, step, objectHolder, null, sw.toString(), level);
				objectHolder.setJobRunFailureCount(
						objectHolder.getJobRunFailureCount() == null ? 1 : objectHolder.getJobRunFailureCount() + 1);

			}
			return null;
		} else {
			LinkedHashMap<?, ?> map = null;
			try {
				map = mapper.readValue(step.getDryRunResponse(), LinkedHashMap.class);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(MarkerFactory.getMarker("Exception"), e);
			}
			return map;
		}
	}

	private void saveStepHistory(Step step, ObjectHolder objectHolder, Object request, Object response, Object header,
			StepHistory.Status status) throws Exception {
		StepHistory stepHistory = new StepHistory();
		// stepHistory.setConfigObjectJson(JsonUtil.toString(objectHolder));

		stepHistory.setRequest(JsonUtil.toString(request));
		if (request != null) {
			stepHistory.setRequest(request.toString());

			List<StepSettings> stepSettingsListForSearchCriteria = stepSettingsService
					.getStepSettings(step.getClientId(), step.getId(), "STEP_SEARCH_CRITERIA");
			if (!stepSettingsListForSearchCriteria.isEmpty()) {
				// if steprequesttype!=null && !=emty
				// else

				if (step.getRequestType() != null && !step.getRequestType().isEmpty()) {

					if (stepSettingsService.getValue(step.getClientId(), step.getId(),
							"STEP_SEARCH_CRITERIA") != null) {
						if (step.getRequestType().contentEquals("XML") && step.getRequestType() != "")
							request = XmlToJsonConverter.convert(request.toString());
						if (step.getRequestType().contentEquals("JSON") && step.getRequestType() != "")
							request = JsonUtil.parse(request.toString());
						if (step.getRequestType().contentEquals("STRING") && step.getRequestType() != "")
							stepHistory.setSearchCriteria("");
						stepHistory
								.setSearchCriteria(templateParser.parse(step.getClientId().toString(),
										"step_searchCriteria_template_" + step.getId().toString(), stepSettingsService
												.getValue(step.getClientId(), step.getId(), "STEP_SEARCH_CRITERIA"),
										request));
					} 

				}else {
					stepHistory
					.setSearchCriteria(templateParser.parse(step.getClientId().toString(),
							"step_searchCriteria_template_" + step.getId().toString(), stepSettingsService
									.getValue(step.getClientId(), step.getId(), "STEP_SEARCH_CRITERIA"),
									objectHolder));

		}
				
			}
		}

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
		// kafkaProducerService.sendMessage(stepHistory,"stepHistory1");
	}

	// public void fillTranslatorObject(Step step, List<StepSettings>
	// stepSettings, ObjectHolder objectHolder,
	// Integer level) {
	// Map<String, Map<String, Object>> converterMap = new HashMap<>();
	// for (StepSettings stepSettingsSingle : stepSettings) {
	// if (stepSettingsSingle.getKey().endsWith("_TRANSLATOR_TEMPLATE")) {
	// String stransaltorValues =
	// templateParser.parse(step.getClientId().toString(),
	// "STEP_SETTING_" + stepSettingsSingle.getId(),
	// stepSettingsSingle.getValue(), objectHolder);
	// for (String value : stransaltorValues.split(",")) {
	// String stepCode =
	// stepSettingsSingle.getKey().replace("_TRANSLATOR_TEMPLATE", "");
	// ClientTranslation stepTranslation = new ClientTranslation();
	// stepTranslation.setClientId(step.getClientId());
	// stepTranslation.setCode(stepCode);
	// stepTranslation.setKey(value);
	// stepTranslation =
	// clientTranslationService.getListByCriteria(stepTranslation, 0, 1).get(0);
	// if (converterMap.get(stepCode) == null) {
	// converterMap.put(stepCode, new HashMap<String, Object>());
	// }
	// converterMap.get(stepCode).put(stepTranslation.getKey(),
	// stepTranslation.getValue());
	// objectHolder.getConfigs().get("" + level).getTranslatorMap().put("" +
	// step.getSequence(),
	// converterMap);
	// }
	// }
	// }
	// }

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

	private ClientHttpRequestFactory getClientHttpRequestFactory(JobDetail jobDetail) {
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		JobSettings searchCriteria = new JobSettings();
		searchCriteria.setClientId(jobDetail.getClientId());
		searchCriteria.setJobDetailId(jobDetail.getId());
		searchCriteria.setKey("CONNECTION_AND_READ_TIMEOUT");
		List<JobSettings> list = jobSettingsDAO.getListByCriteria(searchCriteria, 0, Integer.MAX_VALUE);
		// Connect timeout
		clientHttpRequestFactory.setConnectTimeout(!list.isEmpty() ? Integer.parseInt(list.get(0).getValue()) : 10000);
		// Read timeout
		clientHttpRequestFactory.setReadTimeout(!list.isEmpty() ? Integer.parseInt(list.get(0).getValue()) : 10000);
		return clientHttpRequestFactory;
	}

}