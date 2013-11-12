package com.xenoage.zong.musiclayout.notations.chord;


/**
 * Vertical position of a chord stem.
 * 
 * @author Andreas Wenger
 */
public final class StemAlignment
{
  
	private final float startLinePosition;
	private final float endLinePosition;
	
	
	/**
	 * Creates a new ChordStemAlignment.
	 * @param startLinePosition  the line position where the stem stars (note side)
	 * @param endLinePosition    the line position where the stem ends (flag/beam side)
	 */
	public StemAlignment(float startLinePosition, float endLinePosition)
	{
		this.startLinePosition = startLinePosition;
		this.endLinePosition = endLinePosition;
	}


	/**
	 * Gets the line position where the stem starts.
	 * This is the side where the last note is applied.
	 */
	public float getStartLinePosition()
	{
		return startLinePosition;
	}
	
	
	/**
	 * Gets the line position where the stem ends.
	 * This is the side where the beam or flag is applied,
	 * if available.
	 */
	public float getEndLinePosition()
	{
		return endLinePosition;
	}
	

}
