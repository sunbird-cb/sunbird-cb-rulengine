package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.UserEventDao;
import org.sunbird.ruleengine.model.UserEvent;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class UserEventServiceImpl extends
GenericServiceImpl<UserEvent, UserEvent> implements
UserEventService {

	@Autowired
	UserEventDao userEventDao;

	@Override
	public AbstractDAO<UserEvent, UserEvent> getDAO() {
		return userEventDao;
	}

}
