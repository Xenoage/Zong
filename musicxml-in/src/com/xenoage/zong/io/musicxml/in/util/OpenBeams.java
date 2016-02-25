package com.xenoage.zong.io.musicxml.in.util;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xenoage.zong.core.music.chord.Chord;
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
	 * If a beam was already open with this context, it is overwritten,
	 * but a warning is logged.
	 */
	public void beginBeam(Chord chord, String voice, Context context) {
		OpenBeam openBeam = getOpenBeam(voice, chord);
		if (openBeam != null)
			log(warning("Beginning a new beam, although there is still an open one at " +
				context.getMp()));
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
			log(warning("Can not continue beam which was not started; starting a new one at " +
				context.getMp()));
			beginBeam(chord, voice, context);
		}
		else {
			openBeam.addChord(chord);
		}
	}
	
	/**
	 * Ends an existing beam with the given chord and returnes the beamed chords.
	 * In case of an error, null is returned.
	 */
	public List<Chord> endBeam(Chord chord, String voice, Context context) {
		OpenBeam openBeam = getOpenBeamTolerant(voice, chord);
		if (openBeam == null) {
			log(warning("Can not end beam which was not started; ignoring beam ending at " +
				context.getMp()));
			return null;
		}
		openBeam.addChord(chord);
		openBeams.get(getCategory(chord)).remove(voice);
		return openBeam.getChords();
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
