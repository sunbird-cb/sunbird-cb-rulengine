package org.sunbird.ruleengine.mapper;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.model.AbstractRole;

public abstract class AbstractRoleMapper<T extends AbstractRole> implements AbstractObjectToEntityMapper<List<T>>{

	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> map(Object returnObject) {
	    List<T> resultList= new ArrayList<>();
		for(Object[] object:((List<Object[]>)returnObject))
		{
			T abstractRoleVo= instantiateVo();
			abstractRoleVo.setId(((BigDecimal)object[0]).toBigInteger());
			abstractRoleVo.setRoleName(((String)object[1]));
			mapExtraFields(abstractRoleVo, object);
			resultList.add(abstractRoleVo);
		}
		return resultList;
	}
	
	public abstract T instantiateVo();
	
	public void mapExtraFields(T abstractRoleVo,Object[] object ){};
	
	
}
