/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Loader;

import distinctcounter.Tools.MetricKey;

/**
 *
 * @author stefan
 */
public class LoaderFactory {
	
	public static final int FILE_LOADER  = 1;
	public static final int REDIS_LOADER = 2;
	
	private static int CURRENT_LOADER = 0;
	
	public static void setCurrentLoader(int loaderId) throws Exception{
		if(LoaderFactory.CURRENT_LOADER == 0){
			LoaderFactory.CURRENT_LOADER = loaderId;
		}else{
			throw new Exception("The current loader was already set to loaderId :" + LoaderFactory.CURRENT_LOADER);
		}		
	}
	
	public static int getCurrentLoader(){
		return LoaderFactory.CURRENT_LOADER;
	}
	
	public static Abstract getLoader(int loaderId, MetricKey mk) throws Exception{		
		Abstract loader;		
		switch(loaderId){
			case LoaderFactory.FILE_LOADER : 
				loader = new File(mk);
				break;
				
			case LoaderFactory.REDIS_LOADER : 
				loader = new Redis(mk);
				break;
				
			default:
				throw new Exception("Undefined loaderId :" + loaderId);
		}
		
		return loader;
	}
	
}
