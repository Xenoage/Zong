package com.xenoage.zong.io.musicxml.in.util;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import java.util.*;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.musicxml.in.readers.Context;

/**
 * Unclosed beams, needed during MusicXML import.
 * 
 * Beams are distinguished by being in different voices and/or
 * the presence or absence of grace and cue elements.
 * 
 * Currently subdivisions in beams are not loaded from MusicXML,
 * but computed by the program itself. Thus, only beam elements with
 * number 1 (8th line) are read.
 * 
 * @author Andreas Wenger
 */
public class OpenBeams {
	
	//list index: category (see constants below)
	//map key: voice (may be any string; null is permitted)
	private List<Map<String, OpenBeam>> openBeams;
	
	private static final int catsCount = 4;
	private static final int catCue = 1 << 0;
	private static final int catGrace = 1 << 1;
	
	
	public OpenBeams() {
		openBeams = alist(catsCount);
		for (int i = 0; i < catsCount; i++)
			openBeams.add(new HashMap<String, OpenBeam>());
	}
	
	/**
	 * Begins a new beam with the given chord.
	 * If a beam was already open with this context, it is tried to be overwritten.
	 */
	public void beginBeam(Chord chord, String voice, Context context) {
		OpenBeam openBeam = getOpenBeam(voice, chord);
		if (openBeam != null) {
			context.reportError("Beginning a new beam, although there is still an open one");
			//create open beam, as far as it is known
			createBeam(context, openBeam);
		}
		openBeam = new OpenBeam();
		openBeam.addChord(chord);
		int category = getCategory(chord);
		openBeams.get(category).put(voice, openBeam);
	}
	
	/**
	 * Adds a chord to an existing beam.
	 */
	public void continueBeam(Chord chord, String voice, Context context) {
		OpenBeam openBeam = getOpenBeamTolerant(voice, chord);
		if (openBeam == null) {
			context.reportError("Can not continue beam which was not started");
			beginBeam(chord, voice, context); //begin new beam instead
		}
		else {
			openBeam.addChord(chord);
		}
	}
	
	/**
	 * Ends an existing beam with the given chord and creates the beam.
	 */
	public void endBeam(Chord chord, String voice, Context context) {
		OpenBeam openBeam = getOpenBeamTolerant(voice, chord);
		if (openBeam == null) {
			context.reportError("Can not end beam which was not started");
		}
		else {
			openBeam.addChord(chord);
			openBeams.get(getCategory(chord)).remove(voice);
			createBeam(context, openBeam);
		}
	}

	/**
	 * Creates a beam for the given beamed chords.
	 * Chords, that do not exist any more (when their {@link MP} is unknown)
	 * or which are not in the same measure are removed from the beam.
	 * The chords are sorted by beat. The chords may be mixed up beforehand
	 * because of wrong backup elements in the MusicXML score.
	 * Only beams with two or more notes are created.
	 */
	private void createBeam(Context context, OpenBeam openBeam) {
		List<Chord> beamedChords = openBeam.getChords();

		//remove missing chords
		for (int i : rangeReverse(beamedChords)) {
			if (beamedChords.get(i).getMP() == null)
				beamedChords.remove(i);
		}

		//remove chords, which are in other measures
		if (beamedChords.size() == 0)
			return;
		int measure = getFirst(beamedChords).getMP().measure;
		for (int i : rangeReverse(beamedChords)) {
			if (beamedChords.get(i).getMP().measure != measure)
				beamedChords.remove(i);
		}

		//sort by beat
		//ZONG-120: replace by lambda later
		Collections.sort(beamedChords, new Comparator<Chord>() {
			@Override public int compare(Chord c1, Chord c2) {
				return c1.getMP().beat.compareTo(c2.getMP().beat);
			}
		});

		//create beam
		if (beamedChords.size() > 1)
			context.writeBeam(beamedChords);
	}

	/**
	 * Gets the existing {@link OpenBeam} for the given voice and grace/cue status.
	 * If it does not exist, another open beam with or without grace or cue is searched.
	 * If it can still not be found, null is returned.
	 */
	private OpenBeam getOpenBeamTolerant(String voice, Chord chord) {
		//find perfect match
		OpenBeam openBeam = getOpenBeam(voice, chord);
		if (openBeam != null)
			return openBeam;
		//if not found, try to find an open beam with/without grace/cue
		for (int category : range(catsCount)) {
			openBeam = openBeams.get(category).get(voice);
			if (openBeam != null)
				return openBeam;
		}
		//still not found
		return null;
	}
	
	/**
	 * Gets the existing {@link OpenBeam} for the given voice and grace/cue status.
	 * If it does not exist, null is returned.
	 */
	private OpenBeam getOpenBeam(String voice, Chord chord) {
		int category = getCategory(chord);
		return openBeams.get(category).get(voice);
	}
	
	private int getCategory(Chord chord) {
		return (chord.isCue() ? catCue : 0) + (chord.isGrace() ? catGrace : 0);
	}

}
