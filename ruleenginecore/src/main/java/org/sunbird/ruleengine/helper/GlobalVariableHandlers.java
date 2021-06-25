package org.sunbird.ruleengine.helper;


import java.util.Map;

import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.contracts.ObjectHolder;
import org.sunbird.ruleengine.model.Step;

@Component
public interface GlobalVariableHandlers {
	
	public Map processVariable(Step step,ObjectHolder objectHolder, Integer level);

}