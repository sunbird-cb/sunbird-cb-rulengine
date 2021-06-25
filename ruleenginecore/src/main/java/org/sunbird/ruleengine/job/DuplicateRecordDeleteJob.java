package org.sunbird.ruleengine.job;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.dao.IntegrationInstanceFailureDao;
import org.sunbird.ruleengine.helper.TemplateParser;
import org.sunbird.ruleengine.model.Client;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;
import org.sunbird.ruleengine.model.StepSettings;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.ClientSettingsService;
import org.sunbird.ruleengine.service.IntegrationInstanceFailureService;
import org.sunbird.ruleengine.service.Job;
import org.sunbird.ruleengine.service.StepSettingsService;

@Component
public class DuplicateRecordDeleteJob implements Job {
	private static final Logger logger = LogManager.getLogger(DuplicateRecordDeleteJob.class);
	@Autowired
	ClientService clientService;

	@Autowired
	ClientSettingsService clientSerService;

	@Autowired
	StepSettingsService stepSettingService;

	@Autowired
	IntegrationInstanceFailureService integrationInstanceFailureService;

	@Autowired 
	IntegrationInstanceFailureDao integrationInstanceFailureDao;

	@Autowired
	TemplateParser templateParser;

	@Override
	public void runJob() {
		Client clnt = new Client();
		try {
			List<Client> clientList = clientService.getListByCriteria(clnt, -1, 17);
			for (Client client : clientList) {
				StepSettings clientSettings = new StepSettings();
				clientSettings.setClientId(client.getId());
				clientSettings.setKey("FAILED_RECORD_DELETION_KEY");
				List<StepSettings> stepSettings = stepSettingService.getListByCriteria(clientSettings, -1, 0);
				for (StepSettings stepSetting : stepSettings) {
					IntegrationInstanceFailure integrationInstanceFailure = new IntegrationInstanceFailure();
					integrationInstanceFailure.setStepId(stepSetting.getStepId());
					integrationInstanceFailure.setErrorResponse(stepSetting.getValue());
					integrationInstanceFailure.setDone(false);

					List<IntegrationInstanceFailure> integrationInstanceFailures = integrationInstanceFailureDao
							.getIntegrationFailureList(integrationInstanceFailure);
					for (IntegrationInstanceFailure integrationInstanceFailure2 : integrationInstanceFailures) {
						integrationInstanceFailure2.setDone(true);
						integrationInstanceFailureService.update(integrationInstanceFailure2);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
	}

}
