package org.sunbird.ruleengine.mapper;



import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.model.AbstractRolePermissionJoin;

public abstract class AbstractRolePermissionJoinMapper<T extends AbstractRolePermissionJoin> implements AbstractObjectToEntityMapper<List<T>>{

	@SuppressWarnings("unchecked")
	@Override
	public List<T> map(Object returnObject) {
	    List<T> resultList= new ArrayList<>();
		for(Object[] object:((List<Object[]>)returnObject))
		{
			T abstractRolePermissionJoinVo= instantiateVo();
			abstractRolePermissionJoinVo.setId((BigInteger)object[0]);
			mapExtraFields(abstractRolePermissionJoinVo, object);
			resultList.add(abstractRolePermissionJoinVo);
		}
		return resultList;
	}
	
	public abstract T instantiateVo();
	
	public void mapExtraFields(T abstractRolePermissionJoinVo,Object[] object ){};
}
