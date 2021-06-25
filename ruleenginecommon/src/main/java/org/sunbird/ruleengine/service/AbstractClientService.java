package org.sunbird.ruleengine.service;

import java.math.BigInteger;

import org.sunbird.ruleengine.model.AbstractClient;
import org.sunbird.ruleengine.model.AbstractEntity;

public interface AbstractClientService<T extends AbstractEntity, U extends AbstractEntity> extends GenericService<T, U> {
	
	BigInteger getClientCodeById(String clientCode, BigInteger clientId);
	BigInteger getClientIdByCode(String clientCode);
}
