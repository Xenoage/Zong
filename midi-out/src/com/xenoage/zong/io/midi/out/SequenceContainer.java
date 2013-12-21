package com.xenoage.zong.io.midi.out;

import javax.sound.midi.Sequence;

import com.xenoage.utils.pdlib.IVector;


/**
 * Additional information about a Midi sequence.
 * 
 * OBSOLETE
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class SequenceContainer
{

	public final Sequence sequence;
	/** Track number of the metronome, or null if there is no metronome track */
	public final Integer metronomeBeatTrackNumber;
	public final IVector<MidiTime> timePool;
	public final IVector<Long> measureStartTicks;
	public final IVector<Integer> staffTracks;
	
	
	public SequenceContainer(Sequence sequence, Integer metronomeBeatTrackNumber,
		IVector<MidiTime> timePool, IVector<Long> measureStartTicks,
		IVector<Integer> staffTracks)
	{
		this.sequence = sequence;
		this.metronomeBeatTrackNumber = metronomeBeatTrackNumber;
		this.timePool = timePool;
		this.measureStartTicks = measureStartTicks;
		this.staffTracks = staffTracks;
	}

}
