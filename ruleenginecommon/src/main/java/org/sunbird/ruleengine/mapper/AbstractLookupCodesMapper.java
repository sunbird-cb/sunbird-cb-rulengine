package org.sunbird.ruleengine.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.model.AbstractLookupCodes;

public abstract class AbstractLookupCodesMapper<T extends AbstractLookupCodes> implements AbstractObjectToEntityMapper<List<T>> {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> map(Object returnObject) {
	    List<T> resultList= new ArrayList<>();
		for(Object[] object:((List<Object[]>)returnObject))
		{
			T abstractLookupCodesVo= instantiateVo();
			if(object[0] instanceof BigDecimal)
			{
				abstractLookupCodesVo.setId(((BigDecimal)object[0]).toBigInteger());
			}
			else{
				abstractLookupCodesVo.setId((BigInteger)object[0]);
			}
			

			mapExtraFields(abstractLookupCodesVo, object);
			resultList.add(abstractLookupCodesVo);
		}
		return resultList;
	}
	
	public abstract T instantiateVo();
	
	public void mapExtraFields(T abstractLookupCodesVo,Object[] object ){};
	
	
}
