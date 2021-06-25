package org.sunbird.ruleengine.helper;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class TemplateParser {
	private static final Logger logger = LogManager.getLogger(TemplateParser.class);
	Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
	StringTemplateLoader stringLoader = new StringTemplateLoader();
	public String parse(String clientCode, String templateName, String fillerObject, Object object) {
		try {
			stringLoader.putTemplate(clientCode + "_" + templateName, fillerObject);
			cfg.setTemplateLoader(stringLoader);
			Template template = cfg.getTemplate(clientCode + "_" + templateName);
			StringWriter stringWriter = new StringWriter();
			template.process(object, stringWriter);
			stringWriter.flush();
			return stringWriter.toString();

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		} catch (TemplateException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}

		return null;
	}

	public static void main(String[] args) {
		
		
		
		/*
		 * ObjectHolder objectHolder= new ObjectHolder();
		 * objectHolder.getConfigs().put("0", new Config());
		 * 
		 * String[] array= {"1233444","442","3","10/17/2017","101","2"};
		 * String[] array2= {"1233444","442","3","10/17/2017","101","2"};
		 * List<String[]> list= new ArrayList<>(); list.add(array);
		 * list.add(array2); Map<String,Object> token= new LinkedHashMap<>();
		 * token.put("Token", true);
		 * objectHolder.getConfigs().get("0").getValues().put("1", token);
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * String template=
		 * "{<#setting date_format=\"yyyy-MM-dd\">\"data[url]\":\"/payments/addMultiplePrimarySales\""
		 * + "<#list 0..configs['0'].values['1']?size-1 as i>" +
		 * ",\"data[postparams][Sales][${i}][Payment][warehouse_id]\":${configs['0'].values['1'][i][2]}"
		 * + ",\"data[postparams][Sales][${i}][Payment][mode]\":\"credit\"" +
		 * ",\"data[postparams][Sales][${i}][Payment][fordate]\":\"${configs['0'].values['1'][i][3]?datetime(\"MM/dd/yyyy\")?date}\""
		 * +
		 * ",\"data[postparams][Sales][${i}][Payment][billno]\":${configs['0'].values['1'][i][1]}"
		 * +
		 * ",\"data[postparams][Sales][${i}][Payment][invoiceid]\":${configs['0'].values['1'][i][0]}"
		 * +
		 * ",\"data[postparams][Sales][${i}][Paymentdetail][0][skunit_id]\":${configs['0'].values['1'][i][4]}"
		 * +
		 * ",\"data[postparams][Sales][${i}][Paymentdetail][0][quantity]\":${configs['0'].values['1'][i][5]}</#list>}"
		 * ; TemplateParser emailParser = new TemplateParser();
		 * System.out.println(emailParser.parse("one", "abc",
		 * "Y${Token?string}Y", token));
		 */
		
		List<IntegrationInstanceFailure> list = new ArrayList<>();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("integrationInstanceFailureList", list);
		IntegrationInstanceFailure instanceFailure = new IntegrationInstanceFailure();
		instanceFailure.setStepId(BigInteger.ONE);
		instanceFailure.setErrorResponse("My Response");
		list.add(instanceFailure);
		
		ObjectMapper objectMapper= new ObjectMapper();
		
		try {
			TemplateParser templateParser= new TemplateParser();
				System.out
						.println(templateParser
						.parse("one",
								"two",
								"Hello There,\r\n       These are the failed jobs for yesterday.\r\n <table>\r\n <th>\r\n <td>\r\n Step Id\r\n </td>\r\n <td>\r\n Reason\r\n </td>\r\n </th>\r\n<#list 0..integrationInstanceFailureList?size-1 as i>\r\n<tr>\r\n <td>\r\n ${integrationInstanceFailureList[i].stepId}\r\n </td>\r\n <td>\r\n ${integrationInstanceFailureList[i].errorResponse}\r\n </td>\r\n </tr>\r\n</#list>\r\n</table>",
										root));
		
				Object map5=  	objectMapper.readValue("{\"jobRunningId\":1408,\"runAgain\":false,\"startSequence\":null,\"endSequence\":null,\"batchSize\":null,\"currentDate\":1548845134342,\"lastScheduledStartTime\":1548843121000,\"nextRunTime\":1548846762000,\"jobRunSuccessCount\":0,\"jobRunFailureCount\":0,\"configs\":{\"0\":{\"parent\":null,\"currentResponse\":null,\"translatorMap\":{},\"values\":{\"1\":\"File Moved Successfully\"}}},\"dryRun\":false}\r\n" + 
						"",LinkedHashMap.class);
	
		
		Object o =templateParser.parse("1", "aggd11","<#setting date_format=\"dd_MM_yyyy\">NPSTKRCPHDR._MJN_${nextRunTime?number_to_date}_FILES_MOVER",map5);
		
		
		System.out.println(o);
		
	
		
	
		

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
		

	}
	

}
