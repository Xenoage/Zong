package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.zong.io.musicxml.in.readers.MusicReader;
import com.xenoage.zong.io.musicxml.in.readers.MusicReaderContext;


/**
 * Exception within a {@link MusicReader}.
 * 
 * @author Andreas Wenger
 */
public final class MusicReaderException
	extends RuntimeException
{
	
	private final MusicReaderContext context;


	public MusicReaderException(String message, MusicReaderContext context)
	{
		super(message);
		this.context = context;
	}
	
	
	public MusicReaderException(Throwable cause, MusicReaderContext context)
	{
		super(cause);
		this.context = context;
	}
	
	
	public MusicReaderContext getContext()
	{
		return context;
	}

}
