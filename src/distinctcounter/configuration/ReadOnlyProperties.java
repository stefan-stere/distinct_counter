/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.configuration;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 *
 * @author stefan
 */
public class ReadOnlyProperties {
    
    private Properties props;
    
    public ReadOnlyProperties(){
        props = new Properties();
    }
    
    public void load(Reader reader) throws IOException{
        props.load(reader);
    }
    
    public String getProperty(String propertyName){
        return props.getProperty(propertyName);
    }
    
    public boolean isEmpty(){
        return props.isEmpty();
    }
    
    
}
