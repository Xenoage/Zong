package com.xenoage.zong.musiclayout.layouter.cache;

import static com.xenoage.utils.base.collections.CollectionUtils.map;

import java.util.HashMap;

import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.Notation;


/**
 * Cache for already computed {@link Notation}s.
 * For performance reasons, this cache is mutable.
 * 
 * A {@link Notation} is queried by the {@link MusicElement} it belongs
 * to. Also a staff index may be given, because for {@link ColumnElement}s
 * the same element is found on all staves but with different notations.
 * 
 * When querying for an element, the staff index can also be provided. If
 * nothing can be found at the given staff, the staff-less cache is used.
 * But if an element is registered with a staff but searched for without
 * a staff, it won't be found.
 * 
 * @author Andreas Wenger
 */
public final class NotationsCache
{
	
	private static class StaffIndex
	{
		public final MusicElement element;
		public final int staff;
		
		public StaffIndex(MusicElement element, int staff)
		{
			this.element = element;
			this.staff = staff;
		}

		@Override public int hashCode()
		{
			return element.hashCode() + 31 * staff;
		}

		@Override public boolean equals(Object o)
		{
			if (o instanceof StaffIndex)
			{
				StaffIndex idx = (StaffIndex) o;
				return element == idx.element && staff == idx.staff;
			}
			return false;
		}
		
	}
	
	private HashMap<Object, Notation> cache;
	
	
	public static NotationsCache create()
	{
		return new NotationsCache();
	}
	
	
	private NotationsCache()
	{
		this.cache = map();
	}
	
	
	/**
	 * Adds the given {@link Notation}.
	 * If already there, it is replaced. Use this method only for elements which appear
	 * only on a single staff (like chords or clefs). If they may appear on multiple
	 * staves (like time signatures), use {@link #add(Notation, int)} instead.
	 */
	public void add(Notation notation)
	{
		cache.put(notation.getMusicElement(), notation);
	}
	
	
	/**
	 * Adds the given {@link Notation}, that belongs to the given and staff.
	 * If already there, it is replaced.
	 */
	public void add(Notation notation, int staff)
	{
		cache.put(new StaffIndex(notation.getMusicElement(), staff), notation);
	}
	
	
	/**
	 * Adds the elements of the given {@link StaffNotationCache}.
	 * If elements are already there, they are replaced.
	 */
	public void merge(NotationsCache cache)
	{
		this.cache.putAll(cache.cache);
	}
	
	
	/**
	 * Gets the {@link Notation}, that belongs to the given {@link MusicElement},
	 * or null if unknown. It will also return null if the element was registered
	 * with a staff before. See {@link #add(Notation, MusicElement)}.
	 */
	public Notation get(MusicElement element)
	{
		return cache.get(element);
	}
	
	
	/**
	 * Gets the {@link Notation}, that belongs to the given {@link MusicElement}
	 * and staff index. If it is not found there, the staff-less cache is searched.
	 * If still not found, null is returned.
	 */
	public Notation get(MusicElement element, int staff)
	{
		Notation ret = cache.get(new StaffIndex(element, staff));
		if (ret == null)
			ret = cache.get(element);
		return ret;
	}
	
	
	/**
	 * Gets the {@link ChordNotation}, that belongs to the given {@link Chord},
	 * or null if unknown.
	 */
	public ChordNotation getChord(Chord chord)
	{
		return (ChordNotation) get(chord);
	}
	
	
	public int size()
	{
		return cache.size();
	}
	
	
	@Override public String toString()
	{
		return "[" + getClass().getSimpleName() + " with " + cache.size() + " elements]";
	}

}
