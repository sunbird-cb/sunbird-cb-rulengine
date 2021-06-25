package org.sunbird.ruleengine.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.dao.OipGlobalVariableDAO;
import org.sunbird.ruleengine.model.OipGlobalVariable;
import org.sunbird.ruleengine.model.Step;
import org.sunbird.ruleengine.service.OipGlobalVariableService;

@Component
public class GlobalVariableCreateHandler implements GlobalVariableHandlers {

	@Autowired
	OipGlobalVariableService oipGlobalVariableService;

	@Autowired
	OipGlobalVariableDAO oipGlobalVariableDAO;

	@Autowired
	TemplateParser templateParser;

	private static final Logger logger = LogManager.getLogger(GlobalVariableCreateHandler.class);

	@Override
	public Map processVariable(Step step, ObjectHolder objectHolder, Integer level) {

		Map<String, Object> status = new HashMap<>();
		Map<String, Object> requestResponse = new HashMap<>();

		if (step.getRequestTemplate() != null && !step.getRequestTemplate().trim().isEmpty()) {

			String[] parameters = templateParser.parse(step.getClientId().toString(),
					"step_globalvariable_create" + step.getId().toString(), step.getRequestTemplate(), objectHolder)
					.split("\\|");

			for (int i = 0; i < parameters.length; i++) {

				String[] KeyValues = parameters[i].split("\\:");
				OipGlobalVariable globalVariable = new OipGlobalVariable();
				globalVariable.setJobDetailId(step.getJobDetailId());
				globalVariable.setClientId(step.getClientId());
				globalVariable.setGlobalVariableName(KeyValues[0]);

				List<OipGlobalVariable> globalVariableData = oipGlobalVariableService.getListByCriteria(globalVariable,
						-1, 0);
				if (!globalVariableData.isEmpty()) {

					for (OipGlobalVariable globalVariableUpdate : globalVariableData) {

						String SearchKeys = globalVariableUpdate.getGlobalVariableName();

						if (SearchKeys.equals(KeyValues[0])) {

							globalVariableUpdate.setJobDetailId(step.getJobDetailId());
							globalVariableUpdate.setClientId(step.getClientId());
							globalVariableUpdate.setGlobalVariableName(SearchKeys);
							globalVariableUpdate.setGlobalVariableValue(new String(KeyValues[1]));
							globalVariableUpdate.setLastUpdateDate(new Date());
							globalVariableUpdate.setId(globalVariableUpdate.getId());
							oipGlobalVariableService.update(globalVariableUpdate);
							logger.info("values got updated");
							status.put("success", true);
							requestResponse.put(SearchKeys,KeyValues[1]);

						}

						else {

							OipGlobalVariable globalVariableCreate = new OipGlobalVariable();
							globalVariableCreate.setJobDetailId(step.getJobDetailId());
							globalVariableCreate.setClientId(step.getClientId());
							globalVariableCreate.setGlobalVariableName(KeyValues[0]);
							globalVariableCreate.setGlobalVariableValue(new String(KeyValues[1]));
							globalVariableCreate.setCreationDate(new Date());
							globalVariableCreate.setLastUpdateDate(new Date());
							oipGlobalVariableService.save(globalVariableCreate);
							logger.info("values got created");
							status.put("success", true);
							requestResponse.put(KeyValues[0],KeyValues[1]);

						}

					}

				}

				else {

					OipGlobalVariable globalVariableNew = new OipGlobalVariable();
					globalVariableNew.setJobDetailId(step.getJobDetailId());
					globalVariableNew.setClientId(step.getClientId());
					globalVariableNew.setGlobalVariableName(KeyValues[0]);
					globalVariableNew.setGlobalVariableValue(new String(KeyValues[1]));
					globalVariableNew.setCreationDate(new Date());
					globalVariableNew.setLastUpdateDate(new Date());
					oipGlobalVariableService.save(globalVariableNew);
					logger.info("values got created");
					status.put("success", true);
					requestResponse.put(KeyValues[0],KeyValues[1]);

				}

			}
			
			status.put("request", requestResponse);
			status.put("response", requestResponse.values());
			return status;

		}

		logger.info("Request Template is empty or undefined");
		status.put("success", false);
		status.put("request", step.getRequestTemplate());
		status.put("response", "Request Template is empty or undefined");
		return status;

	}

}