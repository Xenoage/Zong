package com.xenoage.zong.musiclayout.spacer;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.musiclayout.spacer.frame.FrameSpacer.frameSpacer;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.Target;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.FramesSpacing;

/**
 * Breaks the given optimal measure column spacings into systems and frames.
 * 
 * @author Andreas Wenger
 */
public class FramesSpacer {
	
	public static final FramesSpacer framesSpacer = new FramesSpacer();
	
	
	public FramesSpacing compute(List<ColumnSpacing> columns,
		Target target, Context context, Notations notations) {
		
		context.saveMp();
		
		ArrayList<FrameSpacing> frames = alist();
		int measuresCount = context.score.getMeasuresCount();
		int iMeasure = 0;
		int iSystem = 0;
		int iFrame = 0;
		boolean additionalFrameIteration;
		
		while (true) {
			additionalFrameIteration = false;

			//find score frame
			Size2f frameSize;
			if (iFrame < target.areas.size()) {
				//there is another existing score frame
				frameSize = target.getArea(iFrame).size;
			}
			else {
				//there is no another existing score frame
				if (false == target.isCompleteLayout) {
					//we are not interested in the stuff after the last score frame. exit.
					break;
				}
				else if (iMeasure >= measuresCount) {
					//all measures layouted. exit.
					break;
				}
				else {
					//still material to layout and additional frames requested. create one.
					frameSize = target.additionalArea.size;
					additionalFrameIteration = true;
				}
			}

			//some material left to layout?
			if (iMeasure < measuresCount) {
				//more measures to layout
				context.mp = atMeasure(iMeasure);
				FrameSpacing frame = frameSpacer.compute(context, iSystem, frameSize,
					columns, notations);
				if (frame.getSystems().size() > 0) {
					//at least one measure in this frame. remember frame
					frames.add(frame);
					iMeasure = frame.getEndMeasureIndex() + 1;
					iSystem += frame.getSystems().size();
				}
				else {
					//no space for a measure in this frame. empty frame. but only, if frame exists.
					//do not create endless list of empty additional frames
					if (!additionalFrameIteration) {
						frames.add(FrameSpacing.empty(frameSize));
					}
					else {
						break;
					}
				}
			}
			else {
				//no material left. empty frame.
				frames.add(FrameSpacing.empty(frameSize));
			}

			//next frame
			iFrame++;
		}
		
		context.restoreMp();
		
		return new FramesSpacing(frames);
	}
	
}
