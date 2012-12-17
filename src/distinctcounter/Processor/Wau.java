/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Processor;

import distinctcounter.Loader.LoaderFactory;
import distinctcounter.Logger.Profiler;
import distinctcounter.Tools.DateFormat;
import distinctcounter.Tools.MetricKey;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javaewah.EWAHCompressedBitmap;

/**
 *
 * @author stefan
 */
public class Wau extends Abstract{

	public Wau(MetricKey mk){ 
		super(mk);
	}
	
	@Override
	public synchronized int process() throws Exception {
		Profiler.start("["+mk.toStringShort()+"]Total");
		
		Date endDate = mk.getCurrentDate();
		Date startDate = DateFormat.addDays(endDate, -7);
		
		EWAHCompressedBitmap cbRes = new EWAHCompressedBitmap();
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(startDate);
		do{
			gcal.add(Calendar.DAY_OF_YEAR, 1);
			Date currentDate = gcal.getTime();
			MetricKey currentMk = MetricKey.generateKey(mk.getMetricName(), mk.getSplitFields(), currentDate);
			EWAHCompressedBitmap cbTmp = LoaderFactory.getLoader(LoaderFactory.getCurrentLoader(), currentMk).getBitSet();
			cbRes = cbRes.or(cbTmp);			
		}while(!gcal.getTime().equals(endDate));
		int cardinality = cbRes.cardinality();
		Profiler.stop("["+mk.toStringShort()+"]Total");
		
		return cardinality;
	}
	
}
