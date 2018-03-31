package com.xenoage.utils.async;

/**
 * Interface for a class which produces some object
 * asynchronously.
 * 
 * @author Andreas Wenger
 */
public interface AsyncProducer<T> {
	
	public void produce(AsyncResult<T> result);

}
