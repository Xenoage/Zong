package com.xenoage.zong.io.musicxml.in.readers;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * General settings for the MusicXML reader.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class ReaderSettings {

	private final boolean ignoringErrors;
	private final TextReader textReader = new FormattedTextReader();

}
