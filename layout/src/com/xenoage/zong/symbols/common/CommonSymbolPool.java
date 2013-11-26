package com.xenoage.zong.symbols.common;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.Getter;

import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.WarningSymbol;

/**
 * This class manages an array of all symbols
 * that are part of the {@link CommonSymbol} enumeration,
 * allowing access to them in constant time.
 * 
 * @author Andreas Wenger
 */
public class CommonSymbolPool<Shape> {

	private List<Symbol<Shape>> symbols;
	
	/** The warning symbol. */
	@Getter private WarningSymbol<Shape> warningSymbol;


	public CommonSymbolPool(SymbolPool<Shape> pool) {
		symbols = alist();
		for (CommonSymbol commonSymbol : CommonSymbol.values()) {
			symbols.add(pool.getSymbol(commonSymbol.getID()));
		}
		warningSymbol = new WarningSymbol<Shape>();
	}

	/**
	 * Gets the symbol belonging to the given CommonSymbol.
	 */
	public Symbol<Shape> getSymbol(CommonSymbol commonSymbol) {
		if (commonSymbol.ordinal() < symbols.size()) {
			return symbols.get(commonSymbol.ordinal());
		}
		else {
			return warningSymbol;
		}
	}

}
