package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.List;

import org.sunbird.ruleengine.model.JobSettings;

public interface JobSettingsService extends AbstractSettingService<JobSettings, JobSettings> {

	String getValue(BigInteger clientId, BigInteger jobId, String key);

	List<JobSettings> getJobSettings(BigInteger clientId, BigInteger jobId);

	boolean equal(BigInteger clientId, BigInteger jobId, String key, String value);
}
