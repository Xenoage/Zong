package com.xenoage.zong.mobile.android;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.xenoage.zong.core.Score;


/**
 * Midi playback of scores.
 * 
 * @author Andreas Wenger
 */
public class MidiPlayer
{
	
	private MediaPlayer mediaPlayer;
	
	
	/**
	 * Opens the given {@link Score}.
	 * An Android context must be given, that is needed to determine the directory
	 * for the temporary MIDI file and to create the media player.
	 */
	public void open(Score score, Context context)
	{
		try {
			File outputFile = File.createTempFile("zong", "mid", context.getCacheDir());
			
			//GOON
			//MidiFile midi = MidiConverterAndroid.convertToMidiFile(score, false, false);
			//midi.writeToFile(outputFile);
			
			stop();
			mediaPlayer = MediaPlayer.create(context, Uri.fromFile(outputFile));
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Starts playback, if a file is loaded.
	 */
	public void start()
	{
		if (mediaPlayer != null) {
			//TODO: needs prepare() after stopping!
			/* try {
				mediaPlayer.prepare();
			} catch (IOException e) {
			} */
			mediaPlayer.start();
		}
	}
	
	
	/**
	 * Pauses playback, if a file is loaded.
	 */
	public void pause()
	{
		if (mediaPlayer != null)
			mediaPlayer.pause();
	}
	
	
	/**
	 * Stops playback, if a file is loaded.
	 */
	public void stop()
	{
		if (mediaPlayer != null)
			mediaPlayer.stop();
	}
	

}
