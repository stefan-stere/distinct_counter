/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Processor;

import distinctcounter.Loader.LoaderFactory;
import distinctcounter.Logger.Profiler;
import distinctcounter.Tools.DateFormat;
import distinctcounter.Tools.MetricKey;
import javaewah.EWAHCompressedBitmap;

/**
 *
 * @author stefan
 */
public class RDau extends Abstract{
	
	public RDau(MetricKey mk){ 
		super(mk);
	}
	
	@Override
	public synchronized int process() throws Exception {
		Profiler.start("["+mk.toStringShort()+"]Total");
		EWAHCompressedBitmap cb = LoaderFactory.getLoader(LoaderFactory.getCurrentLoader(), mk).getBitSet();
		MetricKey mkDayAgo = MetricKey.generateKey(MetricKey.METRIC_RDAU, mk.getSplitFields(), DateFormat.addDays(mk.getCurrentDate(), -1));
		EWAHCompressedBitmap cbDayAgo = LoaderFactory.getLoader(LoaderFactory.getCurrentLoader(), mkDayAgo).getBitSet();
		cb = cb.and(cbDayAgo);
		int cardinality = cb.cardinality();
		Profiler.stop("["+mk.toStringShort()+"]Total");
		
		return cardinality;
	}
	
}