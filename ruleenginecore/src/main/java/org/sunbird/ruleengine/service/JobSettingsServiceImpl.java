package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.JobSettingsDao;
import org.sunbird.ruleengine.model.JobSettings;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Service
@Transactional
public class JobSettingsServiceImpl extends AbstractSettingServiceImpl<JobSettings, JobSettings> implements JobSettingsService{

	@Autowired
	JobSettingsDao jobSettingsDao;

	@Override
	public AbstractDAO<JobSettings, JobSettings> getDAO() {
		return jobSettingsDao;
	}

	@Override
	protected JobSettings intantiatateVo() {
		return new JobSettings();
	}

	@Override
	public String getValue(BigInteger clientId, BigInteger jobId, String key) {
		JobSettings u = intantiatateVo();
		u.setClientId(clientId);
		u.setKey(key);
		u.setJobDetailId(jobId);
		return getDAO().getListByCriteria(u, 0, 1).get(0).getValue();
	}

	@Override
	public List<JobSettings> getJobSettings(BigInteger clientId, BigInteger jobId) {
		JobSettings u = intantiatateVo();
		u.setClientId(clientId);
		u.setJobDetailId(jobId);
		return getDAO().getListByCriteria(u, 0, Integer.MAX_VALUE);
	}

	@Override
	public boolean equal(BigInteger clientId, BigInteger jobId, String key, String value) {
		JobSettings u = intantiatateVo();
		u.setClientId(clientId);
		u.setKey(key);
		u.setValue(value);
		u.setJobDetailId(jobId);
		return getDAO().getCount(u) > 0;
	}

}
