package org.sunbird.ruleengine.service;

import org.sunbird.ruleengine.model.ClientTranslation;

import org.sunbird.ruleengine.service.GenericService;

public interface ClientTranslationService extends GenericService<ClientTranslation, ClientTranslation> {

	
	public boolean codeAndKeyValidation(ClientTranslation clientTranslation);
	public boolean codeAndKeyValidationExceptThis(ClientTranslation clientTranslation);
	public boolean uniqueResolutionValidation(ClientTranslation clientTranslation);
	public boolean uniqueResolutionValidationExceptThis(ClientTranslation clientTranslation);
}
