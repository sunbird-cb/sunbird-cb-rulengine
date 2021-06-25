package org.sunbird.ruleengine.common.rest;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.dao.NotificationDAO;
import org.sunbird.ruleengine.model.Notification;
import org.sunbird.ruleengine.service.GenericService;
import org.sunbird.ruleengine.service.NotificationService;
import org.sunbird.ruleengine.vo.NotificationVo;

@RestController
@RequestMapping("{clientCode}/notification")
public class NotificationRest extends GenericMultiTenantRoleBasedSecuredRest<Notification, NotificationVo>{

	
	public NotificationRest() {
		super(Notification.class, NotificationVo.class);
	}

	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	NotificationDAO notificationDao;
	
	@Override
	public GenericService<Notification, NotificationVo> getService() {
		
		return notificationService;
	}

	@Override
	public GenericService<Notification, NotificationVo> getUserService() {
		
		return notificationService;
	}

	@Override
	public String rolePrefix() {
		
		return "notification";
	}
	
	@RequestMapping(value = "/getNotificationCount/{userId}", method = RequestMethod.GET)
	  public @ResponseBody Integer getNotificationCount(@PathVariable("userId") BigInteger userId){
		return notificationService.getNoOfNotificationCount(userId);
	  }
	

	@RequestMapping(value = "/updateNotification/{userId}", method = RequestMethod.PUT)
	  public @ResponseBody Response<Notification> updateNotification(@PathVariable("userId") BigInteger userId){
		notificationService.getNotificationUpdate(userId);
		 return new Response<Notification>(true, null);
	  }
	
	@RequestMapping(value = "/readNotification/{id}", method = RequestMethod.PUT)
	  public @ResponseBody Response<Notification> readNotification(@PathVariable("id") BigInteger id){
		notificationService.getNotificationRead(id);
		 return new Response<Notification>(true, null);
	  }
	
	
	
	
	
}
