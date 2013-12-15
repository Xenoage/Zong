package com.xenoage.zong.musicxml.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filename filter for plaintext MusicXML files: .xml
 * 
 * @author Andreas Wenger
 */
public class PlainMusicXMLFilenameFilter
	implements FilenameFilter {

	public static final PlainMusicXMLFilenameFilter plainMusicXMLFilenameFilter = new PlainMusicXMLFilenameFilter();


	@Override public boolean accept(File dir, String name) {
		String nameLC = name.toLowerCase();
		return nameLC.endsWith(".xml");
	}

}
