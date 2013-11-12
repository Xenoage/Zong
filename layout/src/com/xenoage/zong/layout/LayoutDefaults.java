package com.xenoage.zong.layout;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.SymbolPoolUtils;


/**
 * Default settings for a {@link Layout}.
 * 
 * @author Andreas Wenger
 */
public final class LayoutDefaults
{
	
	private final LayoutFormat format;
	private final SymbolPool symbolPool;
	private final LayoutSettings layoutSettings;


	/**
	 * Creates new default layout settings.
	 * @param format          the default page formats
	 * @param symbolPool      the pool of musical symbols. May be null to use the default pool.
	 * @param layoutSettings  the default musical layout settings
	 */
	public LayoutDefaults(LayoutFormat format, @MaybeNull SymbolPool symbolPool,
		LayoutSettings layoutSettings)
	{
		this.format = format;
		this.symbolPool = symbolPool;
		this.layoutSettings = layoutSettings;
	}
	

	public LayoutFormat getFormat()
	{
		return format;
	}


	
	public LayoutSettings getLayoutSettings()
	{
		return layoutSettings;
	}


	/**
	 * Gets the {@link SymbolPool}. If unknown, the default pool is returned,
	 * so never null is returned.
	 */
	@NeverNull public SymbolPool getSymbolPool()
	{
		return (symbolPool != null ? symbolPool : SymbolPoolUtils.getDefaultSymbolPool());
	}
	
	
	public LayoutDefaults withFormat(LayoutFormat format)
	{
		return new LayoutDefaults(format, symbolPool, layoutSettings);
	}

}
