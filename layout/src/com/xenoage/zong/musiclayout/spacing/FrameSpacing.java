package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.collections.CollectionUtils.getLast;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.math.geom.Size2f;

/**
 * The arrangement of {@link SystemSpacing} on a single score frame.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class FrameSpacing {

	/** The systems on this frame. */
	public List<SystemSpacing> systems;
	/** The size in mm this frame arrangement may use. */
	public Size2f usableSizeMm;

	
	/**
	 * Returns an empty {@link FrameSpacing} with the given size.
	 */
	public static FrameSpacing empty(Size2f usableSizeMm) {
		return new FrameSpacing(Collections.<SystemSpacing>emptyList(), usableSizeMm);
	}

	/**
	 * Gets the index of the first measure, or -1 if there are no measures.
	 */
	public int getStartMeasureIndex() {
		if (systems.size() == 0)
			return -1;
		else
			return getFirst(systems).startMeasureIndex;
	}

	/**
	 * Gets the index of the last measure, or -1 if there are no measures.
	 */
	public int getEndMeasureIndex() {
		if (systems.size() == 0)
			return -1;
		else
			return getLast(systems).endMeasureIndex;
	}

}
