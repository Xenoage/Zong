package com.xenoage.zong.layout.frames;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;

/**
 * A list of connected {@link ScoreFrame}s.
 * 
 * This class is also used for unconnected, single score frames.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public class ScoreFrameChain {

	/** The list of frames */
	@Getter private ArrayList<ScoreFrame> frames = alist();
	/** The score. */
	@Getter @NonNull private Score score;
	/** The score layouts in the frames. */
	@Getter @Setter private ScoreLayout scoreLayout = null;


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
		registerFrame(newFrame);
		frames.remove(newFrame);
		frames.add(position, newFrame);
	}

	/**
	 * Removes the given frame from this chain and unregisters the chain from the frame.
	 */
	public void remove(ScoreFrame frame) {
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

	/**
	 * Returns true, if the given {@link ScoreFrame} is the first one in this
	 * score frame chain.
	 */
	public boolean isLeadingScoreFrame(ScoreFrame scoreFrame) {
		return frames.get(0) == scoreFrame;
	}

	/**
	 * Gets the {@link ScoreFrameLayout} of the given {@link ScoreFrame}, or
	 * null if unknown.
	 */
	public ScoreFrameLayout getScoreFrameLayout(ScoreFrame frame) {
		int frameIndex = frames.indexOf(frame);
		if (frameIndex == -1)
			return null;
		return scoreLayout.frames.get(frameIndex);
	}

}
