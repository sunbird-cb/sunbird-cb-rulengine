package org.sunbird.ruleengine.model;

import java.math.BigInteger;

public class ErrorCodeCount {
	public String errorCode;
	public BigInteger count;
	
public ErrorCodeCount(String errorCode, BigInteger count) {
		
		this.errorCode = errorCode;
		this.count = count;
		
	}

public String getErrorCode() {
	return errorCode;
}
public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
}
public BigInteger getCount() {
	return count;
}
public void setCount(BigInteger count) {
	this.count = count;
}


}
