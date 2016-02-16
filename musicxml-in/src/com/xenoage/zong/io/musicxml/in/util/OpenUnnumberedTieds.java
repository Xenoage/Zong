package com.xenoage.zong.io.musicxml.in.util;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.io.musicxml.in.readers.Context;

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
 * To support this case, we use a queue of tieds for each pitch.
 * This ensures that tieds created earlier will be closed earlier. 
 * 
 * @author Andreas Wenger
 */
public class OpenUnnumberedTieds {

	private Map<Pitch, Queue<OpenSlur>> openTieds = map();
	
	/**
	 * Starts a tie.
	 */
	public void startTied(SlurWaypoint wp, VSide side) {
		Chord chord = wp.getChord();
		Pitch pitch = chord.getNotes().get(wp.getNoteIndex()).getPitch();
		//get stack for this pitch
		Queue<OpenSlur> pitchTieds = openTieds.get(pitch);
		if (pitchTieds == null) {
			pitchTieds = new LinkedList<OpenSlur>();
			openTieds.put(pitch, pitchTieds);
		}
		//add new tied
		OpenSlur openTied = new OpenSlur();
		openTied.type = SlurType.Tie;
		openTied.start = new OpenSlur.Waypoint(wp, side);
		pitchTieds.add(openTied);
	}
	
	/**
	 * Ends an existing tie and returns the {@link OpenSlur}.
	 * When the tie does not exist, null is returned.
	 */
	public OpenSlur stopTied(SlurWaypoint wp, VSide side, Context context) {
		Chord chord = wp.getChord();
		Pitch pitch = chord.getNotes().get(wp.getNoteIndex()).getPitch();
		//get stack for this pitch
		Queue<OpenSlur> pitchTieds = openTieds.get(pitch);
		if (pitchTieds == null || pitchTieds.size() == 0) {
			log(warning("Can not stop non-existing tied at " + context.getMp()));
			return null;
		}
		OpenSlur openTied = pitchTieds.poll();
		openTied.stop = new OpenSlur.Waypoint(wp, side);
		return openTied;
	}
	
}
