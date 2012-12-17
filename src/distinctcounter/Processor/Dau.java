/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Processor;

import distinctcounter.Loader.LoaderFactory;
import distinctcounter.Logger.Profiler;
import distinctcounter.Tools.MetricKey;
import javaewah.EWAHCompressedBitmap;

/**
 *
 * @author stefan
 */
public class Dau extends Abstract{ 
	
	public Dau(MetricKey mk){
		super(mk);
	}
	
	@Override
	public synchronized int process() throws Exception {
		Profiler.start("["+mk.toStringShort()+"]Total");
		EWAHCompressedBitmap cb = LoaderFactory.getLoader(LoaderFactory.getCurrentLoader(), mk).getBitSet();
		int cardinality = cb.cardinality();
		Profiler.stop("["+mk.toStringShort()+"]Total");
		
		return cardinality;
	}
	
}
