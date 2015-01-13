package com.xenoage.zong.io.musicxml;

import static com.xenoage.utils.collections.BiMap.biMap;

import com.xenoage.utils.collections.BiMap;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.annotation.OrnamentType;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent.MxlArticulationsContentType;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent.MxlOrnamentsContentType;
import com.xenoage.zong.musicxml.types.enums.MxlBarStyle;
import com.xenoage.zong.musicxml.types.enums.MxlClefSign;
import com.xenoage.zong.musicxml.types.enums.MxlGroupSymbolValue;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;

/**
 * Some types are very similar in Zong! and in MusicXML.
 * This class translates between them.
 * 
 * @author Andreas Wenger
 */
public class Equivalents {
	
	public static BiMap<Alignment, MxlLeftCenterRight> alignments = getAlignments();
	public static BiMap<ArticulationType, MxlArticulationsContentType> articulations = getArticulationTypes();
	public static BiMap<BarlineStyle, MxlBarStyle> barlineStyles = getBarlineStyles();
	public static BiMap<BracketGroup.Style, MxlGroupSymbolValue> bracketGroupStyles = getBracketGroupStyles();
	public static BiMap<ClefSymbol, MxlClefSign> clefSymbols = getClefSymbols();
	public static BiMap<OrnamentType, MxlOrnamentsContentType> ornaments = getOrnaments();
	

	private static BiMap<Alignment, MxlLeftCenterRight> getAlignments() {
		BiMap<Alignment, MxlLeftCenterRight> ret = biMap();
		ret.put(Alignment.Left, MxlLeftCenterRight.Left);
		ret.put(Alignment.Center, MxlLeftCenterRight.Center);
		ret.put(Alignment.Right, MxlLeftCenterRight.Right);
		ret.setDefaultValue1(Alignment.Left);
		return ret;
	}
	
	private static BiMap<ArticulationType, MxlArticulationsContentType> getArticulationTypes() {
		BiMap<ArticulationType, MxlArticulationsContentType> ret = biMap();
		ret.put(ArticulationType.Accent, MxlArticulationsContentType.Accent);
		ret.put(ArticulationType.Staccatissimo, MxlArticulationsContentType.Staccatissimo);
		ret.put(ArticulationType.Staccato, MxlArticulationsContentType.Staccato);
		ret.put(ArticulationType.Marcato, MxlArticulationsContentType.StrongAccent);
		ret.put(ArticulationType.Tenuto, MxlArticulationsContentType.Tenuto);
		return ret;
	}
	
	
	private static BiMap<BracketGroup.Style, MxlGroupSymbolValue> getBracketGroupStyles() {
		BiMap<BracketGroup.Style, MxlGroupSymbolValue> ret = biMap();
		ret.put(BracketGroup.Style.Brace, MxlGroupSymbolValue.Brace);
		ret.put(BracketGroup.Style.Bracket, MxlGroupSymbolValue.Bracket);
		ret.put(BracketGroup.Style.Line, MxlGroupSymbolValue.Line);
		ret.put(BracketGroup.Style.Square, MxlGroupSymbolValue.Square);
		ret.setDefaultValue1(BracketGroup.Style.None);
		return ret;
	}
	
	private static BiMap<BarlineStyle, MxlBarStyle> getBarlineStyles() {
		BiMap<BarlineStyle, MxlBarStyle> ret = biMap();
		ret.put(BarlineStyle.Regular, MxlBarStyle.Regular);
		ret.put(BarlineStyle.Dotted, MxlBarStyle.Dotted);
		ret.put(BarlineStyle.Dashed, MxlBarStyle.Dashed);
		ret.put(BarlineStyle.Heavy, MxlBarStyle.Heavy);
		ret.put(BarlineStyle.LightLight, MxlBarStyle.LightLight);
		ret.put(BarlineStyle.LightHeavy, MxlBarStyle.LightHeavy);
		ret.put(BarlineStyle.HeavyLight, MxlBarStyle.HeavyLight);
		ret.put(BarlineStyle.HeavyHeavy, MxlBarStyle.HeavyHeavy);
		ret.put(BarlineStyle.None, MxlBarStyle.None);
		//not supported yet: Tick and Short
		return ret;
	}
	
	private static BiMap<ClefSymbol, MxlClefSign> getClefSymbols() {
		BiMap<ClefSymbol, MxlClefSign> ret = biMap();
		ret.put(ClefSymbol.G, MxlClefSign.G);
		ret.put(ClefSymbol.F, MxlClefSign.F);
		ret.put(ClefSymbol.C, MxlClefSign.C);
		ret.put(ClefSymbol.PercTwoRects, MxlClefSign.Percussion);
		ret.put(ClefSymbol.Tab, MxlClefSign.TAB);
		ret.put(ClefSymbol.None, MxlClefSign.None);
		return ret;
	}
	
	private static BiMap<OrnamentType, MxlOrnamentsContentType> getOrnaments() {
		BiMap<OrnamentType, MxlOrnamentsContentType> ret = biMap();
		ret.put(OrnamentType.Trill, MxlOrnamentsContentType.TrillMark);
		ret.put(OrnamentType.Turn, MxlOrnamentsContentType.Turn);
		ret.put(OrnamentType.DelayedTurn, MxlOrnamentsContentType.DelayedTurn);
		ret.put(OrnamentType.InvertedTurn, MxlOrnamentsContentType.InvertedTurn);
		ret.put(OrnamentType.Mordent, MxlOrnamentsContentType.Mordent);
		ret.put(OrnamentType.InvertedMordent, MxlOrnamentsContentType.InvertedMordent);
		return ret;
	}
	
}
