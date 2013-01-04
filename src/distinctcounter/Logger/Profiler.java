/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Logger;

import distinctcounter.Db.Db;
import distinctcounter.Tools.CommonDb;
import distinctcounter.Tools.InstanceDetails;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stefan
 */
public class Profiler {
	
	private static Map<String,Long> REPORTS = Collections.synchronizedMap(new HashMap<String, Long>());
	private static Map<String,Float> REPORTS_RES = Collections.synchronizedMap(new HashMap<String, Float>());
	private static Map<String,Integer> REPORTS_CALLS = Collections.synchronizedMap(new HashMap<String, Integer>());
	private static Map<String,Boolean> REPORTS_PERMANENT = Collections.synchronizedMap(new HashMap<String, Boolean>());
	private static String TABLE_NAME = "j_distinctcounter__profilers";
	
	private final static String DB_NAME = CommonDb.getCurrentDb();
	
	private synchronized static void setPermanent(String report, boolean isPermanent){
		REPORTS_PERMANENT.put(report, isPermanent);
	}
	
	public synchronized static void start(String report){
		REPORTS.put(report, System.currentTimeMillis());
		if(!REPORTS_CALLS.containsKey(report)){
			REPORTS_CALLS.put(report, 0);
		}
	}
	
	public synchronized static void start(String report, boolean isPermanent){
		Profiler.setPermanent(report, isPermanent);
		Profiler.start(report);
	}
	
	public synchronized static void stop(String report){		
		Long currentTime = System.currentTimeMillis();
		Long startTime = REPORTS.containsKey(report) ? REPORTS.get(report) : currentTime;
		Float prevDiff = REPORTS_RES.containsKey(report) ? REPORTS_RES.get(report) : 0f;
		Float diff = (float)(currentTime-startTime)/1000 + prevDiff;
		REPORTS_RES.put(report, diff);
		int prevCalls = (REPORTS_CALLS.containsKey(report)) ? REPORTS_CALLS.get(report) : 0;
		REPORTS_CALLS.put(report, prevCalls +1);
	}
	
	public synchronized static void stop(String report, boolean isPermanent){
		Profiler.setPermanent(report, isPermanent);
		Profiler.stop(report);
	}
	
	public synchronized static void logAllReports(boolean withPermanent){
		try {

			for(String report:REPORTS_RES.keySet()){
				if(REPORTS_PERMANENT.containsKey(report) != withPermanent){
					continue;
				}
				
				String insertSql = ""
					+ "INSERT DELAYED INTO "+DB_NAME+"."+TABLE_NAME+""
					+ "(instance_id,name,exec_time,calls)"
					+ "VALUES"
					+ "("+InstanceDetails.getInstanceId()+",'"+report+"',"+REPORTS_RES.get(report) +","+REPORTS_CALLS.get(report) +")";
				Db.getCurrentConnection().exec(insertSql);
				System.out.println("PROFILER: ["+report+"]:"+REPORTS_RES.get(report)+" | CALLS:"+REPORTS_CALLS.get(report));
			}			
			Profiler.clearKeysFromRes(false);
			Profiler.clearKeysFromCalls(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	private synchronized static void clearKeysFromRes(boolean withPermanent){
		HashMap<String,Float> temp = new HashMap<String, Float>();
		temp.putAll(REPORTS_RES);
		
		if(withPermanent){
			temp.clear();			
		}else{
			for(String report:temp.keySet()){
				if(!REPORTS_PERMANENT.containsKey(report)){
					REPORTS_RES.remove(report);
				}
			}
		}
	}
	
	private synchronized static void clearKeysFromCalls(boolean withPermanent){
		HashMap<String,Integer> temp = new HashMap<String, Integer>();
		temp.putAll(REPORTS_CALLS);
		
		if(withPermanent){
			temp.clear();			
		}else{
			for(String report:temp.keySet()){
				if(!REPORTS_PERMANENT.containsKey(report)){
					REPORTS_CALLS.remove(report);
				}
			}
		}
	}
	
}
