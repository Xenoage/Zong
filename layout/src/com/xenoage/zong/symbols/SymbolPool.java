package com.xenoage.zong.symbols;

import java.util.Map;

import lombok.Getter;

import com.xenoage.zong.symbols.common.CommonSymbol;
import com.xenoage.zong.symbols.common.CommonSymbolPool;

/**
 * A {@link SymbolPool} stores and provides all
 * available symbols of a given style.
 *
 * @author Andreas Wenger
 */
public final class SymbolPool<Shape> {

	/** The id of this symbol pool. */
	@Getter private final String id;

	//the symbols in this pool
	private Map<String, Symbol<Shape>> symbols;

	//special pool for very fast access
	private CommonSymbolPool<Shape> commonSymbolPool;


	/**
	 * Creates a {@link SymbolPool} with the given ID and symbols.
	 */
	public SymbolPool(String id, Map<String, Symbol<Shape>> symbols) {
		this.id = id;
		this.symbols = symbols;
		this.commonSymbolPool = new CommonSymbolPool<Shape>(this);
	}

	/**
	 * Gets the symbol with the given ID.
	 * If the symbol is not found, a warning symbol is returned.
	 */
	public Symbol<Shape> getSymbol(String id) {
		Symbol<Shape> ret = symbols.get(id);
		if (ret == null)
			ret = commonSymbolPool.getWarningSymbol();
		return ret;
	}

	/**
	 * Gets the given common symbol in constant time.
	 * If the symbol is not found, a warning symbol is returned.
	 */
	public Symbol<Shape> getSymbol(CommonSymbol commonSymbol) {
		Symbol<Shape> ret = commonSymbolPool.getSymbol(commonSymbol);
		if (ret == null)
			ret = commonSymbolPool.getWarningSymbol();
		return ret;
	}

	/**
	 * Computes and returns the width of the given number, e.g. 1733.
	 * This is the sum of the widths of all digits and additional
	 * gaps between them.
	 * @param number  the number, e.g. 0, 5 or 2738.
	 * @param gap     the gap between the digits in interline spaces.
	 */
	public float computeNumberWidth(int number, float gap) {
		float ret = 0;
		String s = Integer.toString(number);
		int len = s.length();
		for (int i = 0; i < len; i++) {
			char d = s.charAt(i);
			Symbol<Shape> symbol = getSymbol("digit-" + d);
			if (symbol != null) {
				ret += symbol.boundingRect.size.width;
				if (i < len - 1)
					ret += gap;
			}
		}
		return ret;
	}

}
