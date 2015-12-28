package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.Map;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.layout.frames.ScoreFrame;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The spacing information of a {@link Score} layout.
 * 
 * It contains the {@link FrameSpacing}s, which contain the
 * systems, staves, measures and elements of each {@link ScoreFrame}.
 * 
 * Also the {@link BeamSpacing}s are saved here.
 * 
 * @author Andreas Wenger
 */
@Getter @AllArgsConstructor
public class ScoreSpacing {
	
	/** The layouted score. */
	public Score score;
	/** The spacings of the score frames. */
	public FramesSpacing frames;
	/** The spacings of the beams. */
	public Map<Beam, BeamSpacing> beams = map(); 
	
}
