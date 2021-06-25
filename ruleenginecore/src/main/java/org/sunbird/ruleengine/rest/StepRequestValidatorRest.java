package org.sunbird.ruleengine.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.model.StepRequestValidator;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.GenericService;
import org.sunbird.ruleengine.service.StepRequestValidatorService;

@RestController
@RequestMapping("{clientCode}/stepRequestValidatorRest")
public class StepRequestValidatorRest extends GenericMultiTenantRoleBasedSecuredRest<StepRequestValidator, StepRequestValidator> {
	
	public StepRequestValidatorRest() {
	super(StepRequestValidator.class,StepRequestValidator.class);
	}

	@Autowired
	StepRequestValidatorService stepRequestValidatorService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<StepRequestValidator, StepRequestValidator> getService() {
		return stepRequestValidatorService;
	}

	@Override
	public GenericService<StepRequestValidator, StepRequestValidator> getUserService() {
		return stepRequestValidatorService;
	}

	@Override
	public String rolePrefix() {
		return "stepRequestValidator";
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<StepRequestValidator> getListByCriteria(@PathVariable("clientCode") String clientCode,
			@ModelAttribute StepRequestValidator t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
	}
	

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute StepRequestValidator t,
			Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));

		return super.count(clientCode, t, principal);

	}
	
	@RequestMapping(value = "/getstepRequestEnums", method = RequestMethod.GET)
	public @ResponseBody Object getStepType(@PathVariable("clientCode") String clientCode, Principal principal) {
		Object[] object = new Object[1];
		object[0] = StepRequestValidator.ResultFlag.values();
		return object;
	}
	
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody Response<StepRequestValidator> update(@PathVariable("clientCode") String clientCode,
			 @RequestBody StepRequestValidator t, Principal principal) {
		return super.update(clientCode, t, principal);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<StepRequestValidator> save(@PathVariable("clientCode") String clientCode,  @RequestBody StepRequestValidator t,
			Principal principal) {
		return super.save(clientCode, t, principal);
	}
}
