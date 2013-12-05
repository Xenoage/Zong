package com.xenoage.zong.layout;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.layout.frames.FP;
import com.xenoage.zong.layout.frames.Frame;

/**
 * One page within a page layout.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
@Getter @Setter public final class Page
	implements LayoutContainer {

	/** The parent layout of the page, or null if not part of a layout. */
	@MaybeNull private Layout parentLayout = null;
	/** The format of the page. */
	private PageFormat format;
	/** The list of frames. */
	private ArrayList<Frame> frames = alist();


	public Page(PageFormat format) {
		this.format = format;
	}

	/**
	 * Gets the index of this page, or -1 if this page is not part of a layout.
	 */
	public int getIndex() {
		if (parentLayout != null)
			return parentLayout.getPages().indexOf(this);
		else
			return -1;
	}

	/**
	 * Adds the given frame.
	 */
	public void addFrame(Frame frame) {
		if (frames.contains(frame))
			throw new IllegalArgumentException("frame is already a child of this page");
		if (frame.getParent() != null)
			throw new IllegalArgumentException("frame is still a child of another parent");
		frame.setParent(this);
		frames.add(frame);
	}
	
	/**
	 * Removes the given frame.
	 */
	public void removeFrame(Frame frame) {
		if (false == frames.contains(frame))
			throw new IllegalArgumentException("frame is not a child of this page");
		frame.setParent(null);
		frames.remove(frame);
	}

	/**
	 * Transforms the given coordinates in page space to
	 * a frame position.
	 * If there is no frame, null is returned.
	 */
	public FP getFP(Point2f p) {
		//since frames are painted in forward direction,
		//the last one is the highest one. so we have to
		//check for clicks in reverse order.
		for (int i : rangeReverse(frames)) {
			FP fp = frames.get(i).getFP(p);
			if (fp != null) {
				return fp;
			}
		}
		return null;
	}

	@Override public Page getParentPage() {
		return this;
	}

	@Override public float getAbsoluteRotation() {
		return 0;
	}

	@Override public Point2f getAbsolutePosition() {
		return Point2f.origin;
	}

	@Override public Point2f getPagePosition(Point2f p) {
		return p;
	}

}
