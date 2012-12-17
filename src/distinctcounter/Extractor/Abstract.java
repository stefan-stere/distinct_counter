/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Extractor;

import distinctcounter.Tools.MetricKey;
import java.sql.ResultSet;

/**
 *
 * @author stefan
 */
public abstract class Abstract {
	
	protected MetricKey mk;
	
	public Abstract useMetricKey(MetricKey mk){
		this.mk = mk;
		return this;
	}
	
	public abstract ResultSet getData() throws Exception;
	
}
