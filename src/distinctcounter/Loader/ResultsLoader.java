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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author stefan
 */
public class ResultsLoader {
	
	private final static String TABLE_NAME = Configuration.XML_RO.getElementByTagName("destination_table");
	private final static String DB_NAME = Configuration.PROPS_RO.getProperty("server_db");
    
    private static LinkedHashMap<String,String> COLUMNS = new LinkedHashMap<>();
	
    public static void createTable() throws Exception{ 
        COLUMNS.put("short_date","date");
        COLUMNS.put("xau_type","varchar(32)");
		
		NodeList nodes = Configuration.XML_RO.getNodesByTagName("split_field");
        for(int i=0, iMax=nodes.getLength(); i<iMax; i++){
            Element e = (Element)nodes.item(i);            
            String splitField = e.getElementsByTagName("name").item(0).getTextContent();
			COLUMNS.put(splitField,"int");
		}
        
        COLUMNS.put("distinct_values","int");
        
        String sSql = "CREATE TABLE IF NOT EXISTS "+DB_NAME+"."+TABLE_NAME+" (";
        
//        for (String column: COLUMNS.keySet()) {
//            sSql += " "+column+" "+COLUMNS.get(column) + " NOT NULL,";
//        }
		
		for (Entry<String, String> column : COLUMNS.entrySet()){
			sSql += " "+column.getKey()+" "+column.getValue()+ " NOT NULL,";
		}
        
        sSql = sSql.substring(0, sSql.length()-1);
        sSql += " ) ENGINE=MyISAM";
        Db.getCurrentConnection().exec(sSql);
    }
    
	public static void load(MetricKey mk, int result) throws Exception{
		String insertSql = ""
				+ "INSERT INTO "+DB_NAME+"."+TABLE_NAME+""
				+ "(" + COLUMNS.toString() + ")"
				+ "VALUES"
				+ "('"+DateFormat.format(mk.getCurrentDate(), "yyyy-MM-dd")+"','"+mk.getMetricName()+"',"+mk.toStringSplitFieldsValues()+","+result+")";
		Db.getCurrentConnection().exec(insertSql);
	}
	
}
