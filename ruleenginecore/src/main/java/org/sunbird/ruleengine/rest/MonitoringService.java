package org.sunbird.ruleengine.rest;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.util.CommonMessages;
import org.sunbird.ruleengine.job.OIPMonitoring;
import org.sunbird.ruleengine.model.Client;
import org.sunbird.ruleengine.model.ClientWrapper;
import org.sunbird.ruleengine.model.MonitoringReport;
import org.sunbird.ruleengine.service.ClientService;

@RestController
@RequestMapping("/monitoring")
public class MonitoringService{

	@Autowired
	OIPMonitoring oipMonitoring;
	
	@Autowired
	ClientService clientService;
	
	public List<Client> getAllClients(){
		Client cli = new Client();
		List<Client> clientList = clientService.getListByCriteria(cli, -1, 0);
		return clientList;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<MonitoringReport> getReport(@RequestBody ClientWrapper clientWrapper,Principal principal) {
		System.out.println("Client List "+clientWrapper.getClient().size());
		String startDate = null;
		List<MonitoringReport> mr = new ArrayList<MonitoringReport>();
		if(clientWrapper.getStartDate() != null) {
			boolean valid = isThisDateValid(clientWrapper.getStartDate(), "yyyy/MM/dd");
			if (!valid) {
				return new Response<MonitoringReport>(false, null, CommonMessages.CHECK_DATE_FORMAT);
			}
		}
		
		if(clientWrapper.getStartDate() != null) {
			startDate = clientWrapper.getStartDate();
		}
		
		if(clientWrapper.getClient().size() == 0) {
			List<Client> clients = getAllClients();
			mr = oipMonitoring.getMonitoringReport(clients,startDate);
			return new Response(true,mr);
		} else {
			mr = oipMonitoring.getMonitoringReport(clientWrapper.getClient(),startDate);
			return new Response(true,mr);
		}
		
	}

	public boolean isThisDateValid(String dateToValidate, String dateFromat){

		if(dateToValidate == null){
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {

			//if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);

		} catch (ParseException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	
	
	
	
	
	
	
	
	
	
	

}
