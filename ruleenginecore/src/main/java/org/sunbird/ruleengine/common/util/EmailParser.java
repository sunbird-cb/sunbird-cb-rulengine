package org.sunbird.ruleengine.common.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.service.GenericCron;

import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class EmailParser {
	private static final Logger logger = LogManager.getLogger(EmailParser.class);
	Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

	public String parse(String clientCode, String templateName, String emailTempate, Object object) {

		try {
			StringTemplateLoader stringLoader = new StringTemplateLoader();
			stringLoader.putTemplate(clientCode + "_" + templateName, emailTempate);
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
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "12345678");
		// List parsing
		List<String> countries = new ArrayList<String>();
		countries.add("India");
		countries.add("United States");
		countries.add("Germany");
		countries.add("France");
		data.put("countries", countries);
		EmailParser emailParser = new EmailParser();
		ObjectMapper objectMapper= new ObjectMapper();
		
		
		
		try {
			Object map5=  	objectMapper.readValue("{  \r\n" + 
					"  \"endDate\":1530272010000,\r\n" + 
					"  \"integrationInstanceFailureList\":[  \r\n" + 
					"    [  \r\n" + 
					"      15558,\r\n" + 
					"      null,\r\n" + 
					"      \"java.lang.IllegalArgumentException: 'uriTemplate' must not be null\\r\\n\\tat org.springframework.util.Assert.hasText(Assert.java:162)\\r\\n\\tat org.springframework.web.util.UriTemplate$Parser.<init>(UriTemplate.java:179)\\r\\n\\tat org.springframework.web.util.UriTemplate$Parser.<init>(UriTemplate.java:172)\\r\\n\\tat org.springframework.web.util.UriTemplate.<init>(UriTemplate.java:65)\\r\\n\\tat org.springframework.web.client.RestTemplate.execute(RestTemplate.java:528)\\r\\n\\tat org.springframework.web.client.RestTemplate.exchange(RestTemplate.java:447)\\r\\n\\tat org.sunbird.ruleengine.helper.RestStepProcessor.processStep(RestStepProcessor.java:263)\\r\\n\\tat org.sunbird.ruleengine.helper.StepHelper.processStep(StepHelper.java:23)\\r\\n\\tat org.sunbird.ruleengine.service.GenericCron.processStep(GenericCron.java:206)\\r\\n\\tat org.sunbird.ruleengine.service.GenericCron.processJobDetails(GenericCron.java:135)\\r\\n\\tat org.sunbird.ruleengine.service.GenericCron.startCron(GenericCron.java:68)\\r\\n\\tat org.sunbird.ruleengine.service.GenericCron$$FastClassBySpringCGLIB$$edb293f8.invoke(<generated>)\\r\\n\\tat org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)\\r\\n\\tat org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:649)\\r\\n\\tat org.sunbird.ruleengine.service.GenericCron$$EnhancerBySpringCGLIB$$c4a20560.startCron(<generated>)\\r\\n\\tat sun.reflect.GeneratedMethodAccessor49.invoke(Unknown Source)\\r\\n\\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\\r\\n\\tat java.lang.reflect.Method.invoke(Method.java:498)\\r\\n\\tat org.springframework.scheduling.support.ScheduledMethodRunnable.run(ScheduledMethodRunnable.java:65)\\r\\n\\tat org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)\\r\\n\\tat java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)\\r\\n\\tat java.util.concurrent.FutureTask.runAndReset(FutureTask.java:308)\\r\\n\\tat java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$301(ScheduledThreadPoolExecutor.java:180)\\r\\n\\tat java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:294)\\r\\n\\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\\r\\n\\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\\r\\n\\tat java.lang.Thread.run(Thread.java:748)\\r\\n\",\r\n" + 
					"      69,\r\n" + 
					"      null,\r\n" + 
					"      1530268470000,\r\n" + 
					"      1532000525000,\r\n" + 
					"      null,\r\n" + 
					"      3,\r\n" + 
					"      \"null\",\r\n" + 
					"      0,\r\n" + 
					"      null,\r\n" + 
					"      false,\r\n" + 
					"      34,\r\n" + 
					"      \"Bizom EAL Agro Product Master\"\r\n" + 
					"    ]\r\n" + 
					"  ],\r\n" + 
					"  \"startDate\":1530268409000\r\n" + 
					"}",LinkedHashMap.class);
			
			System.out.println(emailParser.parse("one", "abc", "<html>\r\n" + 
					"\r\n" + 
					"<head>\r\n" + 
					"    <style>\r\n" + 
					"        #customers {\r\n" + 
					"            font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\r\n" + 
					"            border-collapse: collapse;\r\n" + 
					"            width: 100%;\r\n" + 
					"        }\r\n" + 
					"        \r\n" + 
					"        #customers td,\r\n" + 
					"        #customers th {\r\n" + 
					"            border: 1px solid #ddd;\r\n" + 
					"            padding: 8px;\r\n" + 
					"        }\r\n" + 
					"        \r\n" + 
					"        #customers tr:nth-child(even) {\r\n" + 
					"            background-color: #f2f2f2;\r\n" + 
					"        }\r\n" + 
					"        \r\n" + 
					"        #customers tr:hover {\r\n" + 
					"            background-color: #ddd;\r\n" + 
					"        }\r\n" + 
					"        \r\n" + 
					"        #customers th {\r\n" + 
					"            padding-top: 12px;\r\n" + 
					"            padding-bottom: 12px;\r\n" + 
					"            text-align: left;\r\n" + 
					"            background-color: #4CAF50;\r\n" + 
					"            color: white;\r\n" + 
					"        }\r\n" + 
					"    </style>\r\n" + 
					"</head>\r\n" + 
					"\r\n" + 
					"<body> Hello There, These are the failed jobs from ${startDate} to ${endDate}.\r\n" + 
					"    <div style=\"padding-top:20px;\">\r\n" + 
					"        <table id='customers'>\r\n" + 
					"		\r\n" + 
					"            <tr>\r\n" + 
					"				<th> Job Name </th>\r\n" + 
					"                <th> Step Id </th>\r\n" + 
					"                <th> Reason </th>\r\n" + 
					"				<th> Date & Time </th>\r\n" + 
					"				\r\n" + 
					"            </tr>\r\n" + 
					"            <#list 0..integrationInstanceFailureList?size-1 as i>\r\n" + 
					"                <tr>\r\n" + 
					"					<td> ${integrationInstanceFailureList[i][14]} </td>\r\n" + 
					"                    <td> ${integrationInstanceFailureList[i][3]} </td>\r\n" + 
					"                    <td> ${integrationInstanceFailureList[i][2]} </td>\r\n" + 
					"					<td> new Date(${integrationInstanceFailureList[i][5]}) </td>\r\n" + 
					"                </tr>\r\n" + 
					"            </#list>\r\n" + 
					"			\r\n" + 
					"        </table>\r\n" + 
					"    </div>\r\n" + 
					"</body>\r\n" + 
					"\r\n" + 
					"</html>", map5));
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
		
	}

}
