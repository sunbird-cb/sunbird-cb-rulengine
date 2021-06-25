package org.sunbird.ruleengine.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.model.StepSettings;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.StepSettingsService;

import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/stepSettings")
public class StepSettingsRest extends GenericMultiTenantRoleBasedSecuredRest<StepSettings, StepSettings> {
	public StepSettingsRest() {
		super(StepSettings.class,StepSettings.class);
	}
	@Autowired
	StepSettingsService stepSettingsService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<StepSettings, StepSettings> getService() {
		return stepSettingsService;
	}

	@Override
	public GenericService<StepSettings, StepSettings> getUserService() {
		return stepSettingsService;
	}

	@Override
	public String rolePrefix() {
		return "stepSettings";
	}

	@RequestMapping(value = "/getStepSetting", method = RequestMethod.GET)
	public @ResponseBody Object getStepSetting(@PathVariable("clientCode") String clientCode,
			@ModelAttribute StepSettings t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return getService().getListByCriteria(t,
				"select stepSetting.ID, stepSetting.STEP_ID, stepSetting.VALUE, stepSetting.KEY_,stepSetting.CLIENT_ID from STEP_SETTINGS stepSetting where 1=1 ",
				"order by stepSetting.ID desc", firstResult, maxResult);
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute StepSettings t,
			Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return getService().getCount(t, "select count(client.ID) from CLIENT_SETTINGS ").longValue();
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<StepSettings> save(@PathVariable("clientCode") String clientCode,
			@RequestBody StepSettings t, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.save(clientCode, t, principal);
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody Response<StepSettings> update(@PathVariable("clientCode") String clientCode,
			@RequestBody StepSettings t, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.update(clientCode, t, principal);
	}

	

}
