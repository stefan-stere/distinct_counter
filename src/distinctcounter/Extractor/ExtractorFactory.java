/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Extractor;

import java.util.HashMap;

/**
 *
 * @author stefan
 */
public class ExtractorFactory {
	
	private static HashMap<String,Abstract> EXTRACTORS = null;
	
	private static String CURRENT_EXTRACTOR_NAME = null;
	
	public synchronized static void setCurrentExtractor(String extractorName){
		ExtractorFactory.init();
		if(!ExtractorFactory.EXTRACTORS.containsKey(extractorName)){
			throw new UnsupportedOperationException("Cannot set current extractor because no extractor was found with the name: ["+extractorName+"]");
		}
		ExtractorFactory.CURRENT_EXTRACTOR_NAME = extractorName;
	}
	
	public synchronized static Abstract getCurrentExtractor(){
		return ExtractorFactory.EXTRACTORS.get(ExtractorFactory.CURRENT_EXTRACTOR_NAME);
	}
	
	private synchronized static void init(){
		if (ExtractorFactory.EXTRACTORS == null) {
			ExtractorFactory.EXTRACTORS = new HashMap<String, Abstract>();
			ExtractorFactory.EXTRACTORS.put("active_users", new ActiveUsers());
		}		
	}
	
	public synchronized static Abstract getExtractor(String extractorName){
		ExtractorFactory.init();		
		
		if(!ExtractorFactory.EXTRACTORS.containsKey(extractorName)){
			throw new UnsupportedOperationException("No extractor was found with the name: ["+extractorName+"]");
		}
		
		return ExtractorFactory.EXTRACTORS.get(extractorName);
	}
	
}
