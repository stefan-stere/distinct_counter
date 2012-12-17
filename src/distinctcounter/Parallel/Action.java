/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Parallel;

import distinctcounter.Loader.LoaderFactory;
import distinctcounter.Loader.ResultsLoader;
import distinctcounter.Logger.Message;
import distinctcounter.Processor.ProcessorManager;
import distinctcounter.Tools.MetricKey;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stefan
 */
public class Action implements Runnable{

	protected Map<MetricKey, String> keysToProcess;
	private int identificationNb = 0;
	
	public Action(int identificationNb){	
		this.identificationNb = identificationNb;
		keysToProcess = Collections.synchronizedMap(new HashMap<MetricKey, String>());
	}

	public synchronized void putInList(MetricKey mk, String ActionName){
		if(!keysToProcess.containsKey(mk)){
			keysToProcess.put(mk, ActionName);
		}
	}
	
	@Override
	public synchronized void run() {		
		try {
			if(keysToProcess.isEmpty()){
				throw new Exception("The key list is empty!!!");
			}
			for (Map.Entry<MetricKey, String> entry : keysToProcess.entrySet()) {
				MetricKey metricKey = entry.getKey();
				String actionName = entry.getValue();
				if(actionName.equals("load")){
					LoaderFactory.getLoader(LoaderFactory.getCurrentLoader(), metricKey).load();
				}
				if(actionName.equals("compile_xau")){
					ResultsLoader.load(metricKey, ProcessorManager.getResult(metricKey));
				}				
			}	
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			Message.log(sw.toString());
			System.exit(1);
		}
	}
	
}
