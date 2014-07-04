package com.xenoage.zong.io;

import static com.xenoage.utils.pdlib.PVector.pvec;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.io.midi.out.MidiScoreFileOutput;
import com.xenoage.zong.io.mp3.out.MP3ScoreFileOutput;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInput;
import com.xenoage.zong.io.ogg.out.OGGScoreFileOutput;
import com.xenoage.zong.io.wav.out.WAVScoreFileOutput;


/**
 * This class contains a list of all formats which can be used
 * for loading or saving a document in the Zong! Player.
 * 
 * @author Andreas Wenger
 */
public class PlayerSupportedFormats
	extends SupportedFormats
{
	
	private static PlayerSupportedFormats instance = new PlayerSupportedFormats();
	
	
	private PlayerSupportedFormats()
	{
		super(getSupportedFormats());
	}
	
	
	static PVector<ScoreFileFormat> getSupportedFormats()
	{
		PVector<ScoreFileFormat> formats = pvec();
		//Midi (write only)
		formats = formats.plus(new ScoreFileFormat(
			FileFormats.Midi.info, null, new MidiScoreFileOutput()));
		//MP3 (write only)
		formats = formats.plus(new ScoreFileFormat(
			FileFormats.MP3.info, null, new MP3ScoreFileOutput()));
		//MusicXML (read only)
		formats = formats.plus(new ScoreFileFormat(
			FileFormats.MusicXML.info, new MusicXMLScoreFileInput(), null));
		//OGG (write only)
		formats = formats.plus(new ScoreFileFormat(
			FileFormats.OGG.info, null, new OGGScoreFileOutput()));
		//WAV (write only)
		formats = formats.plus(new ScoreFileFormat(
			FileFormats.WAV.info, null, new WAVScoreFileOutput()));
		return formats;
	}
	
	
	public static PlayerSupportedFormats getInstance()
	{
		if (instance == null)
			instance = new PlayerSupportedFormats();
		return instance;
	}
	
	
	@Override public ScoreFileFormat getReadDefaultFormat()
	{
		return getByID(FileFormats.MusicXML.info.id);
	}


	@Override public ScoreFileFormat getWriteDefaultFormat()
	{
		return getByID(FileFormats.Midi.info.id);
	}
	

}
