package org.sunbird.ruleengine.common.rest;

import org.sunbird.ruleengine.model.AbstractNotification;

public abstract class AbstractNotificationRest <T extends AbstractNotification, U extends AbstractNotification> extends AbstractMultiTenantRoleBasedSecuredRest<T, U> {

	public AbstractNotificationRest(Class<T> class1,
			Class<U> class2) {
		super(class1,class2);
	}
	

	@Override
	public String getSearchQuery() {
		
		return "select notification.id,notification.notified, notification.notify_to,notification.notification_from,notification.notification_reference_id,users.first_Name,users.Last_name,notification.notification_time,notification.notification_type,notification.notification_text,notification.read from NOTIFICATION notification  left join USER_ users on(users.id=notification.notification_from) where 1=1 ";
	}

	@Override
	public String getSearchOrderBy() {
		
		return " order by notification.id desc";
	}
}
