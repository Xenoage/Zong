package com.xenoage.zong.symbols;

import static com.xenoage.utils.base.collections.CollectionUtils.map;
import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Level.Warning;
import static com.xenoage.utils.log.Report.createReport;
import static com.xenoage.utils.log.Report.fatal;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.IO;
import com.xenoage.zong.Voc;
import com.xenoage.zong.io.symbols.SVGPathReader;
import com.xenoage.zong.io.symbols.SVGSymbolReader;
import com.xenoage.zong.symbols.common.CommonSymbol;
import com.xenoage.zong.symbols.common.CommonSymbolPool;


/**
 * A {@link SymbolPool} stores and provides all
 * available symbols of a given style.
 *
 * @author Andreas Wenger
 */
public final class SymbolPool
{

	public final String id;

	private HashMap<String, Symbol> symbols = map();
	//special pool for very fast access
	private CommonSymbolPool commonSymbolPool = new CommonSymbolPool(this);
	
	
	/**
	 * Loads and returns the default symbol pool or reports an
	 * error if not possible.
	 * Before a {@link SymbolPool} can be used, the {@link SymbolPoolUtils} class
	 * must have been initialized.
	 */
	public SymbolPool()
	{
		this("default");
	}
	
	
	/**
	 * Loads and returns the symbol pool with the given ID or reports an
	 * error if not possible.
	 */
	public SymbolPool(String id)
	{
		if (SymbolPoolUtils.isInitialized() == false)
			throw new IllegalStateException(SVGPathReader.class.getSimpleName() + " is not initialized");
		
		this.id = id;
		HashMap<String, Symbol> symbols = map();

		//load symbols from data/symbols/<id>/
		String dir = "data/symbols/" + id;
		if (!IO.existsDataDirectory(dir)) {
			handle(fatal(Voc.CouldNotLoadSymbolPool, new FileNotFoundException(dir)));
		}
		try {
			Set<String> files = IO.listDataFiles(dir, FileUtils.getSVGFilter());
			SVGSymbolReader loader = new SVGSymbolReader();
			LinkedList<String> symbolsWithErrors = new LinkedList<String>();
			for (String file : files) {
				try {
					String symbolPath = "data/symbols/" + id + "/" + file;
					Symbol symbol = loader.loadSymbol(symbolPath);
					symbols.put(symbol.id, symbol);
				} catch (IllegalStateException ex) {
					symbolsWithErrors.add(file);
				}
			}
			if (symbolsWithErrors.size() > 0) {
				handle(createReport(Warning, true, Voc.CouldNotLoadSymbolPool, null, null, symbolsWithErrors));
			}
		} catch (Exception ex) {
			handle(fatal(Voc.CouldNotLoadSymbolPool, ex));
		}

		this.setSymbols(symbols);
	}


	/**
	 * Sets the symbols.
	 */
	public void setSymbols(HashMap<String, Symbol> symbols)
	{
		this.symbols = symbols;
		this.commonSymbolPool = new CommonSymbolPool(this);
	}


	public HashMap<String, Symbol> getSymbols()
	{
		return symbols;
	}


	/**
	 * Gets the symbol with the given ID, or null if not found.
	 * TODO: if the symbol is not found, return a warning symbol!
	 */
	public Symbol getSymbol(String id)
	{
		return symbols.get(id);
	}


	/**
	 * Gets the given common symbol in constant time.
	 * If the symbol is not found, a warning symbol is returned.
	 */
	public Symbol getSymbol(CommonSymbol commonSymbol)
	{
		Symbol ret = commonSymbolPool.getSymbol(commonSymbol);
		if (ret == null)
			ret = WarningSymbol.instance;
		return ret;
	}


	/**
	 * Computes and returns the width of the given number, e.g. 1733.
	 * This is the sum of the widths of all digits and additional
	 * gaps between them.
	 * @param number  the number, e.g. 0, 5 or 2738.
	 * @param gap     the gap between the digits in interline spaces.
	 */
	public float computeNumberWidth(int number, float gap)
	{
		float ret = 0;
		String s = Integer.toString(number);
		for (int i = 0; i < s.length(); i++) {
			char d = s.charAt(i);
			Symbol symbol = getSymbol("digit-" + d);
			if (symbol != null) {
				ret += symbol.boundingRect.size.width;
				if (i < s.length() - 1)
					ret += gap;
			}
		}
		return ret;
	}

}
