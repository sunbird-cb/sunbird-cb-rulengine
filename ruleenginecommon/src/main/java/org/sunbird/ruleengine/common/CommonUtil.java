package org.sunbird.ruleengine.common;

public class CommonUtil {
	
	public static boolean isBlank(String str)
	{
		if(str==null || str.isEmpty())
			return true;
		else 
			return false;
	}
	

	public static boolean isNotBlank(String str)
	{
		if(str==null || str.isEmpty())
			return false;
		else 
			return true;
	}
	
	public static boolean isTrue(Boolean str)
	{
		if(str==null)
			return false;
		else 
			return str.booleanValue();
	}
	
	public static boolean isNotBlankInteger(int str)
	{
	
		String test = "";
		test = String.valueOf(str);
		
		if(test==null)
			return false;
		else 
			return true;
	}
	

}
