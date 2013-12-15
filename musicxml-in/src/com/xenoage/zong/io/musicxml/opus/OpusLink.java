package com.xenoage.zong.io.musicxml.opus;

import com.xenoage.zong.io.musicxml.link.LinkAttributes;


/**
 * Link to another {@link Opus}.
 * 
 * @author Andreas Wenger
 */
public class OpusLink
	implements OpusItem
{
	
	private final LinkAttributes link;
	
	
	public OpusLink(LinkAttributes link)
	{
		this.link = link;
	}
	
	
	public String getHref()
	{
		return link.getHref();
	}

}
