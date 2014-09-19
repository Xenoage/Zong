package com.xenoage.zong;

import javax.swing.JOptionPane;

/**
 * Product and version information.
 * 
 * @author Andreas Wenger
 */
public class Zong {

	//the version of this program as an integer number
	public static final int projectIteration = 68;

	//general information on this program
	public static final String projectFamilyName = "Zong!";
	public static final String projectVersion = "0.1-a";
	public static final String projectVersionLong = projectVersion + "." + projectIteration;
	public static final String projectIterationName = "Flute";

	//package path
	public static final String projectPackage = "com.xenoage.zong";

	//name in filesystem
	public static final String filename = "zong";

	//copyright information
	public static final String copyright = "Copyright © 2006-2014 by Xenoage Software";

	//other information
	public static final String website = "http://www.zong-music.com";
	public static final String emailErrorReports = "support@zong-music.com";
	public static final String bugtracker = "http://xenoage.atlassian.net";
	public static final String blog = "http://blog.zong-music.com";

	//last version, that was "complete" regarding its functions, and not a
	//current snapshot or "work in progress"
	public static final String projectIterationLastWorking = "54";


	/**
	 * Gets the name of the program as a String,
	 * using the given "first" name of the project (like "Viewer" or "Editor").
	 */
	public static String getName(String firstName) {
		return projectFamilyName + " " + firstName;
	}

	/**
	 * Gets the name and version of the program as a String,
	 * using the given "first" name of the project (like "Viewer" or "Editor").
	 */
	public static String getNameAndVersion(String firstName) {
		return projectFamilyName + " " + firstName + " " + projectVersionLong;
	}

	/**
	 * Shows a warning message using Swing, showing that this version is not usable but
	 * "work in progress".
	 */
	public static void showWorkInProgressWarning() {
		JOptionPane.showMessageDialog(null, "Warning: This version of " + projectFamilyName +
			" is \"work in progress\" any may not work " + "as expected.\n" +
			"If you need a working program, use version " + Zong.projectVersion + "." +
			Zong.projectIterationLastWorking, projectFamilyName + " " + projectVersionLong,
			JOptionPane.WARNING_MESSAGE);
	}

}
