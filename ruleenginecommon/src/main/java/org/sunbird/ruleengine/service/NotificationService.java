package org.sunbird.ruleengine.service;

import java.math.BigInteger;

import org.sunbird.ruleengine.model.Notification;
import org.sunbird.ruleengine.vo.NotificationVo;


public interface NotificationService extends AbstractNotificationService<Notification, NotificationVo> {

	public Integer getNoOfNotificationCount(BigInteger userId);
	public void getNotificationUpdate(BigInteger userId);
	public void getNotificationRead(BigInteger id);
}
