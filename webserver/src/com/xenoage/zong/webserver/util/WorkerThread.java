package com.xenoage.zong.webserver.util;



/**
 * A {@link Thread} which runTry()-method may throw an {@link Exception}.
 * It is wrapped into a {@link RuntimeException}.
 * 
 * @author Andreas Wenger
 */
public abstract class WorkerThread
	extends Thread
{
	
	@Override public void run()
	{
		try {
			runTry();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public abstract void runTry()
		throws Exception;

}
