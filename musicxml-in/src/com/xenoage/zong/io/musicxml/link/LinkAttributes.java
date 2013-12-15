package com.xenoage.zong.io.musicxml.link;


/**
 * MusicXML link-attributes.
 * Currently, only the href attribute is supported.
 * 
 * @author Andreas Wenger
 */
public class LinkAttributes
{
	
	private final String href;
	
	
	public LinkAttributes(String href)
	{
		this.href = href;
	}
	
	
	public String getHref()
	{
		return href;
	}

}
