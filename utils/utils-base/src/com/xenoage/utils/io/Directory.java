package com.xenoage.utils.io;

import java.util.List;

/**
 * System independent interface for directories.
 * 
 * For example, there may be implementations for Java SE, Android or GWT.
 * 
 * @author Andreas Wenger
 */
public interface Directory
	extends FilesystemItem {
	
	public List<FilesystemItem> getChildren();

}
