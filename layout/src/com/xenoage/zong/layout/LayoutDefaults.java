package com.xenoage.zong.layout;

import lombok.Data;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.SymbolPoolUtils;

/**
 * Default settings within a {@link Layout}.
 * 
 * @author Andreas Wenger
 */
@Data public class LayoutDefaults {

	/** The default page formats. */
	private LayoutFormat format;
	/** The pool of musical symbols. May be null to use the default pool. */
	@MaybeNull private SymbolPool symbolPool;
	/** The default musical layout settings. */
	private LayoutSettings layoutSettings;
	

	/**
	 * Gets the {@link SymbolPool}. If unknown, the default pool is returned,
	 * so never null is returned.
	 */
	@NonNull public SymbolPool getSymbolPool() {
		return (symbolPool != null ? symbolPool : SymbolPoolUtils.getDefaultSymbolPool());
	}

}
