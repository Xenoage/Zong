package com.xenoage.zong.io;

import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.MidiScoreDocFileOutput;
import com.xenoage.zong.desktop.io.mp3.out.Mp3ScoreDocFileOutput;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.desktop.io.ogg.out.OggScoreDocFileOutput;
import com.xenoage.zong.desktop.io.pdf.out.PdfScoreDocFileOutput;
import com.xenoage.zong.desktop.io.png.out.PngScoreDocFileOutput;
import com.xenoage.zong.desktop.io.wav.out.WavScoreDocFileOutput;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This class contains a list of all formats which can be used
 * for loading or saving a {@link Score} in the Zong! Converter.
 * 
 * @author Andreas Wenger
 */
public class ConverterSupportedFormats
	extends SupportedFormats<ScoreDoc> {

	public ConverterSupportedFormats() {
		//Midi (write only)
		getFormats().add(new FileFormat<>("mid", "MIDI", ".mid", new String[0],
			null, new MidiScoreDocFileOutput()));
		//MP3 (write only)
		getFormats().add(new FileFormat<>("mp3", "MP3", ".mp3", new String[0],
			null, new Mp3ScoreDocFileOutput()));
		//MusicXML (read only)
		getFormats().add(new FileFormat<>("mxl", "MusicXML", ".mxl", new String[]{".xml"},
			new MusicXmlScoreDocFileInput(), null));
		//OGG (write only)
		getFormats().add(new FileFormat<>("ogg", "OGG Vorbis", ".ogg", new String[0],
			null, new OggScoreDocFileOutput()));
		//PDF (write only)
		getFormats().add(new FileFormat<>("pdf", "PDF", ".pdf", new String[0],
			null, new PdfScoreDocFileOutput()));
		//PNG (write only)
		getFormats().add(new FileFormat<>("png", "PNG", ".png", new String[0],
			null, new PngScoreDocFileOutput()));
		//WAV (write only)
		getFormats().add(new FileFormat<>("wav", "WAV", ".wav", new String[0],
			null, new WavScoreDocFileOutput()));
	}

	@Override public FileFormat<ScoreDoc> getReadDefaultFormat() {
		return getByID("mxl");
	}

	@Override public FileFormat<ScoreDoc> getWriteDefaultFormat() {
		return getByID("pdf");
	}

}
