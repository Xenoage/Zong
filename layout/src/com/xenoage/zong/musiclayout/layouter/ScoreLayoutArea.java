package com.xenoage.zong.musiclayout.layouter;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.HorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.VerticalFrameFillingStrategy;

/**
 * An area in which a score should be layouted.
 * 
 * @author Andreas Wenger
 */
@Const @Data @AllArgsConstructor public class ScoreLayoutArea {

	private final Size2f size;
	private final HorizontalSystemFillingStrategy hFill;
	private final VerticalFrameFillingStrategy vFill;
	
	
	public ScoreLayoutArea(Size2f size) {
		this.size = size;
		this.hFill = ScoreFrame.defaultHFill;
		this.vFill = ScoreFrame.defaultVFill;
	}

}
