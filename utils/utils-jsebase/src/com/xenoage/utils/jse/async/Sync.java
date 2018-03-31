package com.xenoage.utils.jse.async;

import com.xenoage.utils.async.AsyncProducer;
import com.xenoage.utils.async.AsyncResult;

/**
 * This class allows to wrap asynchronous methods into
 * blocking methods for convenience.
 * 
 * @author Andreas Wenger
 */
public class Sync {
	
	private static class State<T> {
		public Object lock = new Object();
		public boolean finished = false;
		public T data = null;
		public Exception exception = null;
	}
	
	/**
	 * Runs the given {@link AsyncProducer} and waits for the result.
	 * The result is returned or an exception is thrown.
	 */
	public static <T> T sync(AsyncProducer<T> producer)
		throws Exception {
		//start async production
		final State<T> state = new State<>();
		producer.produce(new AsyncResult<T>() {
	
			@Override public void onSuccess(T data) {
				state.data = data;
				synchronized (state.lock) {
					state.lock.notify();
					state.finished = true;
				}
			}
	
			@Override public void onFailure(Exception ex) {
				state.exception = ex;
				synchronized (state.lock) {
					state.lock.notify();
					state.finished = true;
				}
			}
		});
		//wait for async production to finish
		synchronized (state.lock) {
			try {
				//if the producer was faster then the main thread, we would
				//wait forever, so we use an additional boolean variable
				if (false == state.finished)
					state.lock.wait();
			} catch (InterruptedException ex) {
				throw ex;
			}
		}
		//throw exception or return result
		if (state.exception != null)
			throw state.exception;
		else
			return state.data;
	}

}
