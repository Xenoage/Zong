package com.xenoage.zong.io.musicxml;

import static com.xenoage.utils.collections.BiMap.biMap;

import com.xenoage.utils.collections.BiMap;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;

/**
 * Some types are very similar in Zong! and in MusicXML.
 * This class translates between them.
 * 
 * @author Andreas Wenger
 */
public class Equivalents {
	
	public static BiMap<Alignment, MxlLeftCenterRight> hAlignments = getHAlignments();

	private static BiMap<Alignment, MxlLeftCenterRight> getHAlignments() {
		BiMap<Alignment, MxlLeftCenterRight> ret = biMap();
		ret.put(Alignment.Left, MxlLeftCenterRight.Left);
		ret.put(Alignment.Center, MxlLeftCenterRight.Center);
		ret.put(Alignment.Right, MxlLeftCenterRight.Right);
		return ret;
	}
}
