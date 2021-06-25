package org.sunbird.ruleengine.helper;

import org.sunbird.ruleengine.common.util.JobException;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.Step;

public interface StepProcessor {
	Object processStep(JobDetail jobDetail,Step step, ObjectHolder config, Integer level) throws JobException, Exception;
}
	