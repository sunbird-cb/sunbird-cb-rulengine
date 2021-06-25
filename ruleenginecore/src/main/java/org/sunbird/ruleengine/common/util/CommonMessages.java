package org.sunbird.ruleengine.common.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.sunbird.ruleengine.common.Message;

@JsonSerialize(using=CommonMessageSerializer.class)
public enum CommonMessages implements Message{
	KEY_CODE_VALIDATION_ERROR("Combination of key and code already present please enter another key", Severity.ERROR), 
	RESOLUTION_VALIDATION("For one code there can be only one Reciprocal", Severity.ERROR),
	CLIENT_CODE_DOESNOT_EXISTS("Client doesn't exits", Severity.ERROR),
	EMPTY_JOB_CODE("Job Code Can't be empty", Severity.ERROR),
	JOB_DOESNOT_EXISTS("Job doesn't exits", Severity.ERROR),
	SOME_ERROR_OCCURED("Some error occured, please login Oresund platform and check step history.", Severity.ERROR),
	CHECK_DATE_FORMAT("Date Format Wrong, please pass the date in yyyy/MM/dd format.", Severity.ERROR),
	;

	String code;
    String message;
    Severity severity;
    String[] parameters;
   
     ResourceBundle resourceBundle;
	
	@Override
	public String getCode() {
		  return getModuleCode()+code;
	}

	@Override
	public String getMessage() {
		 if(getParameters()!=null &&getParameters().length==0)
         {
          return MessageFormat.format(message, (Object[]) getParameters());
         }
         else{
          return message;
         }
	}
	
	   private CommonMessages(String message, Severity severity, String... parameters)
       {
         code=this.toString();
         this.message=MessageFormat.format(message, (Object[]) getParameters());
         this.severity=severity;
         this.parameters=parameters;
       }
      
        private CommonMessages(ResourceBundle resourceBundle, String message, Severity severity, String... parameters)
       {
         code=this.toString();
         this.message=message;
         this.severity=severity;
         this.parameters=parameters;
         this.resourceBundle=resourceBundle;
       }
	public String getLocalizedMessage() {
        return resourceBundle.containsKey(getCode())?resourceBundle.getString(getCode()):getMessage();
      }
	
	@Override
	public String getMessage(ResourceBundle resourceBundle) {
		  return resourceBundle.containsKey(getCode())?resourceBundle.getString(getCode()):getMessage();
	}

	@Override
	public String[] getParameters() {
		 return parameters;
	}

	@Override
	public Severity getSeverity() {
		 return severity;
	}

	@Override
	public String getModuleCode() {
        return "ORESUND-0001-";
	}

	

}
