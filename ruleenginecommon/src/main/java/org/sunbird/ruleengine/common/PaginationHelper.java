package org.sunbird.ruleengine.common;

import java.util.List;



public class PaginationHelper 
{
	long iTotalRecords;
List<?> aaData;

public static PaginantorReturn convertForPagination(long iTotalRecords,
		List<?> aaData) {
	return new PaginantorReturn(iTotalRecords, aaData);
}

public static class PaginantorReturn {
	long iTotalRecords;
	List<?> aaData;
	long iTotalDisplayRecords;
	String sEcho;

	public PaginantorReturn(long iTotalRecords, List<?> aaData) {
		super();
		this.iTotalRecords = iTotalRecords;
		this.aaData = aaData;
		this.iTotalDisplayRecords = iTotalRecords;
	}

	public long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public List<?> getAaData() {
		return aaData;
	}

	public void setAaData(List<?> aaData) {
		this.aaData = aaData;
	}

	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

}

}

