package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.text.Text;
import com.xenoage.zong.musicxml.types.MxlFormattedText;

/**
 * Interface for classes, which read texts from MusicXML.
 * 
 * For example, there may be implementations which do or do not
 * support formatting information.
 * 
 * @author Andreas Wenger
 */
public interface TextReader {

	Text readText(MxlFormattedText mxlText);
	
}