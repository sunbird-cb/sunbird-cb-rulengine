package org.sunbird.ruleengine.job;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.dao.IntegrationInstanceFailureDao;
import org.sunbird.ruleengine.model.Client;
import org.sunbird.ruleengine.model.ErrorPatternMaster;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.ClientSettingsService;
import org.sunbird.ruleengine.service.ErrorPatternMasterService;
import org.sunbird.ruleengine.service.IntegrationInstanceFailureService;
import org.sunbird.ruleengine.service.Job;

@Component
public class IntegrationInstanceFailureCodeUpdateJob implements Job{
	private static final Logger logger = LogManager.getLogger(IntegrationInstanceFailureCodeUpdateJob.class);

	@Autowired
	IntegrationInstanceFailureDao integrationInstanceFailureDao;
	
	@Autowired
	IntegrationInstanceFailureService integrationInstanceFailureService;
	
	@Autowired
	ErrorPatternMasterService errorPatternMasterService;
	
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	ClientSettingsService clientSettingsService;
	
	@Override
	public void runJob() {
		System.out.println("IntegrationInstanceFailureCodeUpdate Cron started");
		logger.info("IntegrationInstanceFailureCodeUpdate Cron started");
		try {
			List<Client> clientList = clientService.getListByCriteria(new Client(), -1, 11);
			for (Client client : clientList) {
				Date endDate = null;
				Date startDate = null;
				if ("YES".equals(clientSettingsService.getValue(client.getId(), "MANUALLY_UPDATE_CODE_IN_INTEGRATION_INSTANCE_FAILURE"))) {
					 endDate = java.sql.Timestamp.valueOf(clientSettingsService.getValue(client.getId(), "END_DATE_TO_UPDATE_CODE_IN_INSTANCE_FAILURE"));
					 startDate = java.sql.Timestamp.valueOf(clientSettingsService.getValue(client.getId(), "START_DATE_TO_UPDATE_CODE_IN_INSTANCE_FAILURE"));
				}
				else {
					LocalDateTime localDate = LocalDateTime.now();
					 endDate = java.sql.Timestamp.valueOf(localDate);
					 startDate = java.sql.Timestamp.valueOf(localDate.minusHours(24));
				}
				
				IntegrationInstanceFailure instanceFailure = new IntegrationInstanceFailure();
				instanceFailure.setClientId(client.getId());
				
				
				List<IntegrationInstanceFailure> integrationFailureList = integrationInstanceFailureDao
						.getIntegrationFailureListByDateOnly(instanceFailure, startDate,
								endDate,-1,0);
				
				ErrorPatternMaster errorPatternMaster = new ErrorPatternMaster();
				errorPatternMaster.setClientId(client.getId());
				List<ErrorPatternMaster> patternList=errorPatternMasterService.getListByCriteria(errorPatternMaster, -1, 0);
				
				for (IntegrationInstanceFailure integrationInstanceFailure : integrationFailureList) {
					integrationInstanceFailure.setErrorCode(getErrorCode(integrationInstanceFailure.getErrorResponse(),patternList));
					integrationInstanceFailureService.update(integrationInstanceFailure);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in IntegrationInstanceFailureCodeUpdate.runJob()");
			logger.info("Exception in IntegrationInstanceFailureCodeUpdate.runJob()");
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
	}

	public String getErrorCode(String errorResponse,List<ErrorPatternMaster> patternList)
	{
	for (ErrorPatternMaster errorPatternMaster2 : patternList) {
		Pattern p = Pattern.compile(errorPatternMaster2.getPattern());
		Matcher m = p.matcher(errorResponse);   // get a matcher object
			if( m.matches()){
			    	 return errorPatternMaster2.getErrorCode();
			   }
	}
			return "Default";
	}
	
	
	
	
	
	
}
