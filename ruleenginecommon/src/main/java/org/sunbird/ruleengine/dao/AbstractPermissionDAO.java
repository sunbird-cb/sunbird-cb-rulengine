package org.sunbird.ruleengine.dao;


import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.model.AbstractPermission;


@Repository
public abstract class AbstractPermissionDAO<T extends AbstractPermission, U extends AbstractPermission> extends AbstractDAO<T, U>{

}
