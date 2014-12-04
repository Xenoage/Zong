package com.xenoage.zong.demos.simplegui;

import javax.sound.midi.MidiUnavailableException;

import com.xenoage.utils.error.Err;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.MidiScorePlayer;
import com.xenoage.zong.io.midi.out.PlaybackListener;

/**
 * MIDI playback controller.
 * 
 * @author Andreas Wenger
 */
public class Playback {
	
	private static MidiScorePlayer player = null;
	
	/**
	 * Opens the given score for MIDI playback.
	 */
	public static void openScore(Score score) {
		initPlayer();
		player.openScore(score);
	}
	
	private static void initPlayer() {
		if (player == null) {
			try {
				MidiScorePlayer.init();
				player = MidiScorePlayer.midiScorePlayer();
			} catch (MidiUnavailableException ex) {
				Err.handle(Report.error("MIDI not available"));
			}
		}
	}
	
	/**
	 * Starts the MIDI playback of the current score.
	 */
	public static void start() {
		if (player != null) {
			player.setMetronomeEnabled(true);
			player.start();
		}
	}
	
	/**
	 * Stops the MIDI playback.
	 */
	public static void stop() {
		if (player != null)
			player.stop();
	}
	
	/**
	 * Notifies the given object about playback events.
	 */
	public static void registerListener(PlaybackListener listener) {
		initPlayer();
		player.addPlaybackListener(listener);
	}

}
