package com.xenoage.zong.musiclayout.spacer;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSpacer.beamSpacer;

import java.util.Map;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.util.BeamIterator;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.FramesSpacing;

/**
 * Computes the {@link BeamSpacing}s for a score.
 * 
 * @author Andreas Wenger
 */
public class BeamsSpacer {
	
	public static final BeamsSpacer beamsSpacer = new BeamsSpacer();
	
	
	public Map<Beam, BeamSpacing> compute(Score score, Notations notations, FramesSpacing frames) {
		Map<Beam, BeamSpacing> ret = map();
		BeamIterator itB = new BeamIterator(score);
		for (Beam beam : itB) {
			MP mp = itB.getMp();
			int staffLinesCount = score.getStaff(mp).getLinesCount();
			FrameSpacing frame = frames.getFrame(mp.measure);
			ret.put(beam, beamSpacer.compute(beam, notations, frame, staffLinesCount));
		}
		return ret;
	}

}
