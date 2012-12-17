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
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import javaewah.EWAHCompressedBitmap;

/**
 *
 * @author stefan
 */
public class File extends Abstract{
	
	private static Integer HIT_THRESHOLD = 1;	
	
	
	public File(MetricKey mk){
		super(mk);
	} 
	
	
	@Override
	public synchronized void load() throws Exception{		
		ResultSet rs = ExtractorFactory.getCurrentExtractor().useMetricKey(mk).getData();
		Profiler.start("["+mk.toStringShort()+"]Load");
		EWAHCompressedBitmap cb = new EWAHCompressedBitmap();
		while(rs.next()){
			int key = rs.getInt(ActiveUsers.KEY);
			cb.set(key);
		}
		DataOutput output = new DataOutputStream(new FileOutputStream(File.getFileName(mk,true)));
		rs.close();
		cb.serialize(output);
		cb.clear();
		
		Profiler.stop("["+mk.toStringShort()+"]Load");
	}
	
	private synchronized ByteBuffer getByteBufferForFile(String fname, Integer capacity) throws Exception {		
		RandomAccessFile aFile = new RandomAccessFile(fname, "rw");
		FileChannel vectorChannel = aFile.getChannel();
		int internalCapacity;
		if(capacity == null){
			internalCapacity = (int)vectorChannel.size();
		}else{
			internalCapacity = capacity;
		}
		ByteBuffer vector = vectorChannel.map(FileChannel.MapMode.READ_WRITE, 0, internalCapacity);
		vectorChannel.close();
		aFile.close();
		
		return vector;
	}
	
	@Override
	public synchronized EWAHCompressedBitmap getBitSet() throws Exception{
		Profiler.start("[TOTAL GetBitSet]", true);
		
		EWAHCompressedBitmap cb = new EWAHCompressedBitmap();
		String fileName = File.getFileName(mk,false);
		if(fileName != null){
			if(!FileMap.containsKey(fileName)){
				DataInput input = new DataInputStream(new FileInputStream(fileName));
				cb.deserialize(input);
				FileMap.put(fileName, cb);	
			}	
		}	
		Profiler.stop("[TOTAL GetBitSet]", true);		

		return (fileName != null) ? FileMap.get(fileName) : cb;
	}
	
	private static synchronized String getFileName(MetricKey mk, boolean withCreate) throws IOException{	
		String tableDate = DateFormat.format(mk.getCurrentDate(),"yyyyMMdd");
		String path = FileMap.getCurrentPath() + "dau" +"/"+mk.toStringSplitFieldsToFile()+"/";
		String filename = path + tableDate + ".bin";
		
		java.io.File f = new java.io.File(filename);
		java.io.File d = new java.io.File(path);
		if((!d.exists() || !f.exists()) && !withCreate){
			//throw new java.io.FileNotFoundException(filename);
			return null;
		}
		d.mkdirs();
		f.createNewFile();
		
		return filename;
	}
	
	/*
	public void load_old() throws Exception{		
		Profiler.start("["+mk.toString()+"]Load");
		
		ResultSet rs = ExtractorFactory.getCurrentExtractor().useMetricKey(mk).getData();
		BitSet bs = new BitSet();
		while(rs.next()){
			int key = rs.getInt(ActiveUsers.KEY);
			bs.set(key, true);
		}
		byte[] res = bs.toByteArray();
		ByteBuffer buf = this.getByteBufferForFile(File.getFileName(mk,true), res.length+1);
		buf.put(res);	
		
		Profiler.stop("["+mk.toString()+"]Load");
	}
	*/
	
	/*
	public BitSet getBitSet_old() throws Exception{
		Profiler.start("["+mk.toString()+"]GetBitSet");
		
		BitSet bs = new BitSet();
		ByteBuffer bb = this.getByteBufferForFile(File.getFileName(mk,false), null);
		int counter=0;
		for(int i = 0; i<bb.capacity(); i++){
			byte b = bb.get(i);
			if(b>1){
				counter++;
			}
			for(int j = 0; j<8; j++){	
				int pos = 8*i+j;
				int val = b&(1<<j);
				boolean bVal = (val > 0) ? true : false;
				bs.set(pos, bVal);
			}
		}
		Profiler.stop("["+mk.toString()+"]GetBitSet");
		
		return bs;
	}
	*/

	
}
