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
import org.sunbird.ruleengine.model.MailSettings;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.GenericService;
import org.sunbird.ruleengine.service.MailSettingsService;

@RestController
@RequestMapping("{clientCode}/mailSettingsRest")
public class MailSettingsRest extends GenericMultiTenantRoleBasedSecuredRest<MailSettings, MailSettings> {
	
	public MailSettingsRest() {
	super(MailSettings.class,MailSettings.class);
	}

	@Autowired
	MailSettingsService mailSettingsService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<MailSettings, MailSettings> getService() {
		return mailSettingsService;
	}

	@Override
	public GenericService<MailSettings, MailSettings> getUserService() {
		return mailSettingsService;
	}

	@Override
	public String rolePrefix() {
		return "mailSettings";
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<MailSettings> getListByCriteria(@PathVariable("clientCode") String clientCode,
			@ModelAttribute MailSettings t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
	}
	
	
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody Response<MailSettings> update(@PathVariable("clientCode") String clientCode,
			 @RequestBody MailSettings t, Principal principal) {
		return super.update(clientCode, t, principal);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<MailSettings> save(@PathVariable("clientCode") String clientCode,  @RequestBody MailSettings t,
			Principal principal) {
		return super.save(clientCode, t, principal);
	}
}
