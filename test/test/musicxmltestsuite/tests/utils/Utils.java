package musicxmltestsuite.tests.utils;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Grace;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;

/**
 * Helper methods.
 * 
 * @author Andreas Wenger
 */
public class Utils {
	
	public static Chord ch(Fraction duration, Pitch... pitches) {
		ArrayList<Note> notes = alist();
		for (Pitch pitch : pitches)
			notes.add(new Note(pitch));
		return new Chord(notes, duration);
	}
	
	/**
	 * Sets the given chord to a grace chord.
	 */
	public static Chord gr(Fraction graceDuration, boolean slash, Pitch... pitches) {
		Grace grace = new Grace(slash, graceDuration);
		ArrayList<Note> notes = alist();
		for (Pitch pitch : pitches)
			notes.add(new Note(pitch));
		return new Chord(notes, grace);
	}
	
	/**
	 * Creates a slur for the given chords (to the first notes).
	 */
	public static Slur slur(Chord start, Chord end) {
		return new Slur(SlurType.Slur, new SlurWaypoint(start, 0, null),
			new SlurWaypoint(end, 0, null), null);
	}
	
	public static Articulation articulation(ArticulationType type, Placement placement) {
		Articulation ret = new Articulation(type);
		ret.setPlacement(placement);
		return ret;
	}
	
	public static Fermata fermata(Positioning positioning) {
		Fermata ret = new Fermata();
		ret.setPositioning(positioning);
		return ret;
	}
	
	public static void checkDurations(Staff staff, Fraction[] expectedDurations) {
		int iDuration = 0;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				//check duration
				assertEquals("element " + iDuration, expectedDurations[iDuration++], e.getDuration());
			}
		}
		assertEquals("not all element found", expectedDurations.length, iDuration);
	}
	
	public static void checkGraceChords(Staff staff, Chord[] expectedChords) {
		checkGraceChords(staff, expectedChords, false);
	}
	
	public static void checkGraceChords(Staff staff, Chord[] expectedChords, boolean skipRests) {
		int iChord = 0;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				if (e instanceof Rest && skipRests)
					continue;
				//check duration, type and notes
				Chord chord = (Chord) e;
				Chord expectedChord = expectedChords[iChord];
				assertEquals("chord " + iChord, expectedChord.getDuration(), chord.getDuration());
				assertEquals("chord " + iChord, expectedChord.getGrace(), chord.getGrace());
				assertEquals("chord " + iChord, expectedChord.getNotes(), chord.getNotes());
				iChord++;
			}
		}
		assertEquals("not all chords found", expectedChords.length, iChord);
	}

}
