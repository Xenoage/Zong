package com.xenoage.utils.io;

/**
 * System independent interface for files.
 * 
 * For example, there may be implementations for Java SE, Android or GWT.
 * 
 * @author Andreas Wenger
 */
public interface File
	extends FilesystemItem {

	/**
	 * Gets the size of this file in bytes, or null if unknown or not specified.
	 */
	public Long getSizeInBytes();

}
