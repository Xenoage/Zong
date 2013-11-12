package com.xenoage.zong.layout.frames;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.HorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.VerticalFrameFillingStrategy;

/**
 * A group frame is a frame that contains
 * any number of child frames.
 * 
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper = true) public class GroupFrame
	extends Frame {

	/** The list of child frames. */
	public final ArrayList<Frame> children = alist();


	/**
	 * Adds a child frame to this group frame, if not already a child.
	 */
	public void addChildFrame(Frame child) {
		if (false == children.contains(child))
			children.add(child);
	}

	/**
	 * Transforms the given coordinates in page space to a frame position.
	 * If the given coordinates are not within this frame, null is returned.
	 * If the given coordinates are within a child frame, the child frame
	 * and the coordinates relative to the child frame are returned.
	 */
	@Override public FP getFP(Point2f p) {
		//check child frames in reverse direction (begin with
		//top frame).
		for (int i : rangeReverse(children)) {
			FP fp = children.get(i).getFP(p);
			if (fp != null) {
				return fp;
			}
		}
		//check this frame
		FP fp = super.getFP(p);
		if (fp != null) {
			return fp;
		}
		else {
			return null;
		}
	}

	@Override public FrameType getType() {
		return FrameType.GroupFrame;
	}

}
