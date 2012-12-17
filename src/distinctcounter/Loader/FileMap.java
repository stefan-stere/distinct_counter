/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Loader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javaewah.EWAHCompressedBitmap;

/**
 *
 * @author stefan
 */
public class FileMap {
	
	private static Map<String, EWAHCompressedBitmap> MAP_BITSETS;
	
	private static String CURRENT_ROOT_PATH = "";
	
	public static synchronized void put(String fileName, EWAHCompressedBitmap bitmap){
		if(!MAP_BITSETS.containsKey(fileName)){			
			MAP_BITSETS.put(fileName, bitmap);
		}
	}
	
	public static synchronized EWAHCompressedBitmap get(String fileName){
		if(MAP_BITSETS.containsKey(fileName)){			
			return MAP_BITSETS.get(fileName);
		}
		
		return new EWAHCompressedBitmap();
	}
	
	public static synchronized void free(){
		MAP_BITSETS.clear();
	}
	
	public static synchronized boolean containsKey(String fileName){
		return MAP_BITSETS.containsKey(fileName);
	}
	
	public static synchronized void init(String rootPath){
		MAP_BITSETS = Collections.synchronizedMap(new HashMap<String, EWAHCompressedBitmap>());
		CURRENT_ROOT_PATH = rootPath;
	}
	
	public static synchronized String getCurrentPath(){
		return CURRENT_ROOT_PATH;
	}
}
