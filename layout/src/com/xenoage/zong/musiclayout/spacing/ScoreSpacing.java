package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.zong.core.position.MP.atMeasure;

import java.util.List;

import lombok.Getter;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.utils.exceptions.IllegalMPException;

/**
 * The spacing information of a {@link Score} layout.
 * 
 * It contains the {@link FrameSpacing}s, which contain the
 * systems, staves, measures and elements of each {@link ScoreFrame}.
 * 
 * @author Andreas Wenger
 */
@Getter
public class ScoreSpacing {
	
	/** The layouted score. */
	public Score score;
	/** The spacings of the score frames. */
	public List<FrameSpacing> frames;
	
	
	public ScoreSpacing(Score score, List<FrameSpacing> frames) {
		this.score = score;
		this.frames = frames;
		//set backward references
		for (FrameSpacing frame : frames)
			frame.score = this;
	}

	public ColumnSpacing getColumn(int measure) {
		for (FrameSpacing frame : frames) {
			if (measure <= frame.getEndMeasureIndex())
				return frame.getColumn(measure);
		}
		throw new IllegalMPException(atMeasure(measure));
	}
	
}
