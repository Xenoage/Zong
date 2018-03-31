package com.xenoage.utils.jse;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.io.File;
import java.util.UUID;

/**
 * The {@link AppWatcher} is a class that can tell if another
 * instance of a given application is running at the moment.
 * 
 * Each application that should
 * be observed must have a unique ID per instance and
 * call the alive-method within a given time interval
 * When it is closed, it has to call the closed-method.
 * 
 * This is realized by files. For each instance a file
 * is created (data/running/{appid}/{instanceid}) and
 * updated (touched) on each call of the alive-method.
 * When the dead-method is called, the file is deleted.
 * The folder data/running/ must exist before!
 * 
 * When the {@link #isAppAlive(String)}-method is called, it looks in the
 * (data/running/{appid}/-folder if there are still files
 * younger than the given time interval. If yes, there is still
 * running an instance of this application.
 * 
 * @author Andreas Wenger
 */
public class AppWatcher {

	private static AppWatcher instance = null;

	private int timeInterval = 60; //call the alive-method each 60 seconds
	private int timeObsolete = 80; //instance is obsolete when time is older than now minus this value


	public static AppWatcher getInstance() {
		if (instance == null) {
			instance = new AppWatcher();
		}
		return instance;
	}

	private AppWatcher() {
	}

	/**
	 * Marks the given app instance as alive.
	 */
	public void alive(String appID, UUID instanceID) {
		//create the directory for the application, if not there
		File appDir = new File("data/running/" + appID);
		if (!appDir.exists()) {
			if (!appDir.mkdirs()) {
				log(warning("Could not create app folder: " + appDir));
				return;
			}
		}
		//update the instance file
		File instanceFile = new File(appDir, instanceID.toString());
		if (instanceFile.exists()) {
			if (!instanceFile.setLastModified(System.currentTimeMillis())) {
				log(warning("Could not touch file: " + instanceFile));
			}
		}
		else {
			try {
				instanceFile.createNewFile();
			} catch (Exception ex) {
				log(warning("Could not create file: " + instanceFile));
			}
		}
	}

	/**
	 * Marks the given app instance as closed.
	 */
	public void dead(String appID, UUID instanceID) {
		//delete our file
		File instanceFile = new File("data/running/" + appID + "/" + instanceID.toString());
		if (!instanceFile.exists()) {
			log(warning("Could not mark as dead, since file doesn't exist: " + instanceFile));
		}
		else {
			if (!instanceFile.delete()) {
				log(warning("Could not mark as dead, since could not delete file: " + instanceFile));
			}
		}
		//also delete all other obsolete files
		deleteObsoleteFiles(appID);
	}

	/**
	 * Returns true, if there is still an instance of the given application.
	 */
	public boolean isAppAlive(String appID) {
		return isAppAlive(appID, null);
	}

	/**
	 * Returns true, if there is still an instance of the given application,
	 * except the one with the given instance ID.
	 */
	public boolean isAppAlive(String appID, UUID exceptThis) {
		//delete obsolete files
		deleteObsoleteFiles(appID);
		//search for remaining files
		File appDir = new File("data/running/" + appID);
		if (appDir.exists()) {
			File files[] = appDir.listFiles();
			String exceptThisName = null;
			if (exceptThis != null) {
				exceptThisName = exceptThis.toString();
			}
			for (File file : files) {
				if (!file.getName().startsWith(".")) //ignore files starting with "." (may be a subversion file)
				{
					if (exceptThis == null || !file.getName().equals(exceptThisName)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets the time interval in seconds after which the app instances
	 * must call the alive-method.
	 */
	public int getTimeInterval() {
		return timeInterval;
	}

	/**
	 * Sets the time interval in seconds after which the app instances
	 * must call the alive-method.
	 */
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * Sets the time interval in seconds after when an app instance
	 * is considered as dead. This should be the
	 * time interval of the alive-method plus an additional
	 * margin, like additional 20 seconds.
	 * 
	 * Do not set values smaller than 2, because most systems don't
	 * save the exact time, but only seconds.
	 */
	public void setTimeObsolete(int timeObsolete) {
		this.timeObsolete = timeObsolete;
	}

	/**
	 * Delete all out-dated files of the given application.
	 */
	private void deleteObsoleteFiles(String appID) {
		File appDir = new File("data/running/" + appID);
		if (appDir.exists()) {
			File files[] = appDir.listFiles();
			for (File file : files) {
				//check if this file is obsolete
				long fileTime = file.lastModified();
				if (System.currentTimeMillis() - fileTime > timeObsolete * 1000) {
					//file is obsolete. delete it.
					file.delete();
				}
			}
		}
	}

}
