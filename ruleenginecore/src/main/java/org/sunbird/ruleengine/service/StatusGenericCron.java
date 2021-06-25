package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.common.util.MailBean;
import org.sunbird.ruleengine.helper.TemplateParser;
import org.sunbird.ruleengine.model.Client;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.JobDetail.IntervalType;

@Component
public class StatusGenericCron implements ApplicationContextAware {
	private static final Logger logger = LogManager.getLogger(StatusGenericCron.class);

	@Autowired
	JobDetailService jobDetailService;

	ApplicationContext applicationContext;

	@Autowired
	TemplateParser templateParser;

	@Autowired
	MailBean mailBean;

	@Autowired
	ClientService clientService;

	@Autowired
	ClientSettingsService clientSettingService;

	@Scheduled(fixedDelay = 60 * 60 * 1000)
	public void startCron() {
		try {
			System.out.println("Status Cron Started");
			logger.info("Status Cron Started");
			Client clnt = new Client();
			List<Client> clients = clientService.getListByCriteria(clnt, -1, 0);
			String template = "";

			for (Client client : clients) {
				if ("YES".equals(clientSettingService.getValue(client.getId(), "STOPPED_JOB_EMAIL_ENABLED"))) {
					JobDetail jobDetail = new JobDetail();
					jobDetail.setToDateTime(new Date());
					jobDetail.setStatus(JobDetail.Status.RUNNING);
					jobDetail.setClientId(client.getId());
					List<JobDetail> jobDetails = jobDetailService.getListByCriteria(jobDetail, -1, 0);
					jobDetails = getAllStoppedJobs(jobDetails, client.getId());
					if (!jobDetails.isEmpty()) {
						Map<String, List<JobDetail>> data = new HashMap<>();
						data.put("jobDetailList", jobDetails);
						template = clientSettingService.getValue(client.getId(), "STOPPED_JOB_EMAIL_TEMPALATE");
						String message = templateParser.parse(client.getId().toString(),
								"stoped_job_mail_" + jobDetails.get(0).getId().toString(), template, data);
						boolean mailSent = sendMail(client.getId(), message);
						if (mailSent) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}

	}

	private boolean sendMail(BigInteger clientId, String message) {
		String subject = clientSettingService.getValue(clientId, "STOPPED_JOB_EMAIL_SUBJECT");
		String fromMail = clientSettingService.getValue(clientId, "STOPPED_JOB_EMAIL_FROM");
		String toMail = clientSettingService.getValue(clientId, "STOPPED_JOB_EMAIL_TO");
		String fromName = clientSettingService.getValue(clientId, "STOPPED_JOB_EMAIL_FROM_NAME");
		String fromMailDisplay = fromMail;
		return mailBean.sendMail(clientId, fromMail, fromMailDisplay, fromName, toMail, message, subject, true);

	}

	private List<JobDetail> getAllStoppedJobs(List<JobDetail> jobDetails, BigInteger clientId) {
		LocalDateTime nextRunningTime;
		List<JobDetail> allStopedJobs = new ArrayList<>();
		Map<BigInteger, Boolean> jobIdMap = getJobIdMap(clientId);
		for (JobDetail jobDetail : jobDetails) {
			if (jobIdMap.isEmpty() || jobIdMap.containsKey(jobDetail.getId())) {
				if (jobDetail.getIntervalType() == IntervalType.INTERVAL_AFTER_LAST_START) {
					nextRunningTime = addInterval(jobDetail.getLastStartTime(), jobDetail.getInterval()*3);
				} else {
					nextRunningTime = addInterval(jobDetail.getLastEndTime(), jobDetail.getInterval()*3);
				}
				LocalDateTime now = LocalDateTime.now();
				if (now.compareTo(nextRunningTime) >= 0) {
					allStopedJobs.add(jobDetail);
				}
			}
		}
		return allStopedJobs;
	}

	private Map<BigInteger, Boolean> getJobIdMap(BigInteger clientId) {
		Map<BigInteger, Boolean> jobIdMap = new HashMap<>();
		try {
			String jobIds = clientSettingService.getValue(clientId, "STOPPED_JOB_IDS_FOR_MAIL");
			String[] jobIdArr = jobIds.split(",");
			for (String JobId : jobIdArr) {
				jobIdMap.put(new BigInteger(JobId), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
		return jobIdMap;
	}

	private LocalDateTime addInterval(Date date, int minutes) {
		LocalDateTime nextRuntime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		nextRuntime = nextRuntime.plusMinutes(minutes);
		return nextRuntime;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		applicationContext = arg0;
	}

}
