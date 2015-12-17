package com.xenoage.zong.musiclayout.notator.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.zong.core.music.util.DurationInfo.getFlagsCount;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.HookLeft;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.HookRight;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.Start;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.Stop;

import java.util.List;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.musiclayout.notation.beam.Fragments;

/**
 * Computes the {@link Fragments} of the lines of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public class BeamFragmenter {
	
	public static final BeamFragmenter beamFragmenter = new BeamFragmenter();
	
	
	/**
	 * Computes the line fragments for all lines of the given beam,
	 * starting with the 16th, then 32nd, ... line.
	 */
	public List<Fragments> compute(Beam beam) {
		int linesCount = beam.getMaxLinesCount();
		List<Fragments> ret = alist(linesCount - 1);
		Fragments lastFragments = null;
		for (int line : rangeReverse(linesCount - 1, 1))
			ret.add(lastFragments = compute(beam, line, lastFragments));
		return ret;
	}
	
	
	/**
	 * Computes the fragments for the given line (1: 16th line, 2: 32th line, ...).
	 * Use an algorithm based on the rules in Chlapik, page 45, rule 6.
	 * 
	 * Begin with the highest line (e.g. 32th before 16th), and use the result of line n
	 * as a parameter to compute line n-1 (for the first computation, use null).
	 * This is needed to support Chlapik, page 45, rule 6, example of row 3, column 6.
	 * Without that, the 16th line would go from the second note to the fourth one.
	 */
	Fragments compute(Beam beam, int line, Fragments higherLine) {
		if (line < 1)
			throw new IllegalArgumentException("This method only works for 16th lines or higher");
		//in this algorithm, we go from note to note, looking for "groups".
		//groups are consecutive chords/stems with the same number of flags (or
		//a higher number inbetween) and not divided by a subdivision break.
		//initialize return array with none-waypoints
		Fragments ret = new Fragments(beam.size());
		int lastFlagsCount = -1;
		int startChord = -1; //start chord of the last group, or -1 if no group is open
		int stopChord = -1; //stop chord of the last group, or -1 if group is open
		for (int iChord : range(beam.size() + 1)) {
			if (iChord < beam.getWaypoints().size()) {
				//another chord within the beam
				Chord chord = beam.getChord(iChord);
				int flagsCount = getFlagsCount(chord.getDuration());
				//enough flags for the given line? (e.g. a 8th beam has no 16th line)
				if (flagsCount >= line + 1) {
					//yes, we need a line of the given line for this stem
					if (startChord == -1) {
						if (higherLine == null || higherLine.get(iChord) != HookLeft) {
							//start new group
							startChord = iChord;
							lastFlagsCount = flagsCount;
						}
						else {
							//example mentioned in the method documentation (Chlapik, page 45, row 3, col 6)
							//we place a hook. this is not explicitly mentioned in the text, but seems to
							//be right when looking at the example.
							startChord = iChord;
							stopChord = iChord;
						}
					}
					else if (lastFlagsCount > -1 && (flagsCount < flagsCount || //less flags than previous stem
						beam.isEndOfSubdivision(iChord))) { //forced subdivision break
						//end the group here
						stopChord = iChord - 1;
					}
				}
				else {
					//no, we need no line of the given line for this stem
					//so, close the last group
					stopChord = iChord - 1;
				}
			}
			else {
				//no more chord in the beam, so we have to close
				stopChord = iChord - 1;
			}
			//if a group was closed, create it
			if (startChord > -1 && stopChord > -1) {
				//type of line is dependent on number of chords in the group
				int chordsCount = stopChord - startChord + 1;
				if (chordsCount > 1) {
					//simple case: more than one chord. create a normal line
					//between those stems
					ret.set(startChord, Start);
					ret.set(stopChord, Stop);
				}
				else {
					//more difficult case: exactly one chord.
					if (startChord == 0) {
						//first chord in beam has always hook to the right
						ret.set(startChord, HookRight);
					}
					else if (startChord == beam.getWaypoints().size() - 1) {
						//last chord in beam has always hook to the left
						ret.set(startChord, HookLeft);
					}
					else {
						//middle chords have left hook, if the preceding chord
						//has a longer or equal duration than the following chord,
						//otherwise they have a right hook
						Fraction left = beam.getChord(startChord - 1).getDuration();
						Fraction right = beam.getChord(startChord + 1).getDuration();
						if (left.compareTo(right) >= 0)
							ret.set(startChord, HookLeft);
						else
							ret.set(startChord, HookRight);
					}
				}
				//reset group data
				startChord = -1;
				stopChord = -1;
				lastFlagsCount = -1;
			}
		}
		return ret;
	}

}
