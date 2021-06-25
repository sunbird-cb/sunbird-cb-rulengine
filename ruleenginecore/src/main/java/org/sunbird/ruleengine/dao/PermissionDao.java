package org.sunbird.ruleengine.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.Permission;
import org.sunbird.ruleengine.vo.PermissionVo;

import org.sunbird.ruleengine.dao.AbstractPermissionDAO;

@Repository
public class PermissionDao extends AbstractPermissionDAO<Permission, PermissionVo>{

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<Permission> getClassType() {
		return Permission.class;
	}

	@SuppressWarnings("unchecked")
	public List<PermissionVo> getPermissions(String permission)
	{
		
		List<PermissionVo> resultList= new ArrayList<>();
		List<String> list= new ArrayList<>();
		String query="SELECT permission.permission_name FROM PERMISSION permission inner join ROLE_PERMISSION_JOIN rp on rp.permission_id=permission.Id inner join USER_ROLE_JOIN ur on ur.ROLE_ID=rp.role_id  where 1=1 and ur.USER_ID=" +permission ;
		Query queryJpa=entityManager.createNativeQuery(query);
		list=queryJpa.getResultList();
		for(String object:list)
		{
			PermissionVo permissionVo= new PermissionVo();
			permissionVo.setFunctionName(object);
			resultList.add(permissionVo);
		}
		return resultList;
	}

}
