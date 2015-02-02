package com.xenoage.zong.musiclayout.notator.chord.stem.beam.direction;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.position.MP;

/**
 * {@link Strategy} for a {@link Beam}, which spans over two adjacent
 * staves in a single measure column.
 * 
 * @author Andreas Wenger
 */
public class OneMeasureTwoStaves
	extends Strategy {


	@Override public StemDirection[] compute(Beam beam, Score score) {
		return compute(beam);
	}

	/**
	 * Currently, the stems of the chords of the upper staff always point down, while
	 * the stems of the chords of the lower staff always point up.
	 */
	StemDirection[] compute(Beam beam) {
		StemDirection[] dirs = new StemDirection[beam.size()];
		int upperStaffIndex = beam.getUpperStaffIndex();
		for (int iChord : range(dirs)) {
			int staffIndex = MP.getMP(beam.getChord(iChord)).staff;
			dirs[iChord] = (staffIndex == upperStaffIndex ? Down : Up);
		}
		return dirs;
	}

}
