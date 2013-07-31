package com.xenoage.zong.io;

import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.zong.core.Score;


/**
 * A list of common file formats.
 * 
 * @author Andreas Wenger
 */
public enum ScoreFileFormats {

	Midi(new FileFormat<Score>("Midi", "Midi", ".mid")),
	MP3(new FileFormat<Score>("MP3", "MPEG Audio Layer III", ".mp3")),
	MusicXML(new FileFormat<Score>("MusicXML", "MusicXML", ".mxl", ".xml")),
	OGG(new FileFormat<Score>("OGG", "Ogg Vorbis", ".ogg")),
	PDF(new FileFormat<Score>("PDF", "PDF", ".pdf")),
	PNG(new FileFormat<Score>("PNG", "PNG", ".png")),
	WAV(new FileFormat<Score>("WAV", "Waveform Audio", ".wav"));


	public final FileFormat<Score> format;


	private ScoreFileFormats(FileFormat<Score> format) {
		this.format = format;
	}

}
