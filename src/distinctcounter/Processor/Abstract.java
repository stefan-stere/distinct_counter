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
public abstract class Abstract{
	
	protected MetricKey mk;
	
	public Abstract(MetricKey mk){
		this.mk = mk;
	}
	
	public abstract int process() throws Exception;
	
}
