package org.sunbird.ruleengine.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.model.OipGlobalVariable;
import org.sunbird.ruleengine.model.Step;
import org.sunbird.ruleengine.service.OipGlobalVariableService;
import org.sunbird.ruleengine.service.StepSettingsService;

@Component
public class GlobalVariableRetrieveHandler implements GlobalVariableHandlers {

	@Autowired
	NashornHelper nashornHelper;

	@Autowired
	StepSettingsService stepSettingsService;

	@Autowired
	TemplateParser templateParser;

	@Autowired
	OipGlobalVariableService oipGlobalVariableService;

	private static final Logger logger = LogManager.getLogger(GlobalVariableRetrieveHandler.class);

	@Override
	public Map processVariable(Step step, ObjectHolder objectHolder, Integer level) {

		Map<String, Object> status = new HashMap<>();
		Map<String, Object> requestResponse = new HashMap<>();

		if (step.getRequestTemplate() != null && !step.getRequestTemplate().trim().isEmpty()) {

			String[] parameters = templateParser.parse(step.getClientId().toString(),
					"step_globalvariable_create" + step.getId().toString(), step.getRequestTemplate(), objectHolder)
					.split("\\|");

			for (int i = 0; i < parameters.length; i++) {

				String Key = parameters[i];
				OipGlobalVariable globalVariable = new OipGlobalVariable();
				globalVariable.setClientId(step.getClientId());
				globalVariable.setJobDetailId(step.getJobDetailId());
				globalVariable.setGlobalVariableName(Key);
				List<OipGlobalVariable> globalVariableData = oipGlobalVariableService.getListByCriteria(globalVariable,
						-1, 0);

				if (!globalVariableData.isEmpty()) {

					for (OipGlobalVariable globalVariableUpdate : globalVariableData) {

							
							logger.info("Data retrieval Success");
							status.put("success", true);
							requestResponse.put(globalVariableUpdate.getGlobalVariableName(),globalVariableUpdate.getGlobalVariableValue());

					}

				}

				else {

					logger.info("The table is empty for the given Key");
					status.put("success", true);
					requestResponse.put(Key,"The table is empty for the given Key");

				}

			}
			
			status.put("request" , requestResponse.keySet()); 
			status.put("response" , requestResponse.values()); 
			
			return status;
		}
		
		else {
			
			logger.info("Request Template is empty or undefined");
			status.put("success", false);
			status.put("request", step.getRequestTemplate());
			status.put("response", "Request Template is empty or undefined");
			return status;
			
		}

	}

}