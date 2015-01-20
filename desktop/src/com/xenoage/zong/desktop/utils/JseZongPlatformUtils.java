package com.xenoage.zong.desktop.utils;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.jse.async.Sync.sync;
import static com.xenoage.utils.log.Report.fatal;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.zong.Voc;
import com.xenoage.zong.io.symbols.SymbolPoolReader;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.util.ZongPlatformUtils;

/**
 * Java SE implementation of {@link ZongPlatformUtils}.
 * 
 * @author Andreas Wenger
 */
public class JseZongPlatformUtils
	extends ZongPlatformUtils {

	public static final JseZongPlatformUtils instance = new JseZongPlatformUtils();

	private SymbolPool symbolPool;


	/**
	 * Initializes the {@link JseZongPlatformUtils} as the {@link ZongPlatformUtils} instance
	 * and {@link JsePlatformUtils} as the {@link PlatformUtils} instance with the given program name.
	 */
	public static void init(String programName) {
		JsePlatformUtils.init(programName);
		ZongPlatformUtils.init(instance);
		//load default symbol pool
		try {
			instance.symbolPool = sync(new SymbolPoolReader("default"));
		} catch (Exception ex) {
			handle(fatal(Voc.CouldNotLoadSymbolPool, ex));
		}
	}

	@Override public SymbolPool getSymbolPool() {
		return symbolPool;
	}

}
