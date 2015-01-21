package com.xenoage.zong.layout.frames;

import static com.xenoage.utils.math.geom.Point2f.origin;
import static com.xenoage.utils.math.geom.Size2f.size0;
import static com.xenoage.zong.layout.LP.lp;
import static java.lang.Math.max;
import lombok.Data;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.LP;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutContainer;
import com.xenoage.zong.layout.Page;

/**
 * A frame is the abstract base class for
 * object in a layout of a score document.
 * 
 * It is basically a rectangle with a position and size
 * and rotation and an optional background.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
@Data public abstract class Frame {

	/** The parent of this frame, or null if not in a layout. */
	protected LayoutContainer parent = null;

	/** Center point of the frame in mm, relative to its parent. */
	protected Point2f position = origin;

	/** Size of the frame in mm. */
	protected Size2f size = size0;

	/** Ccw. rotation of the frame in degrees, relative to its parent. */
	protected float rotation = 0;

	/** Background of the frame, or null. */
	@MaybeNull protected Color background = null;


	/**
	 * Gets the parent layout of this frame, or null, if not part of a layout.
	 */
	public Layout getParentLayout() {
		return (parent != null ? parent.getParentLayout() : null);
	}

	/**
	 * Gets the parent page of this frame, or null, if not part of a page.
	 */
	public Page getParentPage() {
		return (parent != null ? parent.getParentPage() : null);
	}

	/**
	 * Gets the center position of the frame in mm, relative to the page.
	 */
	public final Point2f getAbsolutePosition() {
		Point2f ret = position;
		if (parent != null) {
			ret = MathUtils.rotate(ret, parent.getAbsoluteRotation());
			ret = ret.add(parent.getAbsolutePosition());
		}
		return ret;
	}

	/**
	 * Gets the {@link LP} of the center of the frame.
	 * If this frame is not part of a layout, the page index -1 is returned.
	 */
	public final LP getCenterLP() {
		Point2f pos = getAbsolutePosition();
		Layout layout = getParentLayout();
		Page page = getParentPage();
		int pageIndex = (page != null ? page.getIndex() : -1);
		return lp(layout, pageIndex, pos);
	}

	/**
	 * Gets the counter clockwise rotation of the frame in degrees.
	 */
	public final float getAbsoluteRotation() {
		float ret = rotation;
		if (parent != null) {
			ret += parent.getAbsoluteRotation();
		}
		return ret;
	}

	/**
	 * Transforms the given coordinates in page space to a {@link FP}.
	 * If the given coordinates are not within this frame, null is returned.
	 */
	public FP getFP(Point2f p) {
		return getFP(p, false);
	}

	/**
	 * Transforms the given coordinates in page space to a {@link FP}.
	 * If the given coordinates are not within this frame and force is false,
	 * null is returned. Otherwise the computed coordinates are returned, even if
	 * they are outside the frame.
	 */
	public FP getFP(Point2f p, boolean force) {
		Point2f pos = getAbsolutePosition();
		float rot = getAbsoluteRotation();
		float hw = size.width / 2;
		float hh = size.height / 2;
		//two cases: no rotation or rotation
		if (rot == 0f) {
			//no rotation. this is easy to compute.
			if (force ||
				(p.x >= pos.x - hw && p.x <= pos.x + hw && p.y >= pos.y - hh && p.y <= pos.y + hh)) {
				return new FP(this, new Point2f(p.x - pos.x, p.y - pos.y));
			}
		}
		else {
			//rotated frame. this is more complicated.
			//first fast check: point within circle around center point?
			float radius = max(size.width, size.height);
			Point2f pRel = new Point2f(p.x - pos.x, p.y - pos.y);
			float distanceSq = pRel.x * pRel.x + pRel.y * pRel.y;
			if (force || (distanceSq <= radius * radius)) {
				//the given point could be within the frame. rotate the
				//point and check again.
				pRel = MathUtils.rotate(pRel, -rot);
				if (force || (pRel.x >= -hw && pRel.x <= +hw && pRel.y >= -hh && pRel.y <= +hh)) {
					return new FP(this, new Point2f(pRel.x, pRel.y));
				}
			}
		}
		return null;
	}

	/**
	 * Transforms the given coordinates in frame space to
	 * a position in page space.
	 * 
	 * Untested for higher levels
	 */
	@Untested public final Point2f getPagePosition(Point2f p) {
		Point2f ret = p;
		Frame frame = this;
		if (parent != null) {
			if (frame.rotation != 0f)
				ret = MathUtils.rotate(ret, frame.rotation);
			ret = ret.add(frame.position);
		}
		return parent.getPagePosition(p);
	}

	/**
	 * Gets the type of this frame.
	 */
	public abstract FrameType getType();

}
