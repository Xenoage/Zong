package com.xenoage.zong.layout.frames;

import static com.xenoage.utils.pdlib.PVector.pvec;

import com.xenoage.utils.pdlib.PVector;


/**
 * A list of connected score frames.
 * 
 * This class is also used for unconnected, single score frames.
 * 
 * @author Andreas Wenger
 */
public final class ScoreFrameChain
{

	/** The list of frames */
	public final PVector<ScoreFrame> frames;


	/**
	 * Creates a new list of conntected score frames, beginning with the given frame.
	 */
	public static ScoreFrameChain create(ScoreFrame firstFrame)
	{
		return new ScoreFrameChain(pvec(firstFrame));
	}
	
	
	public ScoreFrameChain(PVector<ScoreFrame> frames)
	{
		if (frames.size() == 0)
			throw new IllegalArgumentException("A score frame chain must have at least one frame");
		this.frames = frames;
	}


	/**
	 * Adds the given frame to the end of the chain, if it is not already part
	 * of the chain.
	 */
	public ScoreFrameChain plusFrame(ScoreFrame frame)
	{
		if (frames.contains(frame))
		{
			throw new IllegalArgumentException(
				"Score frame is already part of the chain!");
		}
		return new ScoreFrameChain(frames.plus(frame));
	}


	/**
	 * Adds the given frame in the middle of the chain, after the given Frame.
	 * If the Frame is already part of the chain, it is moved to the new
	 * position.
	 */
	public ScoreFrameChain plusFrame(ScoreFrame atFrame, ScoreFrame newFrame)
	{
		if (!frames.contains(atFrame))
		{
			throw new IllegalArgumentException("Anchor frame does not exist!");
		}
		int pos = frames.indexOf(atFrame);
		return addFrame(newFrame, pos + 1);
	}


	/**
	 * Adds the givenframe at the given position.
	 * If the frame is already part of the chain, it is moved to the new
	 * position.
	 */
	public ScoreFrameChain addFrame(ScoreFrame newFrame, int position)
	{
		PVector<ScoreFrame> frames = this.frames.minus(newFrame).plus(position, newFrame);
		return new ScoreFrameChain(frames);
	}
	
	
	/**
	 * Replaces the given {@link ScoreFrame} by the other given one
	 * or null to remove it.
	 * If the chain is empty then, null is returned.
	 */
	public ScoreFrameChain replaceFrame(ScoreFrame oldFrame, ScoreFrame newFrame)
	{
		PVector<ScoreFrame> oldFrames = this.frames;
		PVector<ScoreFrame> newFrames = oldFrames.replaceOrMinus(oldFrame, newFrame);
		if (oldFrames != newFrames)
		{
			if (newFrames.size() > 0)
				return new ScoreFrameChain(newFrames);
			else
				return null;
		}
		else
		{
			return this;
		}
	}


}
