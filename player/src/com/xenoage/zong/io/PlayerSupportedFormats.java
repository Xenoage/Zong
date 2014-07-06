package com.xenoage.zong.io;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.Getter;

import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.MidiScoreFileOutput;
import com.xenoage.zong.desktop.io.mp3.out.Mp3ScoreFileOutput;
import com.xenoage.zong.desktop.io.ogg.out.OggScoreFileOutput;
import com.xenoage.zong.desktop.io.wav.out.WavScoreFileOutput;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInput;

/**
 * This class contains a list of all formats which can be used
 * for loading or saving a document in the Zong! Player.
 * 
 * @author Andreas Wenger
 */
public class PlayerSupportedFormats
	extends SupportedFormats<Score> {

	@Getter private static PlayerSupportedFormats instance = new PlayerSupportedFormats();


	private PlayerSupportedFormats() {
		this.formats = getSupportedFormats();
	}

	static List<FileFormat<Score>> getSupportedFormats() {
		List<FileFormat<Score>> formats = alist();
		//Midi (write only)
		formats.add(ScoreFileFormats.Midi.format.withIO(null, new MidiScoreFileOutput()));
		//MP3 (write only)
		formats.add(ScoreFileFormats.MP3.format.withIO(null, new Mp3ScoreFileOutput()));
		//MusicXML (read only)
		formats.add(ScoreFileFormats.MusicXML.format.withIO(new MusicXmlScoreFileInput(), null));
		//OGG (write only)
		formats.add(ScoreFileFormats.OGG.format.withIO(null, new OggScoreFileOutput()));
		//WAV (write only)
		formats.add(ScoreFileFormats.MP3.format.withIO(null, new WavScoreFileOutput()));
		return formats;
	}

	@Override public FileFormat<Score> getReadDefaultFormat() {
		return getByID(ScoreFileFormats.MusicXML.format.getId());
	}

	@Override public FileFormat<Score> getWriteDefaultFormat() {
		return getByID(ScoreFileFormats.Midi.format.getId());
	}

}
