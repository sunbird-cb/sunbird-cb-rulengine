package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.IntegrationInstanceFailureDao;
import org.sunbird.ruleengine.model.ErrorPatternMaster;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class IntegrationInstanceFailureServiceImpl
		extends GenericServiceImpl<IntegrationInstanceFailure, IntegrationInstanceFailure>
		implements IntegrationInstanceFailureService {

	@Autowired
	IntegrationInstanceFailureDao instanceFailureDao;
	
	@Autowired
	ErrorPatternMasterService errorPatternMasterService;

	@Override
	public AbstractDAO<IntegrationInstanceFailure, IntegrationInstanceFailure> getDAO() {
		return instanceFailureDao;
	}

	@Override
	public List<IntegrationInstanceFailure> getIntegrationInstanceFailureWithJobDetail(IntegrationInstanceFailure criteria,Date startDate, Date endDate,String jobDetailIds,int firstResult, int maxResult,List<Object[]> notInErrorpatternIds) {
		String query="";
		if(notInErrorpatternIds.isEmpty())
		 query = "SELECT igFail.ID,igFail.CONFIG_OBJECT_JSON,igFail.ERROR_RESPONSE,igFail.STEP_ID,igFail.CREATED_BY,igFail.CREATION_DATE,igFail.LAST_UPDATE_DATE,igFail.LAST_UPDATED_BY,igFail.CLIENT_ID,igFail.NEXT_REQUEST_JSON,igFail.NO_OF_RETRIES,igFail.FAILURE_TEMPLATE_RESPONSE,igFail.DONE,igFail.JOB_DETAIL_ID, jobRunDetail.JOB_NAME ,jobRunDetail.FILE_NAME FROM INTEGRATION_INSTANCE_FAILURE igFail INNER JOIN JOB_RUN_DETAIL jobRunDetail ON (jobRunDetail.id = igFail.JOB_RUN_ID AND igFail.CREATION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"')  where igFail.DONE = FALSE AND igFail.JOB_DETAIL_ID IN ("+jobDetailIds+")  ";
		else
			query = "SELECT igFail.ID,igFail.CONFIG_OBJECT_JSON,igFail.ERROR_RESPONSE,igFail.STEP_ID,igFail.CREATED_BY,igFail.CREATION_DATE,igFail.LAST_UPDATE_DATE,igFail.LAST_UPDATED_BY,igFail.CLIENT_ID,igFail.NEXT_REQUEST_JSON,igFail.NO_OF_RETRIES,igFail.FAILURE_TEMPLATE_RESPONSE,igFail.DONE,igFail.JOB_DETAIL_ID, jobRunDetail.JOB_NAME ,jobRunDetail.FILE_NAME FROM INTEGRATION_INSTANCE_FAILURE igFail INNER JOIN JOB_RUN_DETAIL jobRunDetail ON (jobRunDetail.id = igFail.JOB_RUN_ID AND igFail.CREATION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"')  where igFail.DONE = FALSE AND igFail.JOB_DETAIL_ID IN ("+jobDetailIds+")  AND (igFail.ERROR_PATTERN_ID IS NULL OR igFail.ERROR_PATTERN_ID NOT IN "+ notInErrorpatternIds.toString().replace("[", "(").replace("]", ")")+") ";
		
		@SuppressWarnings("unchecked")
		List<IntegrationInstanceFailure> list=	 (List<IntegrationInstanceFailure>) instanceFailureDao.getListByCriteria(criteria,query , "order by igFail.JOB_DETAIL_ID", firstResult, maxResult);
			return list;
	}

	@Override
	public ErrorPatternMaster getFailureErrorPattern(BigInteger clientId,String errorResponse) {
		ErrorPatternMaster errorPatternMaster = new ErrorPatternMaster();
		errorPatternMaster.setClientId(clientId);
		List<ErrorPatternMaster> patternList=errorPatternMasterService.getListByCriteria(errorPatternMaster, -1, 0);
		return getErrorPatternId(errorResponse,patternList);
	}
	
	
	public ErrorPatternMaster getErrorPatternId(String errorResponse,List<ErrorPatternMaster> patternList)
	{
		for (ErrorPatternMaster errorPatternMaster2 : patternList) {
			Pattern p = Pattern.compile(errorPatternMaster2.getPattern());
			Matcher m = p.matcher(errorResponse);   // get a matcher object
				if(m.matches())
				    	 return errorPatternMaster2;
		}
			return null;
	}

	public static void main(String[] args) {
		IntegrationInstanceFailureServiceImpl i = new IntegrationInstanceFailureServiceImpl();
		List<ErrorPatternMaster> l = new ArrayList<ErrorPatternMaster>();
		l.add(new ErrorPatternMaster());
		i.getErrorPatternId("abc", l);
	}
	
}