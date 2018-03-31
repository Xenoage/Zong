package com.xenoage.utils;

import com.xenoage.utils.jse.JsePlatformUtils;

/**
 * Bootstrap loader for {@link PlatformUtils}, called automatically
 * by {@link PlatformUtils#platformUtils()}.
 * 
 * @author Andreas Wenger
 */
public class PlatformUtilsInit {
	
	public static void init() {
		JsePlatformUtils.init("utils-test");
	}

}
