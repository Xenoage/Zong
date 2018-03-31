package com.xenoage.utils.io;


/**
 * Interface for {@link File} and {@link Directory}.
 * 
 * @author Andreas Wenger
 */
public interface FilesystemItem {
	
	/**
	 * Returns the name of the file or directory denoted by this abstract pathname.
	 * This is just the last name in the pathname's name sequence.
	 */
	public String getName();

}
