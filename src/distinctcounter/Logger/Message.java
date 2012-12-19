/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Logger;

import distinctcounter.Db.Db;
import distinctcounter.Tools.CommonDb;
import distinctcounter.Tools.InstanceDetails;


/**
 *
 * @author stefan
 */
public class Message {
	
	private static String MSG;
	private static String TABLE_NAME = "j_distinctcounter__messages";
	
	private final static String DB_NAME = CommonDb.getCurrentDb();
	
	public synchronized static void log(String msg){
		msg = msg.replace("'", "\\'");
		try {
			String insertSql = ""
				+ "INSERT DELAYED INTO "+DB_NAME+"."+TABLE_NAME+""
				+ "(instance_id,msg)"
				+ "VALUES"
				+ "("+InstanceDetails.getInstanceId()+",'"+msg+"')";		
			Db.getCurrentConnection().exec(insertSql);
			System.out.println("MESSAGE: "+msg);
		} catch (Exception ex) {
			Exception e = new Exception(msg);
			e.printStackTrace();
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
}
