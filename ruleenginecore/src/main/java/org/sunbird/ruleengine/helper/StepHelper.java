package org.sunbird.ruleengine.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.Step;

@Component
public class StepHelper {

	@Autowired
	Map<String, StepProcessor> stepProcessors = new HashMap<>();

	public Object processStep(JobDetail jobDetail, Step step, ObjectHolder config, Integer level) throws Exception {
		return stepProcessors.get(step.getType().toString().toLowerCase() + "StepProcessor").processStep(jobDetail,step, config, level);
	}

}
