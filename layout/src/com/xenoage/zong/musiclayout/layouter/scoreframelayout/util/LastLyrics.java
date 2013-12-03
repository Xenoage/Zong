package com.xenoage.zong.musiclayout.layouter.scoreframelayout.util;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;

/**
 * This class remembers the last stamped lyrics for each staff and each verse.
 * 
 * @author Andreas Wenger
 */
public final class LastLyrics {

	private ArrayList<ArrayList<StaffTextStamping>> lastLyrics = alist();

	private static final ArrayList<StaffTextStamping> emptyStampings = alist();


	/**
	 * Returns the last stamped lyric within the given staff and verse, or null if unset.
	 */
	public StaffTextStamping get(int staff, int verse) {
		if (staff < lastLyrics.size()) {
			List<StaffTextStamping> verses = lastLyrics.get(staff);
			if (verses != null && verse < verses.size()) {
				return verses.get(verse);
			}
		}
		return null;
	}

	/**
	 * Sets the last stamped lyric within the given staff and verse, or null if unset.
	 */
	public void set(int staff, int verse, StaffTextStamping lyric) {
		//ensure that staff array is big enough
		while (staff >= lastLyrics.size()) {
			lastLyrics.add(null);
		}
		//ensure that verse array is existing and big enough
		if (lastLyrics.get(staff) == null) {
			lastLyrics.set(staff, emptyStampings);
		}
		ArrayList<StaffTextStamping> verses = lastLyrics.get(staff);
		while (verse >= verses.size()) {
			verses.add(null);
		}
		//set lyric
		verses.set(verse, lyric);
		lastLyrics.set(staff, verses);
	}

}
