package org.sunbird.ruleengine.service;

import java.math.BigInteger;

import org.sunbird.ruleengine.model.Step;

import org.sunbird.ruleengine.service.GenericService;

public interface StepService extends GenericService<Step, Step> {
	public BigInteger getLastStepId(BigInteger jobDetailId);
}
