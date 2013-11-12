package com.xenoage.zong.musiclayout.layouter.scoreframelayout.util;

import static com.xenoage.utils.pdlib.PVector.pvec;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;


/**
 * This class remembers the last stamped lyrics for each staff and each verse.
 * 
 * @author Andreas Wenger
 */
public final class LastLyrics
{
	
	private PVector<PVector<StaffTextStamping>> lastLyrics = pvec();
	
	private static final PVector<StaffTextStamping> emptyStampings = pvec();
	
	
	/**
	 * Returns the last stamped lyric within the given staff and verse, or null if unset.
	 */
	public StaffTextStamping get(int staff, int verse)
	{
		if (staff < lastLyrics.size())
		{
			PVector<StaffTextStamping> verses = lastLyrics.get(staff);
			if (verses != null && verse < verses.size())
			{
				return verses.get(verse);
			}
		}
		return null;
	}
	
	
	/**
	 * Sets the last stamped lyric within the given staff and verse, or null if unset.
	 */
	public void set(int staff, int verse, StaffTextStamping lyric)
	{
		//ensure that staff array is big enough
		while (staff >= lastLyrics.size())
		{
			lastLyrics = lastLyrics.plus(null);
		}
		//ensure that verse array is existing and big enough
		if (lastLyrics.get(staff) == null)
		{
			lastLyrics = lastLyrics.with(staff, emptyStampings);
		}
		PVector<StaffTextStamping> verses = lastLyrics.get(staff);
		while (verse >= verses.size())
		{
			verses = verses.plus(null);
		}
		//set lyric
		verses = verses.with(verse, lyric);
		lastLyrics = lastLyrics.with(staff, verses);
	}
	

}
