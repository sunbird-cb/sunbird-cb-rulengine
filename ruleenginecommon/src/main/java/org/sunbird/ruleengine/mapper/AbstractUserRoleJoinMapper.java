package org.sunbird.ruleengine.mapper;



import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.model.AbstractUserRoleJoin;

public abstract class AbstractUserRoleJoinMapper<T extends AbstractUserRoleJoin> implements AbstractObjectToEntityMapper<List<T>>{

	@SuppressWarnings("unchecked")
	@Override
	public List<T> map(Object returnObject) {
	    List<T> resultList= new ArrayList<>();
		for(Object[] object:((List<Object[]>)returnObject))
		{
			T abstractUserRoleJoinVo= instantiateVo();
			abstractUserRoleJoinVo.setId((BigInteger)object[0]);
			mapExtraFields(abstractUserRoleJoinVo, object);
			resultList.add(abstractUserRoleJoinVo);
		}
		return resultList;
	}
	
	public abstract T instantiateVo();
	
	public void mapExtraFields(T abstractUserRoleJoinVo,Object[] object ){};
	
}
