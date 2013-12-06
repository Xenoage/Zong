package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;

import com.xenoage.utils.base.annotations.MaybeNull;


/**
 * MusicXML up-down.
 * 
 * @author Andreas Wenger
 */
public enum MxlUpDown
{
	
	Up,
	Down;
	
	
	@MaybeNull public static MxlUpDown read(String s)
	{
		return getEnumValue(s, values());
	}
	
	
	public String write()
	{
		return toString().toLowerCase();
	}
	

}
