/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.configuration;

import distinctcounter.Db.ConnectionSettings;
import distinctcounter.Db.Db;
import distinctcounter.Loader.FileMap;
import distinctcounter.Loader.ResultsLoader;
import distinctcounter.Main;
import distinctcounter.Tools.InstanceDetails;
import distinctcounter.Tools.SplitFieldManager;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author stefan
 */
public class Configuration {
    
    public final static String PROP_FILE = "main.properties";
    public final static String XML_FILE = "main.xml";
    public final static ReadOnlyProperties PROPS_RO = new ReadOnlyProperties();
    public final static XmlReader XML_RO = new XmlReader();
    
    private static String PATH_TO_CONFIG;
    
    private static void preInit(){
        InstanceDetails.startInstance();
        String path = Configuration.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if(path.endsWith(".jar")){
           path = path.replaceFirst("/[a-zA-Z0-9]+\\.jar", "/");
        }
        PATH_TO_CONFIG = path;
    }
    
    private static void postInit() throws Exception{
        Db.setCurrentConnection(new ConnectionSettings(
                PROPS_RO.getProperty("server_host"),
                PROPS_RO.getProperty("server_user"),
                PROPS_RO.getProperty("server_password"),
                PROPS_RO.getProperty("server_db")
        ));
        
        FileMap.init(PROPS_RO.getProperty("binary_files_path"));
        SplitFieldManager.load();
        
        ResultsLoader.createTable();
    }
    
    public static void load() throws Exception{
        preInit();
        loadProperties();
        XML_RO.load();
        postInit();
    }
    
    public static String getPathToConfig(){
        return PATH_TO_CONFIG;
    }
    
    private static void loadProperties() throws IOException{
        if(!PROPS_RO.isEmpty()){
            return;
        }
        
        
        Reader fileReader = new FileReader(PATH_TO_CONFIG+"configuration/"+PROP_FILE);        
        //Reader fileReader = new FileReader(Configuration.class.getResource(PROP_FILE).toString().replace("file:", ""));        
        PROPS_RO.load(fileReader);
    }
    
}
