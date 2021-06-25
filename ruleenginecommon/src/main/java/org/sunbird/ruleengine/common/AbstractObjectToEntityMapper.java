package org.sunbird.ruleengine.common;

public interface AbstractObjectToEntityMapper<T> {
	T map(Object returnObject);
}
