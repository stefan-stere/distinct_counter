/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Tools;

/**
 *
 * @author stefan
 */
public class GameDetails {
	
	public final int gameReportId;
	public final String platform;
	public final String name;
	public final String launchDate;
	
	public GameDetails(int gameReportId, String platform, String name, String launchDate){
		this.gameReportId = gameReportId;
		this.platform = platform;
		this.name = name;
		this.launchDate = launchDate;
	}
	
}
