package com.xenoage.zong.musiclayout.spacer;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.util.BeamIterator;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.FramesSpacing;
import lombok.val;

import java.util.Map;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSpacer.beamSpacer;

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
			val frame = frames.getFrame(mp.measure);
			val system = frame.getSystem(mp.measure);
			val beamNotation = (BeamNotation) notations.get(beam);
			ret.put(beam, beamSpacer.compute(beamNotation, system, score));
		}
		return ret;
	}

}
