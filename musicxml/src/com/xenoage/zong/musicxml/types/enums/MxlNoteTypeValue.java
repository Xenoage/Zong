package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.math.Fraction;


/**
 * MusicXML note-type-value.
 * 
 * @author Andreas Wenger
 */
public enum MxlNoteTypeValue
{
	
	_256th(fr(1, 256)),
	_128th(fr(1, 128)),
	_64th(fr(1, 64)),
	_32nd(fr(1, 32)),
	_16th(fr(1, 16)),
	Eighth(fr(1, 8)),
	Quarter(fr(1, 4)),
	Half(fr(1, 2)),
	Whole(fr(1, 1)),
	Breve(fr(1, 1)),
	Long(fr(1, 1));
	
	
	public final Fraction duration;
	
	
	private MxlNoteTypeValue(final Fraction duration)
	{
		this.duration = duration;
	}
	
	
	@MaybeNull public static MxlNoteTypeValue read(String s)
	{
		return getEnumValue(s, values());
	}
	
	
	public String write()
	{
		String ret = toString().toLowerCase();
		if (ret.charAt(0) == '_')
			ret = ret.substring(1);
		return ret;
	}

}
