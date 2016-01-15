package com.xenoage.zong.util;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Interface for platform dependent methods which are specific to Zong!,
 * similar to the more general {@link PlatformUtils} class.
 * 
 * Call the {@link #init(ZongPlatformUtils)} method before
 * using the other methods of this class.
 * 
 * @author Andreas Wenger
 */
public abstract class ZongPlatformUtils {
	
	private static ZongPlatformUtils zongPlatformUtils = null;
	
	/**
	 * Gets the current {@link ZongPlatformUtils}.
	 */
	@NonNull public static ZongPlatformUtils zongPlatformUtils() {
		if (zongPlatformUtils == null) {
			Err.handle(Report.fatal(ZongPlatformUtils.class.getName() + " not initialized"));
			throw new IllegalStateException(ZongPlatformUtils.class.getName() + " not initialized");
		}
		return zongPlatformUtils;
	}
	
	/**
	 * Initializes this class with the given platform-specific implementations.
	 */
	public static void init(ZongPlatformUtils zongPlatformUtils) {
		checkArgsNotNull(zongPlatformUtils);
		ZongPlatformUtils.zongPlatformUtils = zongPlatformUtils;
	}
	
	/**
	 * Gets the default SymbolPool.
	 */
	public abstract SymbolPool getSymbolPool();

}
