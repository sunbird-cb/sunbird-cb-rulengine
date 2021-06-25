package org.sunbird.ruleengine.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.model.UserEvent;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.UserEventService;

import org.sunbird.ruleengine.common.rest.AbstractMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.AbstractClientService;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/user-events")
public class UserEventRest extends
AbstractMultiTenantRoleBasedSecuredRest<UserEvent, UserEvent> {
	@Autowired
	ClientService clientService;

	@Autowired
	UserEventService userEventService;

	@Override
	public GenericService<UserEvent, UserEvent> getService() {
		return userEventService;
	}

	@Override
	public GenericService<UserEvent, UserEvent> getUserService() {
		return userEventService;
	}

	@Override
	public String rolePrefix() {
		return "userEvents";
	}

	@Override
	public AbstractClientService<?, ?> getClientService() {
		return clientService;
	}
	/*
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<UserEvent> getListByCriteria(@PathVariable("clientCode")String clientCode,  @ModelAttribute  UserEvent t,
			@RequestParam(value= "firstResult", required=false) int firstResult, @RequestParam(value="maxResult", required=false) int maxResult, Principal principal) {

	//	return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String fromDate="";
		String toDate="";
		if (t.getFromDate()!=null)
		{
			fromDate=	sdf.format(t.getFromDate());
		}
		if (t.getToDate()!=null)
		{
			toDate=	sdf.format(t.getToDate());
		}

		return getService().getListByCriteria(t, "select userLoginEvent.ID,userLoginEvent.FULL_NAME,userLoginEvent.USER_NAME,userLoginEvent.CREATION_DATE from USER_EVENT userLoginEvent where  userLoginEvent.CREATION_DATE BETWEEN nvl(to_date("+fromDate +", 'MM/DD/YYYY HH24:MI:SS'), userLoginEvent.CREATION_DATE) AND nvl(to_date("+toDate +", 'MM/DD/YYYY HH24:MI:SS'), userLoginEvent.CREATION_DATE)  ", " order by userLoginEvent.id desc", firstResult, maxResult, new UserEventMapper());
	 */

	/*	@Override
	@RequestMapping(method = RequestMethod.GET)
	public  @ResponseBody List<UserEvent> getListByCriteria(@PathVariable("clientCode")String clientCode, @ModelAttribute UserEventVo t,@RequestParam(value= "firstResult", required=false) int firstResult, @RequestParam(value="maxResult", required=false) int maxResult,Principal principal){
		//BigDecimal userId = new BigDecimal(2);

			return getService().getListByCriteria(t, "select uE.ID,uE.FULL_NAME,uE.USER_NAME from USER_EVENT uE   ", " order by uE.id desc", firstResult, maxResult, new UserEventMapper());

	}
	 */
}

