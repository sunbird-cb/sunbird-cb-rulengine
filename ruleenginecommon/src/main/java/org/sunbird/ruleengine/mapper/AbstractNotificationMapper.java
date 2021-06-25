package org.sunbird.ruleengine.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.model.AbstractNotification;

public abstract class AbstractNotificationMapper <T extends AbstractNotification> implements AbstractObjectToEntityMapper<List<T>>{

	@SuppressWarnings("unchecked")
	@Override
	public List<T> map(Object returnObject) {
	    List<T> resultList= new ArrayList<>();
		for(Object[] object:((List<Object[]>)returnObject))
		{
			T abstractNotificationVo= instantiateVo();
			if(object[0] instanceof BigDecimal)
			{
				abstractNotificationVo.setId(((BigDecimal)object[0]).toBigInteger());
			}
			else{
				abstractNotificationVo.setId((BigInteger)object[0]);
			}
			abstractNotificationVo.setNotifyTo(((BigDecimal) object[2]).toBigInteger());
			abstractNotificationVo.setNotificationFrom(((BigDecimal) object[3]).toBigInteger());
			abstractNotificationVo.setNotificationReferenceId(((BigDecimal) object[4]).toBigInteger());
			abstractNotificationVo.setFirstName((String) object[5]);
			abstractNotificationVo.setLastName((String) object[6]);
			abstractNotificationVo.setNotificationTime(((Date) object[7]));
			abstractNotificationVo.setNotificationType((String) object[8]);
			abstractNotificationVo.setNotificationText((String) object[9]);
			if(object[10].equals('1'))
			{
				abstractNotificationVo.setRead(true);
			}
		
			else
			{
				abstractNotificationVo.setRead(false);
			}
			mapExtraFields(abstractNotificationVo, object);
			resultList.add(abstractNotificationVo);
		}
		return resultList;
	}
	
	public abstract T instantiateVo();
	
	public void mapExtraFields(T abstractUserVo,Object[] object ){};
}
