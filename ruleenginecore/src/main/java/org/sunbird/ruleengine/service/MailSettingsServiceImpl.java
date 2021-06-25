package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.MailSettingsDao;
import org.sunbird.ruleengine.model.MailSettings;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class MailSettingsServiceImpl extends GenericServiceImpl<MailSettings, MailSettings>
		implements MailSettingsService {

	@Autowired
	MailSettingsDao mailSettingsDao;

	@Override
	public AbstractDAO<MailSettings, MailSettings> getDAO() {
		return mailSettingsDao;
	}

}
