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
public class Blank extends Abstract{

	public Blank(MetricKey mk){
		super(mk); 
	}
	
	@Override
	public int process() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
