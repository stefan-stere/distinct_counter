/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Extractor;

import distinctcounter.Db.Db;
import distinctcounter.Logger.Profiler;
import distinctcounter.Tools.DateFormat;
import distinctcounter.configuration.Configuration;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author stefan
 */
public class ActiveUsers extends Abstract{
	
	public final static String KEY = "za_key";
	
	private final static String DB_NAME = Configuration.PROPS_RO.getProperty("server_db");
	
	@Override
	public synchronized ResultSet getData() throws Exception{
		Profiler.start("["+mk.toStringShort()+"]Extract");
        
        String distinctField = Configuration.XML_RO.getElementByTagName("distinct_value_field");
        String dateField = Configuration.XML_RO.getElementByTagName("date_field");
        
        String tableName = Configuration.XML_RO.getElementByTagName("name_format");
        String tableType = Configuration.XML_RO.getElementByTagName("type"); 
        if(tableType != null && tableType.equals("merge")){
            tableName = format(tableName, mk.getCurrentDate());
        }
        String sqlCond = formatSql(mk.getSplitFields());

		String sSql = ""
			+ "SELECT SQL_NO_CACHE "
			+  " `"+distinctField+"` AS "+ActiveUsers.KEY+" "
			+ "FROM "+DB_NAME+"."+tableName+" "
			+ "WHERE 1 "
            + " AND `"+dateField+"` = '"+DateFormat.format(mk.getCurrentDate(), "yyyy-MM-dd") +"' "
            + sqlCond + " "
			+ "GROUP BY "+ActiveUsers.KEY+" "
			+ "ORDER BY NULL";
		ResultSet rs = Db.getCurrentConnectionNew().fetchAll(sSql);
		Profiler.stop("["+mk.toStringShort()+"]Extract");	
		
		//System.out.println(sSql);
		
		return rs;
	}
    
    private synchronized String format(String name, Date day){
        name = name.replace("yyyy", DateFormat.format(day, "yyyy"));
        name = name.replace("MM", DateFormat.format(day, "MM"));
        name = name.replace("dd", DateFormat.format(day, "dd"));
        
        return name;
    }
    
    private synchronized String formatSql(HashMap<String, Object> splitFields){
        String sql = " ";
        for (String field : splitFields.keySet()) {
            sql += " AND " + field + " = '" + splitFields.get(field).toString() + "' ";
        }
        return sql;
    }
}
