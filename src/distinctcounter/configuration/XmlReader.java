/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.configuration;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author stefan
 */
public class XmlReader {
    
    private Document XML = null;
    private Element root = null;
    
    public void load() throws Exception{
        if(XML != null){
            return;
        }
        
        File xml = new File(Configuration.getPathToConfig()+"configuration/"+Configuration.XML_FILE);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        XML = dBuilder.parse(xml);
    } 
    
    public String getElementByTagName(String tagName){
        NodeList nl = XML.getElementsByTagName(tagName);
        
        if(XML.getElementsByTagName(tagName).item(0) == null){
            return null;
        }
        return ((Element)XML.getElementsByTagName(tagName).item(0)).getTextContent();
    }
    
    public String[] getElementsByTagName(String tagName, String childTagName){
        NodeList nodes = XML.getElementsByTagName(tagName);
        String[] nodesValues = new String[nodes.getLength()];
        for(int i=0, iMax=nodes.getLength(); i<iMax; i++){
            Element e = (Element)nodes.item(i);
            nodesValues[i] = e.getElementsByTagName(childTagName).item(0).getTextContent();
        }
        
        return nodesValues;
    }
    
    public NodeList getNodesByTagName(String tagName){
        return XML.getElementsByTagName(tagName);
    }
}
