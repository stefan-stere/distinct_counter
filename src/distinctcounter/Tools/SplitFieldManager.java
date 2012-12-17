/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Tools;

import distinctcounter.Db.Db;
import distinctcounter.configuration.Configuration;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author stefan
 */
public class SplitFieldManager {
    
    private static HashMap<String, List<Object>> SPLIT_FIELDS = new HashMap<String, List<Object>>();
    
    public static void load() throws SQLException{
        NodeList nodes = Configuration.XML_RO.getNodesByTagName("split_field");
        for(int i=0, iMax=nodes.getLength(); i<iMax; i++){
            Element e = (Element)nodes.item(i);            
            String splitField = e.getElementsByTagName("name").item(0).getTextContent();
            String sourceQuery = e.getElementsByTagName("source_query").item(0).getTextContent();
            
            SPLIT_FIELDS.put(splitField, Db.getCurrentConnection().fetchCol(sourceQuery));
        }
    }
    
    public static List<HashMap<String, Object>> getSplitFields(){
        if(SPLIT_FIELDS.keySet().size() > 2){
            throw new UnsupportedOperationException("You can have more then 2 split fields!");
        }
        
        List<HashMap<String, Object>> splitFields = new ArrayList<HashMap<String, Object>>();
        String[] fields = new String[2];
        fields = SPLIT_FIELDS.keySet().toArray(fields);
        
        if(fields[0] != null){
            for (Object value0 : SPLIT_FIELDS.get(fields[0])) {
                if(fields[1] != null){
                    for (Object value1 : SPLIT_FIELDS.get(fields[1])) {
                        HashMap<String, Object> entry = new HashMap<String, Object>();
                        entry.put(fields[0], value0);
                        entry.put(fields[1], value1);
                        splitFields.add(entry);
                    }
                }else{
                    HashMap<String, Object> entry = new HashMap<String, Object>();
                    entry.put(fields[0], value0);
                    splitFields.add(entry);
                }
            }
        }        
        
        return splitFields;
    }
    
}
