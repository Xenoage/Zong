package com.xenoage.utils;

import com.xenoage.zong.webapp.utils.GwtZongPlatformUtils;


/**
 * GWT version of the {@link PlatformUtilsBootstrap} class.
 * 
 * It is required in a GWT project, otherwise the original final
 * from utils-base would be used, which will lead to a compilation
 * error because of missing Reflection in GWT.
 * 
 * However, auto-initialization in GWT is not possible, because
 * asynchronous calls are required. 
 * 
 * @author Andreas Wenger
 */
public class PlatformUtilsBootstrap {
	
	public static void tryInit() {
		throw new IllegalStateException(GwtZongPlatformUtils.class.getSimpleName() +
			" must be initialized by the user");
	}
	
}
