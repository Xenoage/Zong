package com.xenoage.utils.jse.files;

/**
 * Interface for all classes that can be notified when
 * the list of {@link RecentFiles} changed.
 * 
 * @author Andreas Wenger
 */
public interface RecentFilesListener {

	/**
	 * This method is called when the list of {@link RecentFiles}
	 * has been changed.
	 */
	public void recentFilesChanged();

}
