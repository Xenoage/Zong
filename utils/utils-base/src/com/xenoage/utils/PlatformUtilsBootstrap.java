package com.xenoage.utils;

/**
 * This class tries to init the {@link PlatformUtils} automatically.
 * 
 * Therefor it loads the class <code>{@value #bootstrapClassName}</code>
 * and call its <code>init</code> method. If not successfull, nothing
 * happens.
 * 
 * This is an own class and not part of {@link PlatformUtils}, since
 * platforms which don't support reflection (like GWT) would not compile
 * the {@link PlatformUtils} otherwise.
 * 
 * @author Andreas Wenger
 */
public class PlatformUtilsBootstrap {
	
	public static final String bootstrapClassName = "com.xenoage.utils.PlatformUtilsInit";

	public static void tryInit() {
		Class<?> cls;
		try {
			cls = Class.forName(bootstrapClassName);
			cls.getMethod("init").invoke(null);
		} catch (Exception e) {
		}
	}
	
}
