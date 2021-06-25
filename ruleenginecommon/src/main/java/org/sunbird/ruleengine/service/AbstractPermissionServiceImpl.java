package org.sunbird.ruleengine.service;




import org.sunbird.ruleengine.model.AbstractPermission;
import org.sunbird.ruleengine.service.GenericServiceImpl;

public abstract class AbstractPermissionServiceImpl<T extends AbstractPermission, U extends AbstractPermission> extends GenericServiceImpl<T, U> implements AbstractPermissionService<T, U>  {

}
