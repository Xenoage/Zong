package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.text.Text;
import com.xenoage.zong.core.text.UnformattedText;
import com.xenoage.zong.musicxml.types.MxlFormattedText;

/**
 * This MusicXML text reader ignores formatting information
 * and reads just the text content.
 * 
 * @author Andreas Wenger
 */
public class UnformattedTextReader
	implements TextReader {

	@Override public Text readText(MxlFormattedText mxlText) {
		return new UnformattedText(mxlText.getValue().trim());
	}

}
