package com.xenoage.zong.io.symbols;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Level.Warning;
import static com.xenoage.utils.log.Report.createReport;

import java.util.List;
import java.util.Map;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.async.AsyncProducer;
import com.xenoage.utils.io.FileFilters;
import com.xenoage.utils.io.FileNotFoundException;
import com.xenoage.zong.Voc;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * This class reads a {@link SymbolPool}
 * from the filesystem of {@link PlatformUtils}.
 * 
 * @author Andreas Wenger
 */
public final class SymbolPoolReader
	implements AsyncProducer<SymbolPool> {

	/** Directory, where the symbol pool subdirectories can be found. */
	public static final String symbolPoolPath = "data/symbols/";

	//parameters
	private final String id;
	private final String dir;
	
	//state
	private AsyncResult<SymbolPool> result;
	private int symbolsCount;
	private Map<String, Symbol> symbols = map();
	private List<String> symbolsWithErrors = alist();
	
	
	/**
	 * Creates a loader for the {@link SymbolPool} with the given ID from
	 * {@value #symbolPoolPath}.
	 */
	public SymbolPoolReader(String id) {
		this.id = id;
		this.dir = symbolPoolPath + id;
	}

	@Override public void produce(AsyncResult<SymbolPool> result) {
		this.result = result;
		step1_checkDir();
	}
	
	private void step1_checkDir() {
		platformUtils().getFilesystemInput().existsDirectoryAsync(dir, new AsyncResult<Boolean>() {

			@Override public void onSuccess(Boolean data) {
				if (data) {
					step2_listFiles();
				}
				else {
					result.onFailure(new FileNotFoundException(dir));
				}
			}

			@Override public void onFailure(Exception ex) {
				result.onFailure(ex);
			}
		});
	}
	
	private void step2_listFiles() {
		platformUtils().getFilesystemInput().listFilesAsync(dir, FileFilters.svgFilter,
			new AsyncResult<List<String>>() {

				@Override public void onSuccess(List<String> files) {
					symbolsCount = files.size();
					//load files in parallel, when possible
					for (String file : files) {
						String symbolPath = "data/symbols/" + id + "/" + file;
						step3_loadSymbol(symbolPath);
					}
				}

				@Override public void onFailure(Exception ex) {
					result.onFailure(ex);
				}
		});
	}

	private void step3_loadSymbol(final String symbolPath) {
		SvgSymbolReader loader = new SvgSymbolReader(symbolPath);
		loader.produce(new AsyncResult<PathSymbol>() {

			@Override public void onSuccess(PathSymbol symbol) {
				symbols.put(symbol.getId(), symbol);
				checkFinished();
			}

			@Override public void onFailure(Exception ex) {
				symbolsWithErrors.add(symbolPath);
				checkFinished();
			}
		});
	}
	
	private void checkFinished() {
		if (symbols.size() + symbolsWithErrors.size() == symbolsCount) {
			if (symbolsWithErrors.size() > 0) {
				handle(createReport(Warning, true, Voc.CouldNotLoadSymbolPool, null, null,
					symbolsWithErrors));
			}
			result.onSuccess(new SymbolPool(id, symbols));
		}
	}

}
