package com.xenoage.zong.layout.frames;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.HorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.StretchHorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.NoVerticalFrameFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.VerticalFrameFillingStrategy;

/**
 * A {@link ScoreFrame} is a frame that contains a musical score.
 * 
 * A score frame can be linked to another score frame, where the score goes on,
 * if it does not have enough space in this one. This is stored in the
 * {@link ScoreFrame}.
 * 
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper = true) public class ScoreFrame
	extends Frame {

	/** The chain of frames for the score, or null when the frame is unused. */
	@MaybeNull private ScoreFrameChain scoreFrameChain = null;
	/** The horizontal system filling strategy for this score frame. */
	private HorizontalSystemFillingStrategy hFill = defaultHFill;
	/** The vertical frame filling strategy for this score frame. */
	private VerticalFrameFillingStrategy vFill = defaultVFill;

	//default values
	public static final HorizontalSystemFillingStrategy defaultHFill =
		StretchHorizontalSystemFillingStrategy.instance;
	public static final VerticalFrameFillingStrategy defaultVFill =
		NoVerticalFrameFillingStrategy.instance;


	/**
	 * Converts the given position from frame space into score layout space.
	 * Both spaces use mm units, the difference is the origin:
	 * While frames have their origin in the center, the
	 * origin of a score layout is in the upper left corner.
	 */
	public Point2f getScoreLayoutPosition(Point2f framePosition) {
		Point2f ret = framePosition;
		ret = ret.add(size.width / 2, size.height / 2);
		return ret;
	}
	
	/**
	 * Gets the {@link ScoreFrameLayout} of this frame, or null if there is none. 
	 */
	public ScoreFrameLayout getScoreFrameLayout() {
		if (scoreFrameChain == null)
			return null;
		return scoreFrameChain.getScoreFrameLayout(this);
	}

	@Override public FrameType getType() {
		return FrameType.ScoreFrame;
	}

}
