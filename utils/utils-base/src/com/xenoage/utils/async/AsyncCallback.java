package com.xenoage.utils.async;


/**
 * Callback methods for an asynchronous call
 * without a result object.
 * 
 * @author Andreas Wenger
 */
public interface AsyncCallback {

	/**
	 * This method is called when the operation was successful.
	 */
	public void onSuccess();
	
	/**
	 * This method is called when the operation was not successful.
	 * @param ex  details about the error
	 */
	public void onFailure(Exception ex);
	
}
