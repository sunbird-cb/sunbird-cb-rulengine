package org.sunbird.ruleengine.mapper;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.model.AbstractPermission;

public abstract class AbstractPermissionMapper<T extends AbstractPermission> implements AbstractObjectToEntityMapper<List<T>>{

	@SuppressWarnings("unchecked")
	@Override
	public List<T> map(Object returnObject) {
	    List<T> resultList= new ArrayList<>();
		for(Object[] object:((List<Object[]>)returnObject))
		{
			T abstractPermissionVo= instantiateVo();
			abstractPermissionVo.setId(((BigDecimal)object[0]).toBigInteger());
			abstractPermissionVo.setPermissionName((String)object[1]);
			mapExtraFields(abstractPermissionVo, object);
			resultList.add(abstractPermissionVo);
		}
		return resultList;
	}
	
	public abstract T instantiateVo();
	
	public void mapExtraFields(T abstractPermissionVo,Object[] object ){};
	
	
}
