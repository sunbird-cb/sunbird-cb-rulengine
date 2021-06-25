package org.sunbird.ruleengine.service;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.StepSettingsDao;
import org.sunbird.ruleengine.model.StepSettings;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Service
@Transactional
public class StepSettingsServiceImpl extends AbstractSettingServiceImpl<StepSettings, StepSettings>
		implements StepSettingsService {

	@Autowired
	StepSettingsDao stepSettingsDao;

	@Override
	public AbstractDAO<StepSettings, StepSettings> getDAO() {
		return stepSettingsDao;
	}

	@Override
	protected StepSettings intantiatateVo() {
		return new StepSettings();
	}

	@Override
	public List<StepSettings> getStepSettings(BigInteger clientId, BigInteger stepId) {
		StepSettings u = intantiatateVo();
		u.setClientId(clientId);
		// u.setKey(key);
		u.setStepId(stepId);
		return getDAO().getListByCriteria(u, 0, Integer.MAX_VALUE);
	}

	@Override
	public String getValue(BigInteger clientId, BigInteger stepId, String key) {
		StepSettings u = intantiatateVo();
		u.setClientId(clientId);
		u.setKey(key);
		u.setStepId(stepId);
		return getDAO().getListByCriteria(u, 0, 1).get(0).getValue();
	}

	@Override
	public boolean equal(BigInteger clientId, BigInteger stepId, String key, String value) {
		StepSettings u = intantiatateVo();
		u.setClientId(clientId);
		u.setKey(key);
		u.setValue(value);
		u.setStepId(stepId);
		return getDAO().getCount(u) > 0;
	}

	@Override
	public List<StepSettings> getStepSettings(BigInteger clientId, BigInteger stepId, String key) {
		StepSettings u = intantiatateVo();
		u.setClientId(clientId);
		u.setKey(key);
		u.setStepId(stepId);
		return getDAO().getListByCriteria(u, 0, 1);
	}

}
