package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.collections.CollectionUtils.containsNull;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.math.geom.Size2f;

/**
 * This class contains the arrangement
 * of systems on a single score frame.
 *
 * @author Andreas Wenger
 */
@Const @Getter public final class FrameArrangement {

	/** The systems on this frame. */
	public final IList<SystemArrangement> systems;
	/** The size in mm this frame arrangement may use. */
	public final Size2f usableSize;


	//LOMBOK
	public FrameArrangement(IList<SystemArrangement> systems, Size2f usableSize) {
		//null values are not allowed
		if (containsNull(systems) || usableSize == null)
			throw new IllegalArgumentException("Systems and size may not be null");
		this.systems = systems;
		this.usableSize = usableSize;
	}

	/**
	 * Gets the index of the first measure, or -1 if there are no measures.
	 */
	public int getStartMeasureIndex() {
		if (systems.size() == 0)
			return -1;
		else
			return systems.getFirst().startMeasureIndex;
	}

	/**
	 * Gets the index of the last measure, or -1 if there are no measures.
	 */
	public int getEndMeasureIndex() {
		if (systems.size() == 0)
			return -1;
		else
			return systems.getLast().endMeasureIndex;
	}

}
