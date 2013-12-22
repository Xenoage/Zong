package com.xenoage.zong.io.midi.out;

import com.xenoage.zong.core.position.MP;

/**
 * This listener reports the progress of a Midi playback.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public interface PlaybackListener {

	public static final int timerRate = 50;


	/**
	 * This method is called whenever the playback reaches a new
	 * known {@link MP} (e.g. where a musical element starts or ends).
	 * @param mp  the {@link MP} musical position where the playback stopped
	 * @param ms  the position in milliseconds
	 */
	public void playbackAtMP(MP mp, long ms);

	/**
	 * This method is called about {@value #timerRate} times a second during playback.
	 * @param ms  the current position in milliseconds
	 */
	public void playbackAtMs(long ms);

	/**
	 * This method is called when the playback was (re)started.
	 */
	public void playbackStarted();

	/**
	 * This method is called when the playback was paused.
	 */
	public void playbackPaused();

	/**
	 * This method is called when the playback was stopped.
	 */
	public void playbackStopped();

	/**
	 * This method is called when the playback reached the end of the score.
	 */
	public void playbackAtEnd();

}
