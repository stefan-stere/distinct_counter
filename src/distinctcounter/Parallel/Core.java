/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distinctcounter.Parallel;

import distinctcounter.configuration.Configuration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author stefan
 */
public class Core {
	
	private static int NB_THREADS = Integer.parseInt(Configuration.PROPS_RO.getProperty("number_threads"));
	private static long SEC_TO_WAIT_THREAD = 100;
	private static List<Action> ACTION_QUEUE = Collections.synchronizedList(new ArrayList<Action>());
	private static ExecutorService ACTION_EXECUTOR;
	
	public static void putInQueue(Action toDo){
		if(!ACTION_QUEUE.contains(toDo)){
			ACTION_QUEUE.add(toDo);
		}
	}
	
	public static void run(boolean forced) throws InterruptedException{
		if(ACTION_QUEUE.size() != NB_THREADS && ACTION_QUEUE.size() > 0 && forced == false ){
			return;
		}
		
		ACTION_EXECUTOR = Executors.newCachedThreadPool();
		for(Action toDo : ACTION_QUEUE){
			ACTION_EXECUTOR.execute(toDo);
		}
		ACTION_EXECUTOR.shutdown();
		ACTION_EXECUTOR.awaitTermination(SEC_TO_WAIT_THREAD, TimeUnit.SECONDS);
		
		ACTION_QUEUE.clear();
		System.gc();
	}
	
}
