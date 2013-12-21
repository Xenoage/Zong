package com.xenoage.zong.desktop.io.midi.out;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.xenoage.zong.io.midi.out.MidiSequenceWriter;

/**
 * Java SE implementation of a {@link MidiSequenceWriter}.
 * 
 * @author Andreas Wenger
 */
public class JseMidiSequenceWriter
	extends MidiSequenceWriter {

	private Sequence sequence = null;
	private Track[] tracks;


	@Override public void init(int tracksCount, int resolutionPpq) {
		//create sequence
		try {
			sequence = new Sequence(Sequence.PPQ, resolutionPpq);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(); //TODO
		}
		//add tracks
		for (int i = 0; i < tracksCount; i++) {
			tracks[i] = sequence.createTrack();
		}
	}

	@Override public void writeProgramChange(int track, int channel, long tick, int program) {
		try {
			ShortMessage prgMsg = new ShortMessage();
			prgMsg.setMessage(ShortMessage.PROGRAM_CHANGE, channel, program, 0);
			tracks[track].add(new MidiEvent(prgMsg, tick));
		} catch (InvalidMidiDataException e) {
			//ignore - TODO
		}
	}

	@Override public void writeVolumeChange(int track, int channel, long tick, float volume) {
		try {
			ShortMessage ctrlVolume = new ShortMessage();
			ctrlVolume.setMessage(ShortMessage.CONTROL_CHANGE, channel, controllerVolume,
				(int) (127 * volume));
			tracks[track].add(new MidiEvent(ctrlVolume, tick));
		} catch (InvalidMidiDataException e) {
			//ignore - TODO
		}
	}
	
	@Override public void writePanChange(int track, int channel, long tick, float pan) {
		try {
			//TODO: does not work, no panning can be heared using gervill
			ShortMessage ctrlPan = new ShortMessage();
			ctrlPan.setMessage(ShortMessage.CONTROL_CHANGE, channel, controllerPan,
				(int) (64 + (63 * pan)));
			tracks[track].add(new MidiEvent(ctrlPan, tick));
		} catch (InvalidMidiDataException e) {
			//ignore - TODO
		}
	}
	
}
