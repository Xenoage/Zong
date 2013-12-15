package com.xenoage.zong.io.musicxml.opus;

import com.xenoage.zong.io.musicxml.link.LinkAttributes;


/**
 * Link to a score within an {@link Opus}.
 * 
 * @author Andreas Wenger
 */
public class Score
	implements OpusItem
{
	
	private final LinkAttributes link;
	private final Boolean newPage;
	
	
	/**
	 * Creates a new {@link Score} instance.
	 * @param link     link to the XML document
	 * @param newPage  true if score begins on new page, false if not, null for undefined
	 */
	public Score(LinkAttributes link, Boolean newPage)
	{
		this.link = link;
		this.newPage = newPage;
	}
	
	
	public String getHref()
	{
		return link.getHref();
	}
	
	
	/**
	 * Returns true if score begins on new page, false if not, null for undefined.
	 */
	public Boolean isNewPage()
	{
		return newPage;
	}
	

}
