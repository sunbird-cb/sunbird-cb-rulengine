package org.sunbird.ruleengine.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.model.Mail;
import org.sunbird.ruleengine.service.MailService;

import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/mailRest")
public class mailRest extends GenericMultiTenantRoleBasedSecuredRest<Mail, Mail> {

	public mailRest() {
	super(Mail.class,Mail.class);
	}
	

	@Autowired
	MailService mailService;
 
	@Autowired
	org.sunbird.ruleengine.service.ClientService clientService;

	@Override
	public GenericService<Mail, Mail> getService() {
		return mailService;
	}

	@Override
	public GenericService<Mail, Mail> getUserService() {
		return mailService;
	}

	@Override
	public String rolePrefix() {
		return "mail";
	}

	
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<Mail> getListByCriteria(@PathVariable("clientCode") String clientCode,
			@ModelAttribute Mail t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public Long count(@PathVariable("clientCode") String clientCode,@ModelAttribute Mail t, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.count(clientCode, t, principal);
	}
	

}
