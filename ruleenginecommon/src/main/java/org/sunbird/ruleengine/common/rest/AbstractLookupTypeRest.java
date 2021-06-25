package org.sunbird.ruleengine.common.rest;

import org.sunbird.ruleengine.model.AbstractLookupType;

public abstract class AbstractLookupTypeRest<T extends AbstractLookupType, U extends AbstractLookupType> extends GenericMultiTenantRoleBasedSecuredRest<T, U> {

}
