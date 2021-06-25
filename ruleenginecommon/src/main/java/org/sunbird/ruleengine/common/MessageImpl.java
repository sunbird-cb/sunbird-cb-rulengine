package org.sunbird.ruleengine.common;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageImpl  implements Message {

	
	String[] parameters;
	String code;
	String message;
	Severity severity;
	String moduleCode;
	
	public MessageImpl(Message message, String... parameters )
	{
		this.code=message.getCode();
		this.message=message.getMessage();
		this.parameters=parameters;
		this.severity=message.getSeverity();
		this.moduleCode=message.getModuleCode();
	}
	
	public MessageImpl(Message message, String customMessage)
	{
		this.code=message.getCode();
		this.message=customMessage;
	}

	@Override
	public String getCode() {
		return code;
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
		return getModuleCode();
	}
	
	@Override
	public String getMessage(ResourceBundle resourceBundle) {
		String message= resourceBundle.containsKey(getCode())?resourceBundle.getString(getCode()):getMessage();
		if(getParameters()!=null &&getParameters().length==0)
		{
			return MessageFormat.format(message, (Object[]) getParameters());
		}
		else{
			return message;
		}
	}
	
}
