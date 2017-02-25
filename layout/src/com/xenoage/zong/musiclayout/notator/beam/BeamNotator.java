package com.xenoage.zong.musiclayout.notator.beam;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.beam.Fragments;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import com.xenoage.zong.musiclayout.notator.ElementNotator;
import com.xenoage.zong.musiclayout.notator.beam.lines.BeamRules;

import java.util.List;

import static com.xenoage.zong.musiclayout.notator.beam.BeamFragmenter.beamFragmenter;

/**
 * Computes {@link BeamNotation}s and modifies the {@link StemNotation}s of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public class BeamNotator
	implements ElementNotator {
	
	public static final BeamNotator beamNotator = new BeamNotator();
	
	
	@Override @MaybeNull public BeamNotation compute(MPElement element, Context context, Notations notations) {
		return compute((Beam) element, notations);
	}

	
	@MaybeNull public BeamNotation compute(Beam beam, Notations notations) {
		//compute fragments
		List<Fragments> fragments = beamFragmenter.compute(beam);
		//get minimum stem length and gap
		BeamRules beamRules = BeamRules.getRules(beam);
		float gapIs = beamRules.getGapIs();
		//collect chords
		List<ChordNotation> chords = notations.getBeamChords(beam);
		//create notation
		BeamNotation beamNotation = new BeamNotation(beam, beam.getMP(), fragments, gapIs, chords);
		return beamNotation;
	}

}
