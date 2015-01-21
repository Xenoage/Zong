package com.xenoage.zong.symbols;

import lombok.Getter;

import com.xenoage.zong.symbols.path.Path;

/**
 * Symbols which consist of a complex path.
 *
 * @author Andreas Wenger
 */
public final class PathSymbol
	extends Symbol {
	
	/** The geometric path describing this symbol. */
	@Getter public final Path path;


	public PathSymbol(String id, Path path, Float baselineOffset,
		Float ascentHeight, Float leftBorder, Float rightBorder) {
		super(id, path.getBounds(), baselineOffset, ascentHeight, leftBorder, rightBorder);
		this.path = path;
	}

	@Override public SymbolType getType() {
		return SymbolType.PathSymbol;
	}

}
