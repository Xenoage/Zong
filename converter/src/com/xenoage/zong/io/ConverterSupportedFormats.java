package com.xenoage.zong.io;

import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.MidiScoreDocFileOutput;
import com.xenoage.zong.desktop.io.mp3.out.Mp3ScoreDocFileOutput;
import com.xenoage.zong.desktop.io.ogg.out.OggScoreFileOutput;
import com.xenoage.zong.desktop.io.pdf.out.PdfScoreDocFileOutput;
import com.xenoage.zong.desktop.io.png.out.PngScoreDocFileOutput;
import com.xenoage.zong.desktop.io.wav.out.WavScoreFileOutput;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This class contains a list of all formats which can be used
 * for loading or saving a {@link Score} in the Zong! Converter.
 * 
 * @author Andreas Wenger
 */
public class ConverterSupportedFormats
	extends SupportedFormats<ScoreDoc> {

	private ConverterSupportedFormats() {
		//Midi (write only)
		formats.add(new FileFormat<ScoreDoc>("mid", "MIDI", ".mid", new String[0],
			null, new MidiScoreDocFileOutput()));
		//MP3 (write only)
		formats.add(new FileFormat<ScoreDoc>("mp3", "MP3", ".mp3", new String[0],
			null, new Mp3ScoreDocFileOutput()));
		//MusicXML (read only)
		formats.add(new FileFormat<ScoreDoc>("mxl", "MusicXML", ".mxl", new String[]{".xml"},
			null, new MusicXMLScoreDocFileInput(), null));
		//OGG (write only)
		formats = formats
			.plus(new ScoreFileFormat(FileFormats.OGG.info, null, new OggScoreFileOutput()));
		//PDF (write only)
		formats = formats.plus(new ScoreFileFormat(FileFormats.PDF.info, null,
			new PdfScoreDocFileOutput()));
		//PNG (write only)
		formats = formats.plus(new ScoreFileFormat(FileFormats.PNG.info, null,
			new PngScoreDocFileOutput()));
		//WAV (write only)
		formats = formats
			.plus(new ScoreFileFormat(FileFormats.WAV.info, null, new WavScoreFileOutput()));
		return formats;
	}

	public static ConverterScoreFormats getInstance() {
		if (instance == null)
			instance = new ConverterScoreFormats();
		return instance;
	}

	@Override public ScoreFileFormat getReadDefaultFormat() {
		return getByID(FileFormats.MusicXML.info.id);
	}

	@Override public ScoreFileFormat getWriteDefaultFormat() {
		return getByID(FileFormats.PDF.info.id);
	}

}
