package com.xenoage.utils;

import com.xenoage.zong.Zong;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;

/**
 * Bootstrap loader for {@link PlatformUtils}, called automatically
 * by {@link PlatformUtils#platformUtils()}.
 * 
 * @author Andreas Wenger
 */
public class PlatformUtilsInit {
	
	public static void init() {
		JseZongPlatformUtils.init(Zong.projectPackage + "-test");
	}

}
