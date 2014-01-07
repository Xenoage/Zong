package com.xenoage.zong.layout.frames;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.layout.LayoutContainer;

/**
 * A group frame is a frame that contains
 * any number of child frames.
 * 
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper = true) public class GroupFrame
	extends Frame
	implements LayoutContainer {

	/** The list of child frames. */
	public final ArrayList<Frame> children = alist();


	/**
	 * Adds a child frame to this group frame.
	 */
	public void addChildFrame(Frame child) {
		if (children.contains(child))
			throw new IllegalArgumentException("frame is already a child of this page");
		if (child.getParent() != null)
			throw new IllegalArgumentException("frame is still a child of another parent");
		child.setParent(this);
		children.add(child);
	}
	
	/**
	 * Removes the given child frame from this group frame.
	 */
	public void removeChildFrame(Frame child) {
		if (false == children.contains(child))
			throw new IllegalArgumentException("frame is not a child of this page");
		child.setParent(null);
		children.remove(child);
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
	
	/**
	 * Gets a list with all {@link ScoreFrame}s in this {@link GroupFrame}.
	 */
	public List<ScoreFrame> getScoreFrames() {
		List<ScoreFrame> ret = alist();
		for (Frame frame : children) {
			if (frame instanceof ScoreFrame)
				ret.add((ScoreFrame) frame);
			else if (frame instanceof GroupFrame)
				ret.addAll(((GroupFrame) frame).getScoreFrames());
		}
		return ret;
	}

	@Override public FrameType getType() {
		return FrameType.GroupFrame;
	}

}
