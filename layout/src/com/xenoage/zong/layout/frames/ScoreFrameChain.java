package com.xenoage.zong.layout.frames;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;

import lombok.Getter;

/**
 * A list of connected {@link ScoreFrame}s.
 * 
 * This class is also used for unconnected, single score frames.
 * 
 * @author Andreas Wenger
 */
public class ScoreFrameChain {

	/** The list of frames */
	@Getter private ArrayList<ScoreFrame> frames = alist();

	
	/**
	 * Adds the given frame to the end of the chain, if it is not already part
	 * of the chain.
	 * If the frame was already part of another chain, it is unregistered there.
	 * It is registered for this chain.
	 */
	public void add(ScoreFrame frame) {
		if (frames.contains(frame)) {
			throw new IllegalArgumentException("Score frame is already part of the chain!");
		}
		registerFrame(frame);
		frames.add(frame);
	}

	/**
	 * Adds the given frame in the middle of the chain, after the given frame.
	 * If the frame is already part of the chain, it is moved to the new
	 * position.
	 * If the frame was already part of another chain, it is unregistered there.
	 * It is registered for this chain.
	 */
	public void add(ScoreFrame atFrame, ScoreFrame newFrame) {
		if (!frames.contains(atFrame)) {
			throw new IllegalArgumentException("Anchor frame does not exist!");
		}
		int pos = frames.indexOf(atFrame);
		add(newFrame, pos + 1);
	}

	/**
	 * Adds the givenframe at the given position.
	 * If the frame is already part of the chain, it is moved to the new
	 * position.
	 * If the frame was already part of another chain, it is unregistered there.
	 * It is registered for this chain.
	 */
	public void add(ScoreFrame newFrame, int position) {
		frames.remove(newFrame);
		frames.add(position, newFrame);
	}
	
	/**
	 * Removes the given frame from this chain and unregisters the chain from the frame.
	 */
	private void remove(ScoreFrame frame) {
		frames.remove(frame);
		frame.setScoreFrameChain(null);
	}
	
	/**
	 * If the frame was already part of another chain, it is unregistered there.
	 * It is registered for this chain.
	 */
	private void registerFrame(ScoreFrame frame) {
		if (frame.getScoreFrameChain() != null)
			frame.getScoreFrameChain().remove(frame);
		frame.setScoreFrameChain(this);
	}

}
