package com.xenoage.zong.musicxml.util;



/**
 * Runtime exception for invalid Zong! core data.
 * 
 * @author Andreas Wenger
 */
public final class InvalidCore
	extends RuntimeException
{
	
	private final Object element;
	
	
	public InvalidCore(Object element)
	{
		this.element = element;
	}
	
	
	public static InvalidCore invalidCore(Object element)
	{
		return new InvalidCore(element);
	}
	
	
	public Object getElement()
	{
		return element;
	}

}
