package org.sunbird.ruleengine.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.model.AbstractLookupType;

public abstract class AbstractLookupTypeMapper<T extends AbstractLookupType> implements AbstractObjectToEntityMapper<List<T>>  {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> map(Object returnObject) {
	    List<T> resultList= new ArrayList<>();
		for(Object[] object:((List<Object[]>)returnObject))
		{
			T abstractLookupTypeVo= instantiateVo();
			if(object[0] instanceof BigDecimal)
			{
				abstractLookupTypeVo.setId(((BigDecimal)object[0]).toBigInteger());
			}
			else{
				abstractLookupTypeVo.setId((BigInteger)object[0]);
			}
			

			mapExtraFields(abstractLookupTypeVo, object);
			resultList.add(abstractLookupTypeVo);
		}
		return resultList;
	}
	
	public abstract T instantiateVo();
	
	public void mapExtraFields(T abstractLookupTypeVo,Object[] object ){};
	
}
