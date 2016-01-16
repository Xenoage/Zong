package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.lang.VocByString.voc;

import com.xenoage.utils.annotations.Unneeded;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Zong;

/**
 * Provides additional information about the layouting process.
 * 
 * @author Andreas Wenger
 */
public class LayouterInfo {
	
	/**
	 * Gets the localized name of the given layouter strategy class, e.g.
	 * "Empty staff lines over the whole page".
	 */
	@Unneeded //maybe later, when GUI is provided
	public static String getStrategyName(Class<?> strategyClass) {
		String className = strategyClass.getName();
		if (className.startsWith(Zong.projectPackage + "."))
			className = className.substring((Zong.projectPackage + ".").length());
		return Lang.get(voc(className));
	}

}
