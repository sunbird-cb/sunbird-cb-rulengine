package org.sunbird.ruleengine.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.model.AbstractUser;

public abstract class AbstractUserMapper<T extends AbstractUser> implements AbstractObjectToEntityMapper<List<T>>{

	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> map(Object returnObject) {
	    List<T> resultList= new ArrayList<>();
		for(Object[] object:((List<Object[]>)returnObject))
		{
			T abstractUserVo= instantiateVo();
			if(object[0] instanceof BigDecimal)
			{
				abstractUserVo.setId(((BigDecimal)object[0]).toBigInteger());
			}
			else{
				abstractUserVo.setId((BigInteger)object[0]);
			}

			mapExtraFields(abstractUserVo, object);
			resultList.add(abstractUserVo);
		}
		return resultList;
	}
	
	public abstract T instantiateVo();
	
	public void mapExtraFields(T abstractUserVo,Object[] object ){};
	
	
}
