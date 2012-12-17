/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Tools;

/**
 *
 * @author stefan
 */
public class CommonDb {
	
	private static String CURRENT_DB = "";
	
	public static final String DB__LOCALHOST = "backoffice";
	public static final String DB__IDW = "tdw__igp";
	
	public static void setCurrentDb(String dbUsed){
		if(CURRENT_DB.isEmpty()){
			CURRENT_DB = dbUsed;
		}
	}
	
	public static String getCurrentDb(){
		return CURRENT_DB;
	}
	
}
