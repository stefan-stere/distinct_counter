/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Processor;

import distinctcounter.Tools.MetricKey;

/**
 *
 * @author stefan
 */
public class ProcessorManager {
	
	public static synchronized int getResult(MetricKey mk) throws Exception{
		Abstract processor = new Blank(mk);
		
		if(mk.getMetricName().equals(MetricKey.METRIC_DAU)){
			processor = new Dau(mk);
		}		
		if(mk.getMetricName().equals(MetricKey.METRIC_WAU)){
			processor = new Wau(mk);
		}		
		if(mk.getMetricName().equals(MetricKey.METRIC_MAU)){
			processor = new Mau(mk);
		}
		if(mk.getMetricName().equals(MetricKey.METRIC_RDAU)){
			processor = new RDau(mk);
		}
		
		return processor.process();
	}
	
}
