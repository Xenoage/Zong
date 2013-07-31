package com.xenoage.zong.core.util;

import com.xenoage.zong.core.Score;


/**
 * This exception is used when there is a situation which would
 * lead to an inconsistent {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class InconsistentScoreException
	extends RuntimeException
{
	
	public InconsistentScoreException(String message)
	{
		super(message);
	}

}
