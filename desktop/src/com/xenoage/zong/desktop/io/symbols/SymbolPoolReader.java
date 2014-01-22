package com.xenoage.zong.desktop.io.symbols;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.jse.io.DesktopIO.desktopIO;
import static com.xenoage.utils.log.Level.Warning;
import static com.xenoage.utils.log.Report.createReport;
import static com.xenoage.utils.log.Report.fatal;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.xenoage.utils.io.FileFilters;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.zong.Voc;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * This class reads a {@link SymbolPool} from a given {@link FilesystemInput}.
 * 
 * @author Andreas Wenger
 */
public class SymbolPoolReader {

	/** Directory, where the symbol pool subdirectories can be found. */
	public static final String symbolPoolPath = "data/symbols/";


	/**
	 * Loads and returns the {@link SymbolPool} with the given ID from
	 * {@value symbolPoolPath} or reports an error if not possible.
	 */
	public static SymbolPool readSymbolPool(String id, FilesystemInput filesystem) {
		
		HashMap<String, Symbol> symbols = map();

		//load symbols
		String dir = symbolPoolPath + id;
		if (!desktopIO().existsDirectory(dir)) {
			handle(fatal(Voc.CouldNotLoadSymbolPool, new FileNotFoundException(dir)));
		}
		try {
			List<String> files = filesystem.listFiles(dir, FileFilters.svgFilter);
			SvgSymbolReader loader = new SvgSymbolReader(); //GOON
			LinkedList<String> symbolsWithErrors = new LinkedList<String>();
			for (String file : files) {
				try {
					String symbolPath = "data/symbols/" + id + "/" + file;
					Symbol symbol = loader.loadSymbol(symbolPath);
					symbols.put(symbol.getId(), symbol);
				} catch (IllegalStateException ex) {
					symbolsWithErrors.add(file);
				}
			}
			if (symbolsWithErrors.size() > 0) {
				handle(createReport(Warning, true, Voc.CouldNotLoadSymbolPool, null, null,
					symbolsWithErrors));
			}
		} catch (Exception ex) {
			handle(fatal(Voc.CouldNotLoadSymbolPool, ex));
		}

		return new SymbolPool(id, symbols);
	}

}
