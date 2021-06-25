package org.sunbird.ruleengine.service;

import java.math.BigInteger;

import org.sunbird.ruleengine.model.AbstractSettings;

import org.sunbird.ruleengine.service.GenericService;

public interface AbstractSettingService<T extends AbstractSettings, U extends AbstractSettings>
extends GenericService<T, U> {

	String getValue(BigInteger clientId, String key);

	boolean equal(BigInteger clientId, String key, String value);
}
