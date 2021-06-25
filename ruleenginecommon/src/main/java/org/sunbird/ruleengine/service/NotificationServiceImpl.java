package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.dao.NotificationDAO;
import org.sunbird.ruleengine.model.Notification;
import org.sunbird.ruleengine.vo.NotificationVo;

@Service
@Transactional
public class NotificationServiceImpl extends AbstractNotificationServiceImpl<Notification , NotificationVo> implements NotificationService{

	@Autowired
	NotificationDAO notificationDAO;
	

	@Override
	public AbstractDAO<Notification, NotificationVo> getDAO() {
		
		return notificationDAO;
	}
	
	@Override
	public Integer getNoOfNotificationCount(BigInteger userId) {
		
		return notificationDAO.getNoOfNotificationCount(userId);
	}

	@Override
	public void getNotificationUpdate(BigInteger userId) {
	
		List<Notification> notificationList= notificationDAO.getListByColumnNameAndValue(Notification.class, "notifyTo", userId);
		for (Notification notification : notificationList) {
			notification.setNotified(true);
			notificationDAO.update(notification);
		}
	}
	
	@Override
	public void getNotificationRead(BigInteger id) {
	
		List<Notification> notificationList= notificationDAO.getListByColumnNameAndValue(Notification.class, "id", id);
		for (Notification notification : notificationList) {
			notification.setRead(true);
			notificationDAO.update(notification);
		}
	}
	

}
