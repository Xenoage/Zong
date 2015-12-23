package com.xenoage.zong.musiclayout.notator.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.musiclayout.notator.beam.BeamFragmenter.beamFragmenter;
import static com.xenoage.zong.musiclayout.notator.beam.lines.Beam128thRules.beam128thRules;
import static com.xenoage.zong.musiclayout.notator.beam.lines.Beam16thRules.beam16thRules;
import static com.xenoage.zong.musiclayout.notator.beam.lines.Beam32ndRules.beam32ndRules;
import static com.xenoage.zong.musiclayout.notator.beam.lines.Beam64thRules.beam64thRules;
import static com.xenoage.zong.musiclayout.notator.beam.lines.Beam8thRules.beam8thRules;
import static java.lang.Math.min;

import java.util.List;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.beam.Fragments;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import com.xenoage.zong.musiclayout.notator.beam.lines.BeamRules;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.ScoreSpacing;

/**
 * Computes {@link BeamNotation}s and modifies the {@link StemNotation}s of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public class BeamNotator {
	
	public static final BeamNotator beamNotator = new BeamNotator();
	
	private static BeamRules[] allBeamRules = { null, beam8thRules, beam16thRules,
		beam32ndRules, beam64thRules, beam128thRules };


	//GOON: do not require scoreSPACING!! we do notations here, not spacings
	public void compute(Beam beam, ScoreSpacing scoreSpacing) {
		//compute fragments
		List<Fragments> fragments = beamFragmenter.compute(beam);
		//compute stem length and gap
		BeamRules beamRules = allBeamRules[min(beam.getMaxLinesCount(), allBeamRules.length - 1)];
		float minStemLengthIs = beamRules.getMinimumStemLengthIs(); //GOON use in spacer
		float gapIs = beamRules.getGapIs();
		//collect chords
		List<ChordNotation> chords = alist(beam.size());
		ColumnSpacing column = scoreSpacing.getColumn(getMP(beam.getChord(0)).measure);
		for (BeamWaypoint waypoint : beam.getWaypoints()) {
			Chord chord = waypoint.getChord();
			ElementSpacing cs = column.getElement(chord);
			ChordNotation cn = (ChordNotation) cs.notation;
			chords.add(cn);
		}
		//create notation
		BeamNotation beamNotation = new BeamNotation(beam, fragments, gapIs, chords);
		//save notation in each chord
		for (ChordNotation chord : chords)
			chord.beam = beamNotation;
	}
	
	
	/**
	 * Returns true, when the lines of the given beam are completely outside the staff
	 * (not even touching a staff line).
	 * @param stemDirection      the direction of the stems
	 * @param firstStemEndLp     the LP of the endpoint of the first stem
	 * @param lastStemEndLp      the LP of the endpoint of the last stem  
	 * @param staffLinesCount    the number of staff lines 
	 * @param totalBeamHeightIs  the total height of the beam lines (including gaps) in IS
	 * GOON: move to BeamPlacer
	 */
	public boolean isBeamOutsideStaff(StemDirection stemDirection, float firstStemEndLp,
		float lastStemEndLp, int staffLinesCount, float totalBeamHeightIs) {
		float maxStaffLp = (staffLinesCount - 1) * 2;
		if (stemDirection == Up) {
			//beam lines above the staff?
			if (firstStemEndLp > maxStaffLp + totalBeamHeightIs * 2 &&
				lastStemEndLp > maxStaffLp + totalBeamHeightIs * 2) {
				return true;
			}
			//beam lines below the staff?
			if (firstStemEndLp < 0 && lastStemEndLp < 0) {
				return true;
			}
		}
		else if (stemDirection == Down) {
			//beam lines above the staff?
			if (firstStemEndLp > maxStaffLp && lastStemEndLp > maxStaffLp) {
				return true;
			}
			//beam lines below the staff?
			if (firstStemEndLp < -1 * totalBeamHeightIs * 2 &&
				lastStemEndLp < -1 * totalBeamHeightIs * 2) {
				return true;
			}
		}
		return false;
	}

}
