package com.xenoage.zong.musicxml.util;

import java.io.File;
import java.io.FilenameFilter;


/**
 * Filename filter for MusicXML files: .xml and .mxl
 * 
 * @author Andreas Wenger
 */
public class MusicXMLFilenameFilter
	implements FilenameFilter
{
	
	public static final MusicXMLFilenameFilter musicXMLFilenameFilter =
		new MusicXMLFilenameFilter();
	
	
  @Override public boolean accept(File dir, String name)
  {
  	String nameLC = name.toLowerCase();
    return nameLC.endsWith(".xml") || nameLC.endsWith(".xml");
  }

}
