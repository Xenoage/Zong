package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.zong.core.position.MP.atMeasure;

import java.util.Iterator;
import java.util.List;

import com.xenoage.zong.utils.exceptions.IllegalMPException;

import lombok.AllArgsConstructor;

/**
 * A list of {@link FrameSpacing}s.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class FramesSpacing
	implements Iterable<FrameSpacing> {
	
	private List<FrameSpacing> frames;
	
	public int size() {
		return frames.size();
	}
	
	public FrameSpacing get(int index) {
		return frames.get(index);
	}
	
	public FrameSpacing getFrame(int measure) {
		for (FrameSpacing frame : frames) {
			if (measure <= frame.getEndMeasureIndex())
				return frame;
		}
		throw new IllegalMPException(atMeasure(measure));
	}

	public ColumnSpacing getColumn(int measure) {
		return getFrame(measure).getColumn(measure);
	}

	@Override public Iterator<FrameSpacing> iterator() {
		return frames.iterator();
	}
	
}
