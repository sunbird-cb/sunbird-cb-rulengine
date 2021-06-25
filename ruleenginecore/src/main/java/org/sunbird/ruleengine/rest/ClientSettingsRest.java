package org.sunbird.ruleengine.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.model.ClientSettings;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.ClientSettingsService;
import org.sunbird.ruleengine.vo.ClientSettingsVo;

import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/clientSettings")
public class ClientSettingsRest extends GenericMultiTenantRoleBasedSecuredRest<ClientSettings, ClientSettingsVo> {
	
	public ClientSettingsRest() {
		super(ClientSettings.class,ClientSettingsVo.class);
	}
	
	@Autowired
	ClientSettingsService clientSettingsService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<ClientSettings, ClientSettingsVo> getService() {
		return clientSettingsService;
	}

	@Override
	public GenericService<ClientSettings, ClientSettingsVo> getUserService() {
		return clientSettingsService;
	}

	@Override
	public String rolePrefix() {
		return "clientSettings";
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public  @ResponseBody Response<ClientSettings> save(@PathVariable("clientCode")String clientCode, @RequestBody ClientSettings t,Principal principal){
		return super.save(clientCode,t,principal);
	}


	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public  @ResponseBody Response<ClientSettings> update(@PathVariable("clientCode")String clientCode, @RequestBody ClientSettings t,Principal principal){
		return super.update(clientCode,t,principal);
	}




}
