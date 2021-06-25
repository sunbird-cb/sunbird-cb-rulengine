package org.sunbird.ruleengine.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.model.TranslationMapper;
import org.sunbird.ruleengine.service.TranslatioinMapperService;

import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping(value="{clientCode}/translationMapper")
public class TranslationMapperRest extends GenericMultiTenantRoleBasedSecuredRest<TranslationMapper, TranslationMapper>{
	
	public TranslationMapperRest() {
	super(TranslationMapper.class,TranslationMapper.class);
	}

	@Autowired
	TranslatioinMapperService translationMapperService;
	
	@Override
	public GenericService<TranslationMapper, TranslationMapper> getService() {
		return translationMapperService;
	}

	@Override
	public GenericService<TranslationMapper, TranslationMapper> getUserService() {
		return translationMapperService;
	}

	@Override
	public String rolePrefix() {
		return "translationMapper";
	}

}
