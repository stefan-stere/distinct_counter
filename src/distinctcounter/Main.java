/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter;

import distinctcounter.Db.Db;
import distinctcounter.Extractor.ExtractorFactory;
import distinctcounter.Loader.FileMap;
import distinctcounter.Loader.LoaderFactory;
import distinctcounter.Logger.Message;
import distinctcounter.Logger.Profiler;
import distinctcounter.Parallel.Action;
import distinctcounter.Parallel.Core;
import distinctcounter.Tools.*;
import distinctcounter.configuration.Configuration;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author stefan
 */
public class Main {
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {	
            Configuration.load();

			int nbArg = args.length;
			String actionName = null;
			String extractorName = null;
			Date startDate = null;
			Date endDate = null;
			int gameReportId = 0;
			
			switch(nbArg){
				case 1 : 
					throw new InvalidParameterException("Missing loader, start_date and end_date parameters !!!  EX: action,loader,start_date,end_date[,game_report_id]");
				case 2 : 
					throw new InvalidParameterException("Missing start_date and end_date parameter !!!  EX: action,loader,start_date,end_date[,game_report_id]");
				case 3 : 
					throw new InvalidParameterException("Missing end_date parameter !!!  EX: action,loader,start_date,end_date[,game_report_id]");
				case 4 :
				case 5 :
					actionName = args[0];
					extractorName = args[1];
					startDate = DateFormat.parse(args[2], "yyyy-MM-dd");
					endDate = DateFormat.parse(args[3], "yyyy-MM-dd");
					break;
				default:
					throw new InvalidParameterException("The no of parameters passed is zero or more then 5 ["+nbArg+"]  EX: action,loader,start_date,end_date[,game_report_id]");
			}
			
			Main.run(actionName, extractorName, startDate, endDate);			
			Profiler.logAllReports(true);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			Message.log(sw.toString());
			System.exit(1); 
		}
	}
	
	private static void run(String actionName, String extractorName, Date startDate, Date endDate) throws Exception{		
		Profiler.start("TOTAL TIME (Action:"+actionName+") ["+DateFormat.format(startDate, "yyyy-MM-dd") +"] -> ["+DateFormat.format(endDate, "yyyy-MM-dd") +"]", true);			
		Message.log("START [Action:"+actionName+"] INTERVAL:["+DateFormat.format(startDate, "yyyy-MM-dd") +"] -> ["+DateFormat.format(endDate, "yyyy-MM-dd") +"]");
		
		List<HashMap<String, Object>> splitFields = SplitFieldManager.getSplitFields();
		LoaderFactory.setCurrentLoader(LoaderFactory.FILE_LOADER);
		ExtractorFactory.setCurrentExtractor(extractorName);		
		int counter = 0;
		for (HashMap<String, Object> splitFieldsValues : splitFields) {	
			GregorianCalendar gcal = new GregorianCalendar();
			gcal.setTime(startDate);
			do{
				Date currentDate = gcal.getTime();

				Action toDo = new Action(++counter);
				MetricKey mkDau = MetricKey.generateKey(MetricKey.METRIC_DAU, splitFieldsValues, currentDate);	
				if(actionName.equals("load")){
					toDo.putInList(mkDau, actionName);
				}
				if(actionName.equals("compile_xau")){					
					MetricKey mkWau = MetricKey.generateKey(MetricKey.METRIC_WAU, splitFieldsValues, currentDate);	
					MetricKey mkMau = MetricKey.generateKey(MetricKey.METRIC_MAU, splitFieldsValues, currentDate);	
					MetricKey mkRDau = MetricKey.generateKey(MetricKey.METRIC_RDAU, splitFieldsValues, currentDate);	

					toDo.putInList(mkDau, actionName);
					toDo.putInList(mkWau, actionName);
					toDo.putInList(mkMau, actionName);
					toDo.putInList(mkRDau, actionName);				
				}
				Core.putInQueue(toDo);
				Core.run(false);
				
				gcal.add(Calendar.DAY_OF_YEAR, 1);				
			}while(!DateFormat.addDays(endDate, 1).equals(gcal.getTime()));
			Core.run(true);			
			FileMap.free();
			System.gc();			
			Message.log("SPLIT FIELDS : ["+ MetricKey.toStringSplitFields(splitFieldsValues) +"] INTERVAL:["+DateFormat.format(startDate, "yyyy-MM-dd") +"] -> ["+DateFormat.format(endDate, "yyyy-MM-dd") +"]");
		}
		Profiler.logAllReports(false);
		Message.log("STOP  [Action:"+actionName+"] INTERVAL:["+DateFormat.format(startDate, "yyyy-MM-dd") +"] -> ["+DateFormat.format(endDate, "yyyy-MM-dd") +"]");
		Profiler.stop("TOTAL TIME (Action:"+actionName+") ["+DateFormat.format(startDate, "yyyy-MM-dd") +"] -> ["+DateFormat.format(endDate, "yyyy-MM-dd") +"]", true);
	}
	
}
