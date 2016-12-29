package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.musicxml.in.readers.Context;

import java.util.Map;

import static com.xenoage.utils.collections.CollectionUtils.map;

/**
 * Unclosed unnumbered tied elements, needed during MusicXML import.
 * 
 * The elements can be distinguished by number, however this
 * is rarely done, and when, this is handled by registerSlur method
 * in the {@link Context} class.
 * 
 * Note pitches will usually suffice to distinguish tieds.
 * However, we saw MusicXML files where a tied note in the middle
 * of a long tie (which means that it has both a start and a stop
 * element) provided the start element for the following tied first
 * and then a stop element for the preceding tied.
 * To support this case, we remember up to two tieds. When a tied
 * is closed at the same musical position where the last one was
 * opened, the earlier tied is used instead.
 *  
 * @author Andreas Wenger
 */
public class OpenUnnumberedTieds {

	private Map<Pitch, OpenSlur> openTieds = map();
	private Map<Pitch, OpenSlur> openEarlierTieds = map();
	
	/**
	 * Starts a tie.
	 */
	public void startTied(SlurWaypoint wp, VSide side) {
		Chord chord = wp.getChord();
		Pitch pitch = chord.getNotes().get(wp.getNoteIndex()).getPitch();
		//already a tied open for this pitch? then remember it, too
		if (openTieds.get(pitch) != null)
			openEarlierTieds.put(pitch, openTieds.get(pitch));
		//add new tied
		OpenSlur openTied = new OpenSlur();
		openTied.type = SlurType.Tie;
		openTied.start = new OpenSlur.Waypoint(wp, side);
		openTieds.put(pitch, openTied);
	}
	
	/**
	 * Ends an existing tie and returns the {@link OpenSlur}.
	 * When the tie does not exist, null is returned.
	 */
	public OpenSlur stopTied(SlurWaypoint wp, VSide side, Context context) {
		Chord chord = wp.getChord();
		Pitch pitch = chord.getNotes().get(wp.getNoteIndex()).getPitch();
		//get tied for this pitch
		OpenSlur openTied = openTieds.remove(pitch);
		if (openTied == null) {
			context.reportError("Can not stop non-existing tied");
			return null;
		}
		//does start chord exist? could have been overwritten by backup element
		MP startMp = openTied.start.wp.getChord().getMP();
		if (startMp == null) {
			context.reportError("Tied can not be closed; start chord does not exist");
			return null;
		}
		//is tied closed at the same musical position where it was opened?
		//then close the earlier open tied instead, if there is one
		if (startMp.equals(context.getMp())) {
			openTieds.put(pitch, openTied); //remember last tied
			openTied = openEarlierTieds.remove(pitch); //use earlier tied instead
			if (openTied == null) {
				context.reportError("Tied can not be stopped on starting position, " +
					"and there is no earlier tied");
				return null;
			}
		}
		openTied.stop = new OpenSlur.Waypoint(wp, side);
		return openTied;
	}
	
}
