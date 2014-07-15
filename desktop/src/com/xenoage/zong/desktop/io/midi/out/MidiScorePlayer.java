package com.xenoage.zong.desktop.io.midi.out;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.util.List;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

import com.xenoage.utils.jse.collections.WeakList;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.midi.out.MidiConverter;
import com.xenoage.zong.io.midi.out.MidiEvents;
import com.xenoage.zong.io.midi.out.MidiSequence;
import com.xenoage.zong.io.midi.out.MidiSettings;
import com.xenoage.zong.io.midi.out.MidiTime;
import com.xenoage.zong.io.midi.out.PlaybackListener;

/**
 * This class offers the interface for MIDI playback in
 * the program to play a given {@link Score}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MidiScorePlayer
	implements ControllerEventListener {

	private static MidiScorePlayer instance = null;

	private MidiSequence<Sequence> sequence = null;
	private WeakList<PlaybackListener> listeners = new WeakList<PlaybackListener>();
	private boolean metronomeEnabled;
	private float volume = new MidiSettings().getDefaultVolume(); //TODO
	private int currentPosition;
	private TimeThread timeThread = new TimeThread();


	/**
	 * Gets the single instance of this class.
	 */
	public static MidiScorePlayer midiScorePlayer() {
		if (instance == null)
			throw new IllegalStateException(MidiScorePlayer.class.getName() + " not initialized");
		return instance;
	}

	public static void init()
		throws MidiUnavailableException {
		if (instance == null)
			instance = new MidiScorePlayer();
	}

	private MidiScorePlayer() {
		setVolume(volume);
		SynthManager.removeAllControllerEventListeners();

		//controller events to listen for (see MidiEvents doc)
		SynthManager.addControllerEventListener(this, new int[]{ MidiEvents.eventPlaybackControl });
		SynthManager.addControllerEventListener(this, new int[]{ MidiEvents.eventPlaybackEnd } );
	}

	/**
	 * Opens the given {@link Score} for playback.
	 */
	public void openScore(Score score) {
		stop();
		this.sequence = MidiConverter.convertToSequence(
			score, true, true, new JseMidiSequenceWriter());
		try {
			SynthManager.getSequencer().setSequence(sequence.getSequence());
		} catch (InvalidMidiDataException ex) {
			log(warning(ex));
		}
		applyVolume();
	}

	/**
	 * Registers the given {@link PlaybackListener} which will be
	 * informed about playback events like the current position.
	 * This class stores only a weak reference of the listener, so
	 * removing the listener is optional.
	 */
	public void addPlaybackListener(PlaybackListener listener) {
		listeners.add(listener);
	}

	/**
	 * Unregisters the given {@link PlaybackListener}. 
	 */
	public void removePlaybackListener(PlaybackListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Changes the position of the playback cursor to the given
	 * time in microseconds.
	 */
	public void setMicrosecondPosition(long ms) {
		SynthManager.getSequencer().setMicrosecondPosition(ms);
		currentPosition = 0;
	}

	/**
	 * Changes the position of the playback cursor to the given
	 * {@link BMP}.
	 */
	public void setMP(MP bmp) {
		long tickPosition = calculateTickFromMP(bmp, sequence.getMeasureStartTicks(),
			sequence.getSequence().getResolution());
		SynthManager.getSequencer().setTickPosition(tickPosition);
		currentPosition = 0; //as we don't know the real position, we set it 0, because the playback will automatically jump to the correct position.
	}

	/**
	 * Starts playback at the current position.
	 */
	public void start() {
		Sequencer sequencer = SynthManager.getSequencer();
		if (sequencer.getSequence() != null) {
			sequencer.start();
			timeThread.stopTimer();
			timeThread = new TimeThread();
			timeThread.start();
			for (PlaybackListener listener : listeners.getAll()) {
				listener.playbackStarted();
			}
			applyVolume();
		}
	}

	/**
	 * Stops the playback without resetting the
	 * current position.
	 */
	public void pause() {
		Sequencer sequencer = SynthManager.getSequencer();
		if (sequencer.isRunning()) {
			sequencer.stop();
			timeThread.stopTimer();
			for (PlaybackListener listener : listeners.getAll()) {
				listener.playbackPaused();
			}
		}
	}

	/**
	 * Stops the playback and sets the cursor to the start position.
	 */
	public void stop() {
		Sequencer sequencer = SynthManager.getSequencer();
		if (sequencer.isRunning()) {
			sequencer.stop();
			timeThread.stopTimer();
		}
		setMicrosecondPosition(0);
		currentPosition = 0;
		for (PlaybackListener listener : listeners.getAll()) {
			listener.playbackStopped();
		}
	}

	public boolean getMetronomeEnabled() {
		return metronomeEnabled;
	}

	public void setMetronomeEnabled(boolean metronomeEnabled) {
		this.metronomeEnabled = metronomeEnabled;
		Integer metronomeTrack = sequence.getMetronomeTrack();
		if (metronomeTrack != null)
			SynthManager.getSequencer().setTrackMute(metronomeTrack, !metronomeEnabled);
	}

	private long calculateTickFromMP(MP pos, List<Long> measureTicks, int resolution) {
		if (pos == null) {
			return 0;
		}
		else {
			return measureTicks.get(pos.measure) +
				MidiConverter.calculateTickFromFraction(pos.beat, resolution);
		}
	}

	/**
	 * This method catches the ControllerChangedEvent from the sequencer.
	 * For BMP-specific events, the method decides, which {@link BMP} is the
	 * right one and notifies the listener.
	 */
	@Override public void controlChange(ShortMessage message) {
		List<MidiTime> timePool = sequence.getTimePool();
		if (message.getData1() == MidiEvents.eventPlaybackControl) {
			//calls the listener with the most actual tick
			long currentTick = SynthManager.getSequencer().getTickPosition();
			//if playback is ahead: return nothing
			if (timePool.get(0).tick > currentTick) {
				return;
			}
			//if the program hung up but the player continued, there programm would always be to late.
			//So the algorithm deletes all aruments before the current Element.
			while (timePool.get(currentPosition + 1).tick <= currentTick)
				currentPosition++;
			MP pos = timePool.get(currentPosition).mp;
			for (PlaybackListener listener : listeners.getAll()) {
				listener.playbackAtMP(pos, SynthManager.getSequencer().getMicrosecondPosition() / 1000L);
			}
		}
		else if (message.getData1() == MidiEvents.eventPlaybackEnd) {
			stop(); //stop to really ensure the end
			for (PlaybackListener listener : listeners.getAll()) {
				listener.playbackAtEnd();
			}
		}
	}

	/**
	 * Gets the volume, which is a value between 0 (silent) and 1 (loud).
	 */
	public float getVolume() {
		return volume;
	}

	/**
	 * Sets the volume.
	 * @param volume  value between 0 (silent) and 1 (loud)
	 */
	public void setVolume(float volume) {
		this.volume = volume;
		applyVolume();
	}

	private void applyVolume() {
		MidiChannel[] channels = SynthManager.getSynthesizer().getChannels();
		int max = 127; //according to MIDI standard
		for (int i = 0; i < channels.length; i++) {
			channels[i].controlChange(7, Math.round(volume * max));
		}
	}

	/**
	 * Returns true, if the playback cursor is at the end of the
	 * score, otherwise false.
	 */
	public boolean isPlaybackFinished() {
		Sequencer sequencer = SynthManager.getSequencer();
		return sequencer.getMicrosecondPosition() >= sequencer.getMicrosecondLength();
	}

	/**
	 * Gets the length of the current sequence in microseconds,
	 * or 0 if no score is loaded.
	 */
	public long getMicrosecondLength() {
		if (sequence == null)
			return 0;
		return sequence.getSequence().getMicrosecondLength();
	}

	/**
	 * Gets the current position within the sequence in microseconds,
	 * or 0 if no score is loaded.
	 */
	public long getMicrosecondPosition() {
		if (sequence == null)
			return 0;
		return SynthManager.getSequencer().getMicrosecondPosition();
	}

	public Sequence getSequence() {
		if (sequence != null)
			return sequence.getSequence();
		else
			return null;
	}


	private class TimeThread
		extends Thread {

		private boolean stop = false;
		
		public TimeThread() {
			setDaemon(true);
		}

		@Override public void run() {
			try {
				while (!stop) {
					long ms = SynthManager.getSequencer().getMicrosecondPosition() / 1000;
					for (PlaybackListener listener : listeners.getAll()) {
						listener.playbackAtMs(ms);
					}
					Thread.sleep(1000 / PlaybackListener.timerRate);
				}
			} catch (InterruptedException e) {
			}
		}

		public void stopTimer() {
			stop = true;
		}
	}

}
