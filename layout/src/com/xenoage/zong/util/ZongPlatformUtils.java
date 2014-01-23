package com.xenoage.zong.util;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.io.symbols.SvgPathReader;

/**
 * Interface for platform dependent methods which are specific to Zong!,
 * similar to the more general {@link PlatformUtils} class.
 * 
 * Call the {@link #init(ZongPlatformUtils, PlatformUtils)} method before
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
		if (zongPlatformUtils == null)
			throw new IllegalStateException(ZongPlatformUtils.class.getName() + " not initialized");
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
	 * Gets the {@link SvgPathReader} for the current platform.
	 */
	public abstract SvgPathReader<?> getSvgPathReader();

}
