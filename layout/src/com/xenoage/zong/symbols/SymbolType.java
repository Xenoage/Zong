package com.xenoage.zong.symbols;

/**
 * List of types of symbols.
 * 
 * Needed as a fast workaround for the missing
 * multiple dispatch feature in Java.
 * 
 * @author Andreas Wenger
 */
public enum SymbolType {
	/** A symbol, where the geometrical path is known. */
	PathSymbol,
	/** The warning symbol used for showing problems or missing symbols. */
	WarningSymbol
}
