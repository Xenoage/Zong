package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.musicxml.types.enums.MxlBarStyle;

/**
 * Reads a {@link BarlineStyle} from a {@link MxlBarStyle}.
 * 
 * @author Andreas Wenger
 */
public class BarlineStyleReader {

	@MaybeNull public static BarlineStyle read(MxlBarStyle mxlBarStyle) {
		switch (mxlBarStyle) {
			case Regular:
				return BarlineStyle.Regular;
			case Dotted:
				return BarlineStyle.Dotted;
			case Dashed:
				return BarlineStyle.Dashed;
			case Heavy:
				return BarlineStyle.Heavy;
			case LightLight:
				return BarlineStyle.LightLight;
			case LightHeavy:
				return BarlineStyle.LightHeavy;
			case HeavyLight:
				return BarlineStyle.HeavyLight;
			case HeavyHeavy:
				return BarlineStyle.HeavyHeavy;
			case Tick:
				return null; //not supported
			case Short:
				return null; //not supported
			case None:
				return BarlineStyle.None;
		}
		return null; //not supported
	}

}
