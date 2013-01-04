/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Tools;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author stefan
 */
public class MetricKey {
	
	public static String METRIC_DAU = "dau";	
	public static String METRIC_WAU = "wau";
	public static String METRIC_MAU = "mau";
	public static String METRIC_LAU = "lau";
	public static String METRIC_RDAU = "rdau";
	
	private String metricName;
	private HashMap<String, Object> splitFields;
	private Date currentDate;
	
	public synchronized String getMetricName(){
		return this.metricName;
	}
	
	public synchronized HashMap<String, Object> getSplitFields(){
		return this.splitFields;
	}
	
	public synchronized Date getCurrentDate(){
		return this.currentDate;
	}
	
	@Override
	public synchronized String toString(){
		String s = ""
                + "metric_name:"+this.metricName+"|"
                + "split_fields:["+this.splitFields.keySet().toString()+":"+this.splitFields.values().toString()+"]|"
                + "current_day:"+DateFormat.format(this.currentDate, "yyyyMMdd");
		return s;
	}
	
    public synchronized static String toStringSplitFields(HashMap<String, Object> splitFields){
		String s = splitFields.keySet().toString()+":"+splitFields.values().toString();
		return s;
	}
    
	public synchronized String toStringSplitFieldsValues(){
		String values = splitFields.values().toString();
		values = values.substring(1);
		values = values.substring(0, values.length()-1);
		
		return values;
	}
	
    public synchronized String toStringSplitFieldsToFile(){
        String keys = splitFields.keySet().toString();
        keys = keys.replace(",", "#");
        String values = splitFields.values().toString();
        values = values.replace(",", "#");
		
        return keys+"_"+values;
	}
    
	public synchronized String toStringShort(){
		String s = "metric_name:"+this.metricName+"|current_day:"+DateFormat.format(this.currentDate, "yyyyMMdd");
		return s;
	}
	
	public synchronized static MetricKey generateKey(String metricName, HashMap<String, Object> splitFields, Date currentDate){
		MetricKey mk = new MetricKey();
		mk.metricName = metricName;
        mk.splitFields = splitFields;
		mk.currentDate = currentDate;
		
		return mk;
	}
	
}
