package com.xenoage.zong.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.xenoage.zong.musiclayout.settings.LayoutSettings.defaultLayoutSettings;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Default settings within a {@link Layout}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter public class LayoutDefaults {

	/** The default page formats. */
	@NonNull private LayoutFormat format;
	/** The pool of musical symbols. */
	@NonNull private SymbolPool symbolPool;
	/** The default musical layout settings. */
	@NonNull private LayoutSettings layoutSettings;
	
	/**
	 * Creates a {@link LayoutDefaults} with the given format and the
	 * other settings set to their default values.
	 */
	public LayoutDefaults(LayoutFormat format) {
		this.format = format;
		//use default symbol pool
		this.symbolPool = zongPlatformUtils().getSymbolPool();
		//load layout settings - TODO: load settings one time from "data/layout/default.xml"
		this.layoutSettings = defaultLayoutSettings; //LayoutSettingsReader.read("data/layout/default.xml");
	}
	
}
