package com.xenoage.zong.io.musicxml.in.readers;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * General settings for the {@link MusicReader}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class MusicReaderSettings {

	private final boolean ignoringErrors;
	private final TextReader textReader = new FormattedTextReader();

}
