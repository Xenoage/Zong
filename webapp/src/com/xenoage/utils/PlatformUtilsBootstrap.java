package com.xenoage.utils;

import com.xenoage.utils.gwt.GwtPlatformUtils;

/**
 * GWT compatible version of the {@link PlatformUtilsBootstrap} class.
 * It is required in a GWT project, otherwise the original final
 * from utils-base would be used, which will lead to a compilation
 * error because of missing Reflection in GWT.
 * 
 * @author Andreas Wenger
 */
public class PlatformUtilsBootstrap {
	
	public static void tryInit() {
		GwtPlatformUtils.init();
	}
	
}
