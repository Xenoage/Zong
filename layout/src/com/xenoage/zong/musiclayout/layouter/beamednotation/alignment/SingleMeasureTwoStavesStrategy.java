package com.xenoage.zong.musiclayout.layouter.beamednotation.alignment;

import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;

import com.xenoage.utils.iterators.It;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.BeamedStemAlignmentNotationsStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.design.BeamDesign;
import com.xenoage.zong.musiclayout.layouter.beamednotation.design.DoubleBeamDesign;
import com.xenoage.zong.musiclayout.layouter.beamednotation.design.MultipleBeamDesign;
import com.xenoage.zong.musiclayout.layouter.beamednotation.design.SingleBeamDesign;
import com.xenoage.zong.musiclayout.layouter.beamednotation.design.TripleBeamDesign;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.beam.BeamStemAlignments;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;

/**
 * This class helps the {@link BeamedStemAlignmentNotationsStrategy}
 * to compute beams that are all in the same measure, but in
 * two adjacent staves.
 * 
 * The strategy is quite simple: The first and the last stem have the default lengths
 * (or the lengths the user has defined). All other stem alignments can not be
 * computed here, since the distance of the staves is still unknown. They have to be
 * computed later, meanwhile they are null.
 * 
 * @author Andreas Wenger
 */
public class SingleMeasureTwoStavesStrategy
	implements ScoreLayouterStrategy {

	/**
	 * This strategy computes the lengths of the stems of the beamed chords.
	 */
	public NotationsCache computeNotations(Beam beam, NotationsCache notations) {
		NotesAlignment[] chordNa = new NotesAlignment[beam.getWaypoints().size()];
		int beamlines = beam.getMaxBeamLinesCount();
		int i = 0;
		for (BeamWaypoint waypoint : beam.getWaypoints()) {
			Chord chord = waypoint.getChord();
			ChordNotation cn = notations.getChord(chord);
			chordNa[i] = cn.notesAlignment;
			i++;
		}
		Chord firstChord = beam.getStart().getChord();
		Stem firstStem = firstChord.getStem();
		StemDirection firstStemDirection = notations.getChord(firstChord).stemDirection;
		Chord lastChord = beam.getStop().getChord();
		Stem lastStem = lastChord.getStem();
		StemDirection lastStemDirection = notations.getChord(lastChord).stemDirection;

		BeamStemAlignments bsa = computeStemAlignments(chordNa, beamlines, firstStem, lastStem,
			firstStemDirection, lastStemDirection);

		//compute new notations
		NotationsCache ret = new NotationsCache();
		It<BeamWaypoint> waypoints = it(beam.getWaypoints());
		for (BeamWaypoint waypoint : waypoints) {
			Chord chord = waypoint.getChord();
			ChordNotation oldCN = notations.getChord(chord);
			ret.add(oldCN.withStemAlignment(bsa.stemAlignments[waypoints.getIndex()]));
		}

		return ret;
	}

	/**
	 * Computes the vertical positions of the first and the last stem
	 * of the given beam. All other stem endpoints are set to null (which
	 * means unknown).
	 * @param chordNa             the alignments of all chords of the beam
	 * @param beamLinesCount      the number of lines of the beam
	 * @param stemDirection       the direction of the stem
	 * @param firstStem           the stem of the first chord
	 * @param lastStem            the stem of the last chord
	 * @param firstStemDirection  the direction of the first chord
	 * @param lastStemDirection   the direction of the last chord
	 * @return  the alignments of all stems of the given chords                        
	 */
	public BeamStemAlignments computeStemAlignments(NotesAlignment[] chordNa, int beamLinesCount,
		Stem firstStem, Stem lastStem, StemDirection firstStemDirection, StemDirection lastStemDirection) {
		//get appropriate beam design
		BeamDesign beamDesign;
		switch (beamLinesCount) {
		//TIDY: we need only a small subset of the BeamDesign class. extract it?
			case 1:
				beamDesign = new SingleBeamDesign(firstStemDirection, 0);
				break;
			case 2:
				beamDesign = new DoubleBeamDesign(firstStemDirection, 0);
				break;
			case 3:
				beamDesign = new TripleBeamDesign(firstStemDirection, 0);
				break;
			default:
				beamDesign = new MultipleBeamDesign(firstStemDirection, 0, beamLinesCount);
		}

		//compute stem alignments
		int chordsCount = chordNa.length;
		StemAlignment[] stemAlignments = new StemAlignment[chordsCount];
		for (int i = 0; i < chordsCount; i++) {
			StemAlignment stemAlignment = null; //unknown
			if (i == 0 || i == chordsCount - 1) {
				Stem stem = (i == 0 ? firstStem : lastStem);
				StemDirection stemDirection = (i == 0 ? firstStemDirection : lastStemDirection);

				//start LP
				float startLP;
				if (stemDirection == Up) {
					startLP = chordNa[i].getLinePositions().getBottom();
				}
				else {
					startLP = chordNa[i].getLinePositions().getTop();
				}

				//end LP
				float endLP;
				if (stem.getLength() != null) {
					//use user-defined length
					endLP = startLP + stemDirection.getSign() * 2 * stem.getLength();
				}
				else {
					//compute length
					endLP = startLP + stemDirection.getSign() * 2 * beamDesign.getMinimumStemLength();
				}

				stemAlignment = new StemAlignment(startLP, endLP);
			}
			stemAlignments[i] = stemAlignment;
		}
		BeamStemAlignments beamstemalignments = new BeamStemAlignments(stemAlignments,
			BeamDesign.BEAMLINE_WIDTH, beamDesign.getDistanceBetweenBeamLines(), beamLinesCount);

		return beamstemalignments;
	}

}
