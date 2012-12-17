/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author stefan
 */
public class DateFormat {
	
	public static String format(Date currentDate, String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);		
		return df.format(currentDate);
	}
	
	public static Date parse(String currentDate, String pattern) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat(pattern);		
		return df.parse(currentDate);
	}
	
	public static Date addDays(Date currentDate, int amount){
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		
		return cal.getTime();
	}
	
}
