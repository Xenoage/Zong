package com.xenoage.zong.android.util;

import android.content.res.Resources;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.android.AndroidPlatformUtils;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.Voc;
import com.xenoage.zong.io.symbols.SvgPathReader;
import com.xenoage.zong.io.symbols.SymbolPoolReader;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.util.ZongPlatformUtils;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.jse.async.Sync.sync;
import static com.xenoage.utils.log.Report.fatal;

/**
 * Android implementation of {@link ZongPlatformUtils}.
 * 
 * @author Andreas Wenger
 */
public class AndroidZongPlatformUtils
	extends ZongPlatformUtils {
	
	public static final AndroidZongPlatformUtils instance = new AndroidZongPlatformUtils();
	
	private SymbolPool symbolPool = null;
	
	
	/**
	 * Initializes the {@link AndroidZongPlatformUtils} as the {@link ZongPlatformUtils} instance
	 * with the given {@link Resources}.
	 */
	public static void init(Resources resources) {
		AndroidPlatformUtils.init(resources);
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
