/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Tools;

import distinctcounter.Db.Db;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stefan
 */
public class GameManager {
	
	private static List<GameDetails> GAMES = new ArrayList<GameDetails>();
	
	private final static String DB_NAME = CommonDb.getCurrentDb();
	
	public static List<GameDetails> load(int gameReportId) throws Exception{
		if(!GameManager.GAMES.isEmpty()){
			return GameManager.GAMES;
		}		
		
		String sSql = ""
			+ "SELECT SQL_NO_CACHE "
			+ "id,name,platform,IF(launch_date='0000-00-00','none',launch_date) as launch_date "
			+ "FROM "+DB_NAME+".d_game_report "
			+ "WHERE 1 ";
		if(gameReportId !=0 ){
			sSql += "AND id = "+gameReportId;
		}
		ResultSet rs = Db.getCurrentConnection().fetchAll(sSql);
		
		while(rs.next()){
			GameManager.GAMES.add(new GameDetails(rs.getInt("id"), rs.getString("platform"), rs.getString("name"), rs.getString("launch_date")));
		}
		
		return GameManager.GAMES;
	}
	
}
