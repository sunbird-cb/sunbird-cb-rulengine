package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.ClientSettingsDao;
import org.sunbird.ruleengine.model.ClientSettings;
import org.sunbird.ruleengine.vo.ClientSettingsVo;

import org.sunbird.ruleengine.dao.AbstractDAO;

@Service
@Transactional
public class ClientSettingsServiceImpl extends AbstractSettingServiceImpl<ClientSettings, ClientSettingsVo> implements ClientSettingsService {

	@Autowired
	ClientSettingsDao adClientSettingsDao;

	@Override
	public AbstractDAO<ClientSettings, ClientSettingsVo> getDAO() {
		return adClientSettingsDao;
	}

	@Override
	protected ClientSettingsVo intantiatateVo() {
		return new ClientSettingsVo();
	}




}
