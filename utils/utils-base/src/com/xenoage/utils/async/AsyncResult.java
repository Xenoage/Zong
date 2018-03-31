package com.xenoage.utils.async;


/**
 * Callback methods for an asynchronous call with
 * a result object.
 * 
 * @author Andreas Wenger
 */
public interface AsyncResult<T> {

	/**
	 * This method is called when the operation was successful.
	 * @param data  the resulting data
	 */
	public void onSuccess(T data);
	
	/**
	 * This method is called when the operation was not successful.
	 * @param ex  details about the error
	 */
	public void onFailure(Exception ex);
	
}
