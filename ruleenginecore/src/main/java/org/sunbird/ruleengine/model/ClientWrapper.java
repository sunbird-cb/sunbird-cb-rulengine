package org.sunbird.ruleengine.model;

import java.util.List;

public class ClientWrapper {
	

	    List<Client> client;
	    
	    
        public String startDate;
	    
		public ClientWrapper() {
			
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public List<Client> getClient() {
			return client;
		}

		public void setClient(List<Client> client) {
			this.client = client;
		}
	    

	    //getters setters
	
}
