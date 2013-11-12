package com.xenoage.zong.musiclayout.spacing.horizontal;

import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.music.Voice;


/**
 * This class contains the spacing of one voice
 * of a measure of a single staff.
 *
 * @author Andreas Wenger
 */
public final class VoiceSpacing
{

	private final Voice voice;
	private final float interlineSpace;
	private final Vector<SpacingElement> spacingElements;


	/**
	 * Creates a {@link MeasurevoiceSpacing} for the given {@link Voice},
	 * using the given {@link SpacingElement}s.
	 */
	public VoiceSpacing(Voice voice, float interlineSpace, Vector<SpacingElement> spacingElements)
	{
		this.voice = voice;
		this.interlineSpace = interlineSpace;
		this.spacingElements = spacingElements;
	}


	/**
	 * Gets the {@link Voice} this spacing belongs to.
	 */
	public Voice getVoice()
	{
		return voice;
	}


	/**
	 * Gets the interline space in mm of this voice.
	 */
	public float getInterlineSpace()
	{
		return interlineSpace;
	}


	/**
	 * Gets the {@link SpacingElement}s of this voice.
	 */
	public Vector<SpacingElement> getSpacingElements()
	{
		return spacingElements;
	}


	/**
	 * Two voice spacings are equal, if the have the same voice,
	 * interline space and elements.
	 */
	@Override public boolean equals(Object o)
	{
		if (o instanceof VoiceSpacing) {
			VoiceSpacing vs = (VoiceSpacing) o;
			return this.voice == vs.voice && this.interlineSpace == vs.interlineSpace &&
				this.spacingElements.equals(vs.spacingElements);
		} else {
			return super.equals(o);
		}
	}


	@Override public String toString()
	{
		return "{VoiceSpacing with IS=" + interlineSpace + " and elements: " +
			this.spacingElements.toString() + "}";
	}


}
