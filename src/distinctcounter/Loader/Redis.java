/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Loader;

import distinctcounter.Extractor.ActiveUsers;
import distinctcounter.Extractor.ExtractorFactory;
import distinctcounter.Logger.Profiler;
import distinctcounter.Tools.DateFormat;
import distinctcounter.Tools.MetricKey;
import java.sql.ResultSet;
import java.util.BitSet;
import javaewah.EWAHCompressedBitmap;
import redis.clients.jedis.Jedis;

/**
 *
 * @author stefan
 */
public class Redis extends Abstract{
	
	private static Jedis CONN = null;
	
	public Redis(MetricKey mk){
		super(mk);
	}
	
	@Override
	public void load() throws Exception{		
		Profiler.start("["+mk.toString()+"]Load");
				
		ResultSet rs = ExtractorFactory.getCurrentExtractor().useMetricKey(mk).getData();
		String redisKey = Redis.getRedisKey(mk);
		while(rs.next()){
			Redis.getConnection().setbit(redisKey, rs.getInt(ActiveUsers.KEY), true);
		}
		
		Profiler.stop("["+mk.toString()+"]Load");
	}
	
	public static String getRedisKey(MetricKey mk){	
		String tableDate = DateFormat.format(mk.getCurrentDate(),"yyyyMMdd");
		String filename = mk.getMetricName()+"_"+mk.toStringSplitFieldsToFile()+"__"+tableDate;
		
		return filename;
	}
	
	public static Jedis getConnection(){
		if(Redis.CONN == null){
			Redis.CONN = new Jedis("localhost");
			Redis.CONN.connect();
		}
		
		return Redis.CONN;
	}
	

	@Override
	public EWAHCompressedBitmap getBitSet() throws Exception{
		Profiler.start("["+mk.toString()+"]GetBitSet");
		
		EWAHCompressedBitmap cb = new EWAHCompressedBitmap();
		BitSet bs = BitSet.valueOf(Redis.getConnection().get(Redis.getRedisKey(mk).getBytes()));
		for(int i=0; i<bs.size(); i++){
			if(bs.get(i)){
				cb.set(i);
			}			
		}
		
		Profiler.stop("["+mk.toString()+"]GetBitSet");
		
		return cb;
	}
	
}
