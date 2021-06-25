package org.sunbird.ruleengine.contracts;

import java.util.HashMap;
import java.util.Map;

public class Config {

	Config parent;

	Object currentResponse;
	
	Map<String, Map<String, Map<String, Object> >> translatorMap = new HashMap<>();

	Map<String, Object> values = new HashMap<>();

	public Config getParent() {
		return parent;
	}

	public void setParent(Config parent) {
		this.parent = parent;
	}

	public Object getCurrentResponse() {
		return currentResponse;
	}

	public void setCurrentResponse(Object currentResponse) {
		this.currentResponse = currentResponse;
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}

	public Map<String, Map<String, Map<String, Object>>> getTranslatorMap() {
		return translatorMap;
	}

	public void setTranslatorMap(Map<String, Map<String, Map<String, Object>>> translatorMap) {
		this.translatorMap = translatorMap;
	}
	
	

}
