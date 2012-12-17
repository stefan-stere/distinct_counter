/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Loader;

import distinctcounter.Db.Db;
import distinctcounter.Tools.DateFormat;
import distinctcounter.Tools.MetricKey;
import distinctcounter.configuration.Configuration;
import java.util.ArrayList;

/**
 *
 * @author stefan
 */
public class ResultsLoader {
	
	private final static String TABLE_NAME = Configuration.XML_RO.getElementByTagName("destination_table");
	private final static String DB_NAME = Configuration.PROPS_RO.getProperty("server_db");
    
    private static ArrayList<String> COLUMNS = new ArrayList<String>();
	
    public static void creatTable(){
        COLUMNS.add("short_date");
        COLUMNS.add("xau_type");
        
        String splitField1 = Configuration.XML_RO.getElementByTagName("split_field_1");
        String splitField2 = Configuration.XML_RO.getElementByTagName("split_field_2");
        
        if(splitField1 != null){
            COLUMNS.add(splitField1);
        }
        if(splitField2 != null){
            COLUMNS.add(splitField2);
        }
        
        COLUMNS.add("distinct_values");
    }
    
	public static void load(MetricKey mk, int result) throws Exception{
		String insertSql = ""
				+ "INSERT INTO "+DB_NAME+"."+TABLE_NAME+""
				+ "(" + COLUMNS.toString() + ")"
				+ "VALUES"
				+ "('"+DateFormat.format(mk.getCurrentDate(), "yyyy-MM-dd")+"',"+mk.toStringSplitFieldsToFile()+",'"+mk.getMetricName()+"',"+result+")"
				+ "ON DUPLICATE KEY UPDATE users_count=VALUES(users_count)";
		Db.getCurrentConnection().exec(insertSql);
	}
	
}
