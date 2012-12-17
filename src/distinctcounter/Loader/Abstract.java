/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Loader;

import distinctcounter.Tools.MetricKey;
import javaewah.EWAHCompressedBitmap;

/**
 *
 * @author stefan
 */
public abstract class Abstract {
	
	protected MetricKey mk;
	
	public Abstract(MetricKey mk){
		this.mk = mk;
	}
	
	public abstract void load() throws Exception;
	
	public void getDataSize() throws Exception{
		throw new Exception("Not implemented !!!");
	}
	
	public abstract EWAHCompressedBitmap getBitSet() throws Exception;
}
