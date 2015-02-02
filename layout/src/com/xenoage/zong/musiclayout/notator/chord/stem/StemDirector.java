package com.xenoage.zong.musiclayout.notator.chord.stem;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.musiclayout.notator.chord.stem.beam.BeamedStemDirector.beamedStemDirector;
import static com.xenoage.zong.musiclayout.notator.chord.stem.single.SingleStemDirector.singleStemDirector;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ChordNotation;

/**
 * Computes the {@link StemDirection} of the chords of a score.
 * 
 * @author Andreas Wenger
 */
public final class StemDirector {
	
	public static StemDirector stemDirector = new StemDirector();

	/**
	 * Computes the {@link StemDirection} of all the chords of the given score
	 * and stores them in the given {@link NotationsCache}.
	 */
	public void compute(Score score, NotationsCache notationsCache) {
		for (int iStaff : range(score.getStavesCount())) {
			Staff staff = score.getStaff(iStaff);
			for (int iMeasure : range(score.getMeasuresCount())) {
				Measure measure = staff.getMeasure(iMeasure);
				for (int iVoice : range(measure.getVoices())) {
					Voice voice = measure.getVoice(iVoice);
					for (int iElement : range(voice.getElements())) {
						VoiceElement element = voice.getElement(iElement);
						if (element instanceof Chord) {
							MP mp = atElement(iStaff, iMeasure, iVoice, iElement);
							computeChord((Chord) element, mp, score, notationsCache);
						}
					}
				}
			}
		}
	}
	
	void computeChord(Chord chord, MP mp, Score score, NotationsCache notationsCache) {
		Beam beam = chord.getBeam();
		if (beam != null) {
			//compute beamed chords together, i.e. only for the first chord in the beam
			if (beam.getChord(0) == chord) {
				StemDirection[] beamedStems = beamedStemDirector.compute(beam, score);
				for (int iChord : range(beam.size())) {
					ChordNotation cn = notationsCache.getChord(beam.getChord(iChord));
					cn.stemDirection = beamedStems[iChord];
				}
			}
		}
		else {
			//compute stem direction for single chord
			StemDirection stem = singleStemDirector.compute(chord, score.getMusicContext(
				mp, BeforeOrAt, Before));
			notationsCache.getChord(chord).stemDirection = stem;
		}
	}

}
