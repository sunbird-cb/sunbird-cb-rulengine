package org.sunbird.ruleengine.mapper;

import org.sunbird.ruleengine.model.Notification;

public class NotificationMapper extends AbstractNotificationMapper<Notification>{

	@Override
	public Notification instantiateVo() {
		
		return new Notification();
	}

}
