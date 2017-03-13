package com.xenoage.zong.io;

import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.zong.core.Score;


/**
 * A list of common file formats.
 * 
 * @author Andreas Wenger
 */
public enum ScoreFileFormats {

	Midi(new FileFormat<>("Midi", "Midi", ".mid")),
	MP3(new FileFormat<>("MP3", "MPEG Audio Layer III", ".mp3")),
	MusicXML(new FileFormat<>("MusicXML", "MusicXML", ".mxl", ".xml")),
	OGG(new FileFormat<>("OGG", "Ogg Vorbis", ".ogg")),
	PDF(new FileFormat<>("PDF", "PDF", ".pdf")),
	PNG(new FileFormat<>("PNG", "PNG", ".png")),
	WAV(new FileFormat<>("WAV", "Waveform Audio", ".wav"));


	public final FileFormat<Score> format;


	private ScoreFileFormats(FileFormat<Score> format) {
		this.format = format;
	}

}
