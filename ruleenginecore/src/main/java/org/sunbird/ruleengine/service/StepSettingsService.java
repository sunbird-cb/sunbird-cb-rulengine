package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.List;

import org.sunbird.ruleengine.model.StepSettings;

public interface StepSettingsService extends AbstractSettingService<StepSettings, StepSettings> {

	String getValue(BigInteger clientId, BigInteger stepId, String key);

	List<StepSettings> getStepSettings(BigInteger clientId, BigInteger stepId);

	boolean equal(BigInteger clientId, BigInteger stepId, String key, String value);

	List<StepSettings> getStepSettings(BigInteger clientId, BigInteger stepId, String key);

}
