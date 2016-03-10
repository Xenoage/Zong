package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.io.musicxml.in.util.ErrorHandling;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * General settings for the MusicXML reader.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class ReaderSettings {

	private final ErrorHandling errorHandling;
	private final TextReader textReader = new FormattedTextReader();

}
