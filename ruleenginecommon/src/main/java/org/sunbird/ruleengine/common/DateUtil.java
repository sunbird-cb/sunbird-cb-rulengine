package org.sunbird.ruleengine.common;



import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {
	
	public static int same(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);

		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
			return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
		if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
			return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);

	}
	
	public static Date fromDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date toDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	public static Date newDate(String timeZone)
	{
		Calendar calendar= GregorianCalendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone.toString()));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date newDateTime(TimeZoneEnum timeZone)
	{
		Calendar calendar= GregorianCalendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone.toString()));
		return calendar.getTime();
	}
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	public static Date getCurrentDateInTimeZone(TimeZoneEnum timeZone) {
		  Date currentDate = new Date();
		  Calendar mbCal = new GregorianCalendar(TimeZone.getTimeZone(timeZone.getTimeZone()));
		        mbCal.setTimeInMillis(currentDate.getTime());
		        Calendar cal = Calendar.getInstance();
		        cal.set(Calendar.YEAR, mbCal.get(Calendar.YEAR));
		        cal.set(Calendar.MONTH, mbCal.get(Calendar.MONTH));
		        cal.set(Calendar.DAY_OF_MONTH, mbCal.get(Calendar.DAY_OF_MONTH));
		        cal.set(Calendar.HOUR_OF_DAY, mbCal.get(Calendar.HOUR_OF_DAY));
		        cal.set(Calendar.MINUTE, mbCal.get(Calendar.MINUTE));
		        cal.set(Calendar.SECOND, mbCal.get(Calendar.SECOND));
		        cal.set(Calendar.MILLISECOND, mbCal.get(Calendar.MILLISECOND));
		        return cal.getTime();
		    }
		 
		 public enum TimeZoneEnum {
		  GMTPLUS0400("GMT+04:00"), GMT("GMT"), UTC("UTC");
		  private String timeZone;
		  TimeZoneEnum(String str) {
		   timeZone=str;
		  }
		  
		  public String getTimeZone(){
		   return timeZone;
		  }

		 }
		 
		
	

}
