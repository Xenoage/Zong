package com.xenoage.zong;

import javax.swing.JOptionPane;


/**
 * Product and version information.
 * 
 * @author Andreas Wenger
 */
public class Zong
{

	//the version of this program as an integer number
	public static final int PROJECT_ITERATION = 66;
	
	//general information on this program
  public static final String PROJECT_FAMILY_NAME = "Zong!";
  public static final String PROJECT_VERSION = "0.1-a";
  public static final String PROJECT_ITERATION_NAME = "Rattle";
  
  //package path
  public static final String PACKAGE = "com.xenoage.zong";
  
  //name in filesystem
  public static final String FILENAME = "zong";
  
  //copyright information
  public static final String COPYRIGHT =
  	"Copyright © 2006-2014 by Andreas Wenger, Uli Teschemacher, Xenoage Software";
  
  //other information
  public static final String WEBSITE = "http://www.zong-music.com";
  public static final String EMAIL_ERROR_REPORTS = "support@zong-music.com";
  public static final String BUGTRACKER = "http://xenoage.atlassian.net";
  
  //last version, that was "complete" regarding its functions, and not a
  //current snapshot or "work in progress"
  public static final String PROJECT_ITERATION_LAST_WORKING = "54";
  
  
  /**
   * Gets the name of the program as a String,
   * using the given "first" name of the project (like "Viewer" or "Editor").
   */
  public static String getName(String firstName)
  {
  	return PROJECT_FAMILY_NAME + " " + firstName;
  }
  
  
  /**
   * Gets the name and version of the program as a String,
   * using the given "first" name of the project (like "Viewer" or "Editor").
   */
  public static String getNameAndVersion(String firstName)
  {
  	return PROJECT_FAMILY_NAME + " " + firstName + " " + PROJECT_VERSION + "." +
  		PROJECT_ITERATION + " ALPHA";
  }
  
  
  /**
   * Shows a warning message using Swing, showing that this version is not usable but
   * "work in progress".
   */
  public static void showWorkInProgressWarning()
  {
  	JOptionPane.showMessageDialog(null, "Warning: This version of " +
  		PROJECT_FAMILY_NAME + " is \"work in progress\" any may not work " +
  		"as expected.\n" +
  		"If you need a working program, use version " +
  		Zong.PROJECT_VERSION + "." + Zong.PROJECT_ITERATION_LAST_WORKING,
  		PROJECT_FAMILY_NAME + " " + PROJECT_VERSION + "." + PROJECT_ITERATION,
  		JOptionPane.WARNING_MESSAGE);
  }
  


}
