package org.sunbird.ruleengine.common.rest;


import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.model.AbstractRolePermissionJoin;

public abstract class AbstractRolePermissionJoinRest<T extends AbstractRolePermissionJoin, U extends AbstractRolePermissionJoin> extends GenericMultiTenantRoleBasedSecuredRest<T, U>{

}
