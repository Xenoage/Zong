package com.xenoage.zong.android.io.midi.out;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.xenoage.zong.io.midi.out.MidiSequenceWriter;


/**
 * Android implementation of a {@link MidiSequenceWriter}.
 * 
 * @author Andreas Wenger
 */
public class AndroidMidiSequenceWriter
	extends MidiSequenceWriter<MidiFile> {

	private int resolutionPpq;
	private ArrayList<MidiTrack> tracks;


	@Override public void init(int tracksCount, int resolutionPpq) {
		this.resolutionPpq = resolutionPpq;
		//add tracks
		tracks = alist(tracksCount);
		for (int i = 0; i < tracksCount; i++) {
			tracks.add(new MidiTrack());
		}
	}
	
	@Override public void writeNote(int track, int channel, long tick, int note, boolean on, int velocity) {
		MidiEvent event;
		if (on)
			event = new NoteOn(tick, channel, note, velocity);
		else
			event = new NoteOff(tick, channel, note, velocity);
		tracks.get(track).insertEvent(event);
	}
	
	@Override public void writeShortMessage(int track, int channel, long tick, int command, int data1, int data2) {
		ShortMessageEvent event = new ShortMessageEvent(tick, command, channel, data1, data2);
		tracks.get(track).insertEvent(event);
	}
	
	@Override public void writeMetaMessage(int track, long tick, int type, byte[] data) {
		MetaMessage event = new MetaMessage(tick, type, data);
		//tracks.get(track).insertEvent(event);
	}

	@Override public long getLength() {
		return getSequence().getLengthInTicks();
	}
	
	@Override public long tickToMicrosecond(long tick) {
		return 0; //unsupported
	}

	@Override protected MidiFile getSequence() {
		return new MidiFile(resolutionPpq, tracks);
	}

}
