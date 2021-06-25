package org.sunbird.ruleengine.job;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.common.util.EmailParser;
import org.sunbird.ruleengine.common.util.JsonUtil;
import org.sunbird.ruleengine.common.util.MailBean;
import org.sunbird.ruleengine.dao.FailureMailConfigDao;
import org.sunbird.ruleengine.dao.IntegrationInstanceFailureDao;
import org.sunbird.ruleengine.dao.MailDao;
import org.sunbird.ruleengine.model.Client;
import org.sunbird.ruleengine.model.FailureMailConfig;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.Mail;
import org.sunbird.ruleengine.model.MailSettings;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.ClientSettingsService;
import org.sunbird.ruleengine.service.IntegrationInstanceFailureService;
import org.sunbird.ruleengine.service.Job;
import org.sunbird.ruleengine.service.JobDetailService;
import org.sunbird.ruleengine.service.JobSettingsService;
import org.sunbird.ruleengine.service.MailService;
import org.sunbird.ruleengine.service.MailSettingsService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MailJob implements Job {
	private static final Logger logger = LogManager.getLogger(MailJob.class);
	@Autowired
	MailBean mailBean;

	@Autowired
	IntegrationInstanceFailureService integrationInstanceFailureService;

	@Autowired
	ClientService clientService;

	@Autowired
	IntegrationInstanceFailureDao integrationInstanceFailureDao;

	@Autowired
	MailService mailService;

	@Autowired
	ClientSettingsService clientSettingsService;

	@Autowired
	JobDetailService jobDetailService;

	@Autowired
	JobSettingsService jobSettingsService;

	@Autowired
	EmailParser emailParser;

	@Autowired
	MailSettingsService mailSettingsService;

	@Autowired
	MailDao mailDao;
	
	@Autowired
	FailureMailConfigDao failureMailConfigDao;


	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void runJob() {
		System.out.println("Mail Cron started");
		logger.info("Mail Cron started");
		Client clnt = new Client();
		try {
			
			List<Client> clientList = clientService.getListByCriteria(clnt, -1, 11);
			for (Client client : clientList) {
				if ("YES".equals(clientSettingsService.getValue(client.getId(), "FAILED_JOB_EMAIL_AT_CLIENT_LEVEL"))) {
					
					
					if ("YES".equals(clientSettingsService.getValue(client.getId(), "FAILED_JOB_EMAIL_ENABLED"))) {
						String jobIds = clientSettingsService.getValue(client.getId(), "FAILED_JOB_IDS_FOR_MAIL");
						Mail mail = new Mail();
						mail.setClientId(client.getId());
						
						MailSettings mailSetting = new MailSettings();
						mailSetting.setClientId(client.getId());
						
						List<MailSettings> mailSettings = mailSettingsService.getListByCriteria(mailSetting, -1, 0);
						IntegrationInstanceFailure integrationInstanceFailure = new IntegrationInstanceFailure();
						integrationInstanceFailure.setClientId(client.getId());
						integrationInstanceFailure.setDone(false);
						
						FailureMailConfig searchCriteriaForMailConfig = new FailureMailConfig();
						searchCriteriaForMailConfig.setClientId(client.getId());
						List<Object[]> notInErrorPatternIds = failureMailConfigDao.getArrayListByCriteria(searchCriteriaForMailConfig, "SELECT failureMailConfig.NOT_IN_ERROR_PATTERN_ID FROM FAILURE_MAIL_CONFIG failureMailConfig WHERE 1=1 ", null, -1, 0);
						
						
						
						if (!mailSettings.isEmpty()) {
							Map<String, Boolean> orderBy = new HashMap<>();
							orderBy.put("creationDate", Boolean.FALSE);
							List<Mail> mails = mailDao.getListByCriteria(mail, 0, 1, orderBy, false, null);
							if (mails.isEmpty()) {
								LocalDateTime localDate = LocalDateTime.now();
								Date endDate = java.sql.Timestamp.valueOf(localDate);
								Date startDate = java.sql.Timestamp.valueOf(localDate.minusHours(3));
								List<IntegrationInstanceFailure> integrationFailureList = integrationInstanceFailureService
										.getIntegrationInstanceFailureWithJobDetail(integrationInstanceFailure, startDate,
												endDate,jobIds,0,100,notInErrorPatternIds);
								
									//integrationFailureList = setConfigInObject(integrationFailureList);
									sendMail(client.getId(), integrationFailureList, startDate, endDate);
								
								
							} else {
								Mail previousMail = mails.get(0);
								Date previousMailDate = previousMail.getCreationDate();
								int mailInterval = mailSettings.get(0).getMailInterval();
								boolean validToSendMail = validToSendMail(previousMailDate, mailInterval);
								if (validToSendMail) {
									LocalDateTime localDate = previousMailDate.toInstant()
											.atZone(ZoneId.systemDefault()).toLocalDateTime();
									Date startDate = java.sql.Timestamp.valueOf(localDate.minusSeconds(1));
									Date endDate = java.sql.Timestamp.valueOf(localDate.plusMinutes(mailInterval));
									List<IntegrationInstanceFailure> integrationFailureList = integrationInstanceFailureService
											.getIntegrationInstanceFailureWithJobDetail(integrationInstanceFailure, startDate,
													endDate,jobIds,0,100,notInErrorPatternIds);
									
									//	integrationFailureList = setConfigInObject(integrationFailureList);
										sendMail(client.getId(), integrationFailureList, startDate,
												endDate);
									
									
								}
							}
						} else {
							Date startDate = getTodayStartDate();
							Date endDate = getTodayEndDate();
							List<Mail> mailList = mailService.getListByDate(mail, 0, Integer.MAX_VALUE, startDate,
									endDate);
							if (mailList.isEmpty()) {
								List<IntegrationInstanceFailure> integrationFailureList =integrationInstanceFailureService
										.getIntegrationInstanceFailureWithJobDetail(integrationInstanceFailure, startDate,
												endDate,jobIds,0,100,notInErrorPatternIds);
								
									//integrationFailureList = setConfigInObject(integrationFailureList);
									sendMail(client.getId(), integrationFailureList, startDate, endDate);
								
								
							}
						}

					
					}
				}
				
				
				if ("YES".equals(clientSettingsService.getValue(client.getId(), "FAILED_JOB_EMAIL_AT_JOB_LEVEL"))) {
					
					
					JobDetail jobDetailSearch = new JobDetail();
					jobDetailSearch.setClientId(client.getId());
					List<JobDetail> jobDetails = jobDetailService.getListByCriteria(jobDetailSearch, -1, 0);
					for (JobDetail jobDetail : jobDetails) {
						if (jobSettingsService.equal(client.getId(), jobDetail.getId(), "FAILED_JOB_EMAIL_ENABLED",
								"YES")) {
							Mail mail = new Mail();
							mail.setClientId(client.getId());
							mail.setJobDetailId(jobDetail.getId());
							MailSettings mailSetting = new MailSettings();
							mailSetting.setClientId(client.getId());
							mailSetting.setJobDetailId(jobDetail.getId());
							List<MailSettings> mailSettings = mailSettingsService.getListByCriteria(mailSetting, -1, 0);
							IntegrationInstanceFailure integrationInstanceFailure = new IntegrationInstanceFailure();
							integrationInstanceFailure.setClientId(client.getId());
							integrationInstanceFailure.setJobDetailId(jobDetail.getId());
							integrationInstanceFailure.setDone(false);
							
							
							FailureMailConfig searchCriteriaForMailConfig = new FailureMailConfig();
							searchCriteriaForMailConfig.setJobDetailId(jobDetail.getId());
							searchCriteriaForMailConfig.setClientId(client.getId());
							List<Object[]> notInErrorPatternIds = failureMailConfigDao.getArrayListByCriteria(searchCriteriaForMailConfig, "SELECT failureMailConfig.NOT_IN_ERROR_PATTERN_ID FROM FAILURE_MAIL_CONFIG failureMailConfig WHERE 1=1 ", null, -1, 0);
							if (!mailSettings.isEmpty()) {
								Map<String, Boolean> orderBy = new HashMap<>();
								orderBy.put("creationDate", Boolean.FALSE);
								List<Mail> mails = mailDao.getListByCriteria(mail, 0, 1, orderBy, false, null);
								if (mails.isEmpty()) {
									LocalDateTime localDate = LocalDateTime.now();
									Date endDate = java.sql.Timestamp.valueOf(localDate);
									Date startDate = java.sql.Timestamp.valueOf(localDate.minusHours(3));
									List<IntegrationInstanceFailure> integrationFailureList = integrationInstanceFailureDao
											.getIntegrationFailureListByDate(integrationInstanceFailure, startDate,
													endDate,0,100,notInErrorPatternIds);
									
										//integrationFailureList = setConfigInObject(integrationFailureList);
										sendMail(client.getId(), jobDetail.getId(), integrationFailureList, startDate, endDate);
									
									
								} else {
									Mail previousMail = mails.get(0);
									Date previousMailDate = previousMail.getCreationDate();
									int mailInterval = mailSettings.get(0).getMailInterval();
									boolean validToSendMail = validToSendMail(previousMailDate, mailInterval);
									if (validToSendMail) {
										LocalDateTime localDate = previousMailDate.toInstant()
												.atZone(ZoneId.systemDefault()).toLocalDateTime();
										Date startDate = java.sql.Timestamp.valueOf(localDate.minusSeconds(1));
										Date endDate = java.sql.Timestamp.valueOf(localDate.plusMinutes(mailInterval));
										List<IntegrationInstanceFailure> integrationFailureList = integrationInstanceFailureDao
												.getIntegrationFailureListByDate(integrationInstanceFailure, startDate,
														endDate,0,100,notInErrorPatternIds);
										
										//	integrationFailureList = setConfigInObject(integrationFailureList);
											sendMail(client.getId(), jobDetail.getId(), integrationFailureList, startDate,
													endDate);
										
										
									}
								}
							} else {
								Date startDate = getTodayStartDate();
								Date endDate = getTodayEndDate();
								List<Mail> mailList = mailService.getListByDate(mail, 0, Integer.MAX_VALUE, startDate,
										endDate);
								if (mailList.isEmpty()) {
									List<IntegrationInstanceFailure> integrationFailureList = integrationInstanceFailureDao
											.getIntegrationFailureListByDate(integrationInstanceFailure,
													getPreviousDayStartDate(), getPreviousDayEndDate(),0,100,notInErrorPatternIds);
									
										//integrationFailureList = setConfigInObject(integrationFailureList);
										sendMail(client.getId(), jobDetail.getId(), integrationFailureList, startDate, endDate);
									
									
								}
							}

						}
				}
				
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in mailJob.runJob()");
			logger.info("Exception in mailJob.runJob()");
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}

	}

	private void sendMail(BigInteger clientId,
			List<IntegrationInstanceFailure> integrationFailureList, Date fromDate, Date toDate) {

		String subject = clientSettingsService.getValue(clientId, "FAILED_JOB_EMAIL_SUBJECT");
		String fromMail = clientSettingsService.getValue(clientId,  "FAILED_JOB_EMAIL_FROM");
		String toMail = clientSettingsService.getValue(clientId, "FAILED_JOB_EMAIL_TO");
		String fromMailDisplay = fromMail;
		String fromName = clientSettingsService.getValue(clientId, "FAILED_JOB_EMAIL_FROM_NAME");
		String emailTempate = clientSettingsService.getValue(clientId, "FAILED_JOB_EMAIL_TEMPALATE");
		Map<String, Object> root = new HashMap<String, Object>();
		String message = "";
		root.put("integrationInstanceFailureList", integrationFailureList);
		root.put("startDate", fromDate);
		root.put("endDate", toDate);
		System.out.println(JsonUtil.toString( root));
		logger.info(JsonUtil.toString( root));
		boolean mailSent=false;
		if(integrationFailureList.size()>0)
		{
			message = emailParser.parse(clientId.toString(), "JOB_DETAIL_MAIL" + clientId, emailTempate, root);
			mailSent = mailBean.sendMail(clientId, fromMail, fromMailDisplay, fromName, toMail, message, subject,
					true);
		}
		else
		{
			mailSent=true;
		}
		
		if (mailSent) {
			saveMail(clientId, fromMail, fromMailDisplay, subject, toMail, toDate);
		}
	}
	
	
	
	
	
	
	private void sendMail(BigInteger clientId, BigInteger jobDetailId,
			List<IntegrationInstanceFailure> integrationFailureList, Date fromDate, Date toDate) {

		
		String subject = jobSettingsService.getValue(clientId, jobDetailId, "FAILED_JOB_EMAIL_SUBJECT");
		String fromMail = jobSettingsService.getValue(clientId, jobDetailId, "FAILED_JOB_EMAIL_FROM");
		String toMail = jobSettingsService.getValue(clientId, jobDetailId, "FAILED_JOB_EMAIL_TO");
		String fromMailDisplay = fromMail;
		String fromName = jobSettingsService.getValue(clientId, jobDetailId, "FAILED_JOB_EMAIL_FROM_NAME");
		String emailTempate = jobSettingsService.getValue(clientId, jobDetailId, "FAILED_JOB_EMAIL_TEMPALATE");
		Map<String, Object> root = new HashMap<String, Object>();
		String message = "";
		root.put("integrationInstanceFailureList", integrationFailureList);
		root.put("startDate", fromDate);
		root.put("endDate", toDate);
		System.out.println(JsonUtil.toString(root));
		logger.info(JsonUtil.toString( root));
		boolean mailSent=false;
		if(integrationFailureList.size()>0)
		{
			message = emailParser.parse(clientId.toString(), "JOB_DETAIL_MAIL" + jobDetailId, emailTempate, root);
			mailSent = mailBean.sendMail(clientId, fromMail, fromMailDisplay, fromName, toMail, message, subject,
					true);
		}
		else
		{
			mailSent=true;
		}
		
		if (mailSent) {
			saveMail(clientId, fromMail, fromMailDisplay, subject, toMail, jobDetailId, toDate);
		}
	}

	private void saveMail(BigInteger clientId, String fromMail, String fromMailDisplay, String subject, String toMail,
			BigInteger jobDetailId, Date date) {
		Mail mailObj = new Mail();
		mailObj.setClientId(clientId);
		mailObj.setCreationDate(date);
		mailObj.setLastUpdateDate(date);
		mailObj.setFromMail(fromMail);
		mailObj.setHeader(fromMailDisplay);
		mailObj.setSubject(subject);
		mailObj.setToMail(toMail);
		mailObj.setJobDetailId(jobDetailId);
		mailService.save(mailObj);
	}
	
	
	private void saveMail(BigInteger clientId, String fromMail, String fromMailDisplay, String subject, String toMail,
			 Date date) {
		Mail mailObj = new Mail();
		mailObj.setClientId(clientId);
		mailObj.setCreationDate(date);
		mailObj.setLastUpdateDate(date);
		mailObj.setFromMail(fromMail);
		mailObj.setHeader(fromMailDisplay);
		mailObj.setSubject(subject);
		mailObj.setToMail(toMail);
		
		mailService.save(mailObj);
	}

	private Date getPreviousDayStartDate() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
		LocalDateTime localStartDateTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(),
				localDateTime.getDayOfMonth(), 0, 0, 0, 0);
		return java.sql.Timestamp.valueOf(localStartDateTime);
	}

	private Date getPreviousDayEndDate() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
		LocalDateTime localEndDateTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(),
				localDateTime.getDayOfMonth(), 23, 59, 59, 999999999);
		return java.sql.Timestamp.valueOf(localEndDateTime);
	}

	private Date getTodayStartDate() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
		LocalDateTime localStartDateTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(),
				localDateTime.getDayOfMonth(), 0, 0, 0, 0);
		return java.sql.Timestamp.valueOf(localStartDateTime);
	}

	private Date getTodayEndDate() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
		LocalDateTime localEndDateTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(),
				localDateTime.getDayOfMonth(), 23, 59, 59, 999999999);
		return java.sql.Timestamp.valueOf(localEndDateTime);
	}

	private boolean validToSendMail(Date date, int hours) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime previousMailDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		previousMailDate = previousMailDate.plusMinutes(hours);
		int result = previousMailDate.compareTo(now);
		return result <= 0;
	}

	/*private List<IntegrationInstanceFailure> setConfigInObject(List<IntegrationInstanceFailure> failureList) {
		try {
			for (IntegrationInstanceFailure failure : failureList) {
				failure.setConfigObjectJsonObject(
						objectMapper.readValue(failure.getConfigObjectJson(), LinkedHashMap.class));
			}
		} catch (Exception e) {
			System.out.println("Exception in mailJob.setConfigInObject()");
			e.printStackTrace();
		}
		return failureList;
	}*/

	// final Calendar cal = Calendar.getInstance();
	// cal.set(Calendar.HOUR_OF_DAY, 0);
	// cal.set(Calendar.MINUTE, 0);
	// cal.set(Calendar.SECOND, 0);
	// // cal.add(Calendar.DATE, -1);
	// Date startDate = cal.getTime();
	// cal.set(Calendar.HOUR_OF_DAY, 23);
	// cal.set(Calendar.MINUTE, 59);
	// cal.set(Calendar.SECOND, 59);
	// Date endDate = cal.getTime();

	// LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
	// LocalDateTime localStartDateTime =
	// LocalDateTime.of(localDateTime.getYear(),
	// localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), 0, 0,
	// 0, 0);
	// LocalDateTime localEndDateTime = LocalDateTime
	// .of(localDateTime.getYear(), localDateTime.getMonthValue(),
	// localDateTime.getDayOfMonth(), 23, 59, 59, 999999999);
	// Date startDate = java.sql.Timestamp.valueOf(localStartDateTime);
	// Date endDate = java.sql.Timestamp.valueOf(localEndDateTime);
	// final Calendar cal = Calendar.getInstance();
	// cal.set(Calendar.HOUR_OF_DAY, 0);
	// cal.set(Calendar.MINUTE, 0);
	// cal.set(Calendar.SECOND, 0);
	// cal.add(Calendar.DATE, -1);
	// Date startDate = cal.getTime();
	// cal.set(Calendar.HOUR_OF_DAY, 23);
	// cal.set(Calendar.MINUTE, 59);
	// cal.set(Calendar.SECOND, 59);
	// Date endDate = cal.getTime();
}
