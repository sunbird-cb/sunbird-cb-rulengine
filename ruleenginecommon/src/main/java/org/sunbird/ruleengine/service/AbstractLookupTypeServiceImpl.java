package org.sunbird.ruleengine.service;

import org.sunbird.ruleengine.model.AbstractLookupType;

public abstract class AbstractLookupTypeServiceImpl<T extends AbstractLookupType, U extends AbstractLookupType> extends GenericServiceImpl<T, U> implements AbstractLookupTypeService<T, U> {

}
