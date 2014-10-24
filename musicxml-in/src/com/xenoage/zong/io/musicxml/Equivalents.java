package com.xenoage.zong.io.musicxml;

import static com.xenoage.utils.collections.BiMap.biMap;

import com.xenoage.utils.collections.BiMap;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.musicxml.types.enums.MxlGroupSymbolValue;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;

/**
 * Some types are very similar in Zong! and in MusicXML.
 * This class translates between them.
 * 
 * @author Andreas Wenger
 */
public class Equivalents {
	
	public static BiMap<Alignment, MxlLeftCenterRight> hAlignments = getHAlignments();
	public static BiMap<BracketGroup.Style, MxlGroupSymbolValue> bracketGroupStyles = getBracketGroupStyles();

	private static BiMap<Alignment, MxlLeftCenterRight> getHAlignments() {
		BiMap<Alignment, MxlLeftCenterRight> ret = biMap();
		ret.put(Alignment.Left, MxlLeftCenterRight.Left);
		ret.put(Alignment.Center, MxlLeftCenterRight.Center);
		ret.put(Alignment.Right, MxlLeftCenterRight.Right);
		return ret;
	}
	
	private static BiMap<BracketGroup.Style, MxlGroupSymbolValue> getBracketGroupStyles() {
		BiMap<BracketGroup.Style, MxlGroupSymbolValue> ret = biMap();
		ret.put(BracketGroup.Style.Brace, MxlGroupSymbolValue.Brace);
		ret.put(BracketGroup.Style.Bracket, MxlGroupSymbolValue.Bracket);
		ret.put(BracketGroup.Style.Line, MxlGroupSymbolValue.Line);
		ret.put(BracketGroup.Style.Square, MxlGroupSymbolValue.Square);
		return ret;
	}
}
