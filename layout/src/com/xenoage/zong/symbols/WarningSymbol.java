package com.xenoage.zong.symbols;

import com.xenoage.utils.math.geom.Rectangle2f;


/**
 * Warning symbol, that can for example be used if other symbols
 * are missing.
 * 
 * It is shown on screen, but not printed.
 *
 * @author Andreas Wenger
 */
public final class WarningSymbol
	extends Symbol
{
	
	public static final WarningSymbol instance = new WarningSymbol();
	
	
	private WarningSymbol()
	{
		super("warning", new Rectangle2f(0, 0, 0, 0), null, null, 0f, 0f);
	}
	
  
  /**
   * Gets the type of this symbol.
   */
  @Override public SymbolType getType()
  {
  	return SymbolType.WarningSymbol;
  }
  
  
}
