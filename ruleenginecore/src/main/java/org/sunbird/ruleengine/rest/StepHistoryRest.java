package org.sunbird.ruleengine.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.model.StepHistory;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.GenericService;
import org.sunbird.ruleengine.service.StepHistoryService;

@RestController
@RequestMapping("{clientCode}/stepHistoryRest")
public class StepHistoryRest extends GenericMultiTenantRoleBasedSecuredRest<StepHistory, StepHistory> {
	
	public StepHistoryRest() {
	super(StepHistory.class,StepHistory.class);
	}

	@Autowired
	StepHistoryService stepHistoryService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<StepHistory, StepHistory> getService() {
		return stepHistoryService;
	}

	@Override
	public GenericService<StepHistory, StepHistory> getUserService() {
		return stepHistoryService;
	}

	@Override
	public String rolePrefix() {
		return "StepHistory";
	}

	@RequestMapping(value = "/getStepHistoryEnums", method = RequestMethod.GET)
	public @ResponseBody Object getStepHistoryEnums(@PathVariable("clientCode") String clientCode,
			Principal principal) {
		Object[] objects = new Object[1];
		objects[0] = StepHistory.Status.values();
		return objects;
	}

	@RequestMapping(value = "/getStepHistory", method = RequestMethod.GET)
	public @ResponseBody Object getStepHistory(@PathVariable("clientCode") String clientCode,
			@ModelAttribute StepHistory t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		if (t.getLastUpdateDate() != null) {
			t.setLastUpdateDate(new java.sql.Date(t.getLastUpdateDate().getTime()));
		} 
		super.validateAuthorization(clientCode, principal);
		return getService().getListByCriteria(t,
				"select igFail.ID, igFail.STEP_ID, igFail.STEP_ID as config_object_json, igFail.RESPONSE, igFail.LAST_UPDATE_DATE, igFail.STEP_HISTORY_STATUS, igFail.HEADER, igFail.REQUEST, igFail.JOB_ID, igFail.UNIQUE_KEY_TEMPLATE from STEP_HISTORY igFail where 1=1 ",
				"order by igFail.ID desc", firstResult, maxResult);
	}

	
	
	
	
	@Override
	@RequestMapping( method = RequestMethod.GET)
	public List<StepHistory> getListByCriteria(@PathVariable("clientCode") String clientCode, @ModelAttribute  StepHistory t,@RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute StepHistory t,
			Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		if (t.getLastUpdateDate() != null) {
			t.setLastUpdateDate(new java.sql.Date(t.getLastUpdateDate().getTime()));
		} 
		return getService().getCount(t, "select count(igFail.ID) from STEP_HISTORY igFail where 1=1 ").longValue();
	}

}
