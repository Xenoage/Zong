package com.xenoage.zong.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
	
}
