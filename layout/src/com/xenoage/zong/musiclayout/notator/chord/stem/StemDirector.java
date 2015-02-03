package com.xenoage.zong.musiclayout.notator.chord.stem;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.musiclayout.notator.chord.stem.beam.BeamedStemDirector.beamedStemDirector;
import static com.xenoage.zong.musiclayout.notator.chord.stem.single.SingleStemDirector.singleStemDirector;

import java.util.Map;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.position.MP;

/**
 * Computes the {@link StemDirection} of chords.
 * 
 * @author Andreas Wenger
 */
public final class StemDirector {

	public static StemDirector stemDirector = new StemDirector();
	

	/**
	 * Computes the {@link StemDirection} of the given chord (and maybe connected
	 * ones) and returns them. The chord must be part of a score.
	 */
	public Map<Chord, StemDirection> compute(Chord chord) {
		Map<Chord, StemDirection> ret = map();
		Beam beam = chord.getBeam();
		Score score = chord.getScore();
		if (beam != null) {
			//compute beamed chords together, i.e. only for the first chord in the beam
			if (beam.getChord(0) == chord) {
				StemDirection[] beamedStems = beamedStemDirector.compute(beam, score);
				for (int iChord : range(beam.size()))
					ret.put(beam.getChord(iChord), beamedStems[iChord]);
			}
		}
		else {
			//compute stem direction for single chord
			MP mp = MP.getMP(chord);
			StemDirection stem = singleStemDirector.compute(chord,
				score.getMusicContext(mp, BeforeOrAt, Before));
			ret.put(chord, stem);
		}
		return ret;
	}

}
