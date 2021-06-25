package org.sunbird.ruleengine.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.common.util.JsonUtil;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.model.ClientTranslation;
import org.sunbird.ruleengine.model.Step;
import org.sunbird.ruleengine.model.StepSettings;
import org.sunbird.ruleengine.service.ClientTranslationService;

@Component
public class ClientTranslationHelper {

	@Autowired
	TemplateParser templateParser;

	@Autowired
	ClientTranslationService clientTranslationService;
	
	@Autowired
	NashornHelper nashornHelper;

	public void fillTranslatorObject(Step step, List<StepSettings> stepSettings, ObjectHolder objectHolder,
			Integer level) throws TranslatorNotFoundException {
		Map<String, Map<String, Object>> converterMap = new HashMap<>();
		for (StepSettings stepSettingsSingle : stepSettings) {
			if (stepSettingsSingle.getKey().endsWith("_TRANSLATOR_TEMPLATE")) {
				String stransaltorValues = templateParser.parse(step.getClientId().toString(),
						"STEP_SETTING_" + stepSettingsSingle.getId(), stepSettingsSingle.getValue(), objectHolder);
				for (String value : stransaltorValues.split(",")) {
					String stepCode = stepSettingsSingle.getKey().replace("_TRANSLATOR_TEMPLATE", "");
					ClientTranslation stepTranslation = new ClientTranslation();
					stepTranslation.setClientId(step.getClientId());
					stepTranslation.setCode(stepCode);
					stepTranslation.setKey(value);
					// stepTranslation =
					// clientTranslationService.getListByCriteria(stepTranslation,
					// 0, 1).get(0);
					List<ClientTranslation> clientTranslationList = clientTranslationService
							.getListByCriteria(stepTranslation, 0, 1);
					if (!clientTranslationList.isEmpty()) {
						stepTranslation = clientTranslationList.get(0);
						if (converterMap.get(stepCode) == null) {
							converterMap.put(stepCode, new HashMap<String, Object>());
						}
						converterMap.get(stepCode).put(stepTranslation.getKey(), stepTranslation.getValue());
						objectHolder.getConfigs().get("" + level).getTranslatorMap().put("" + step.getSequence(),
								converterMap);
					} else {
						TranslatorNotFoundException translatorNotFoundException = new TranslatorNotFoundException(
								"Translator Not Found");
						translatorNotFoundException.setTranslatorCode(value);
						throw translatorNotFoundException;
					}
				}
			}
			if (stepSettingsSingle.getKey().endsWith("_TRANSLATOR_NASHORN")) {
				Object object=	nashornHelper.process(stepSettingsSingle.getValue(), objectHolder,objectHolder);
					if (object!=null) {
						objectHolder.getConfigs().get("" + level).getTranslatorMap().put("" + step.getSequence(),
								(Map<String, Map<String, Object>>) object);
					}
					 else {
						TranslatorNotFoundException translatorNotFoundException = new TranslatorNotFoundException(
								"Translator Not Found");
						throw translatorNotFoundException;
					}
			}
		}
	}

}
