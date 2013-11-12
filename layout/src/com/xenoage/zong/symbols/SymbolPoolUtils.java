package com.xenoage.zong.symbols;

import com.xenoage.zong.io.symbols.SVGPathReader;


/**
 * Useful methods to work with {@link SymbolPool}s.
 * 
 * @author Andreas Wenger
 */
public class SymbolPoolUtils
{
	
	private static boolean init = false;
	private static SVGPathReader svgPathReader = null;
	private static SymbolPool defaultSymbolPool = null;
	
	
	/**
	 * Initializes this class, which must be done before creating a {@link SymbolPool}.
	 * @param svgPathReader  the {@link SVGPathReader} implementation for the
	 *                       graphics library used in this app
	 */
	public static void init(SVGPathReader svgPathReader)
	{
		SymbolPoolUtils.svgPathReader = svgPathReader;
		init = true;
	}
	
	
	/**
	 * Returns true, if this class has been initialized.
	 */
	public static boolean isInitialized()
	{
		return init;
	}
	
	
	/**
	 * Gets the {@link SVGPathReader} implementation for the
	 * graphics library used in this app.
	 */
	public static SVGPathReader getSVGPathReader()
	{
		return svgPathReader;
	}

	
	/**
	 * Returns a {@link SymbolPool}, that may
	 * be used as the default one in an application.
	 * It must be set before, otherwise it is null by default.
	 */
	public static SymbolPool getDefaultSymbolPool()
	{
		return defaultSymbolPool;
	}

	
	/**
	 * Sets a {@link SymbolPool}, that may
	 * be used as the default one in an application.
	 */
	public static void setDefaultSymbolPool(SymbolPool defaultSymbolPool)
	{
		SymbolPoolUtils.defaultSymbolPool = defaultSymbolPool;
	}
	
	
	/**
	 * Returns the given {@link SymbolPool}, if not null, otherwise
	 * the default {@link SymbolPool} defined in this class, if not null,
	 * otherwise an {@link IllegalStateException} is thrown.
	 */
	public static SymbolPool getOrDefault(SymbolPool symbolPool)
	{
		if (symbolPool != null)
			return symbolPool;
		else if (defaultSymbolPool != null)
			return defaultSymbolPool;
		else
			throw new IllegalStateException("Default SymbolPool is not set");
	}
	

}
