/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Tools;

/**
 *
 * @author stefan
 */
public class InstanceDetails {
	
	private static int INSTANCE_ID = 0;
	
	private static int PRECISION  = 1000000000;
	
	public synchronized static void startInstance(){
		if(INSTANCE_ID == 0){
			INSTANCE_ID = (int) Math.round(Math.random()*PRECISION);
		}
	}
	
	public synchronized static int getInstanceId(){
		return INSTANCE_ID;
	}
	
}
