package com.xenoage.zong.musiclayout.layouter.cache;

import java.util.HashMap;
import java.util.Iterator;

import com.xenoage.zong.core.music.curvedline.CurvedLine;
import com.xenoage.zong.musiclayout.layouter.cache.util.CurvedLineCache;


/**
 * Cache for curved lines (ties, slurs) which are still not
 * stamped completely.
 * 
 * @author Andreas Wenger
 */
public class OpenCurvedLinesCache
	implements Iterable<CurvedLineCache>
{
	
	//slurs and ties, which are not stamped completely yet
	private HashMap<CurvedLine, CurvedLineCache> openCurvedLines =
		new HashMap<CurvedLine, CurvedLineCache>();
	
	
	/**
	 * Adds the given {@link CurvedLineCache}.
	 */
	public void add(CurvedLineCache tiedChords)
	{
		openCurvedLines.put(tiedChords.getCurvedLine(), tiedChords);
	}
	
	
	/**
	 * Gets the given {@link CurvedLine} instance.
	 * If not existing, null is returned.
	 */
	public CurvedLineCache get(CurvedLine curvedLine)
	{
		return openCurvedLines.get(curvedLine);
	}
	
	
	/**
	 * Removes the given {@link CurvedLine} instance.
	 * If not existing, nothing happens.
	 */
	public void remove(CurvedLine curvedLine)
	{
		openCurvedLines.remove(curvedLine);
	}
	
	
	/**
	 * Gets an iterator for all open beams.
	 */
	@Override public Iterator<CurvedLineCache> iterator()
	{
		return openCurvedLines.values().iterator();
	}
	
	
}
