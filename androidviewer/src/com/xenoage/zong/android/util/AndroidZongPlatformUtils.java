package com.xenoage.zong.android.util;

import android.content.res.Resources;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.android.AndroidPlatformUtils;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.zong.io.symbols.AndroidSvgPathReader;
import com.xenoage.zong.io.symbols.SvgPathReader;
import com.xenoage.zong.util.ZongPlatformUtils;

/**
 * Android implementation of {@link ZongPlatformUtils}.
 * 
 * @author Andreas Wenger
 */
public class AndroidZongPlatformUtils
	extends ZongPlatformUtils {
	
	public static final AndroidZongPlatformUtils instance = new AndroidZongPlatformUtils();
	
	/**
	 * Initializes the {@link AndroidZongPlatformUtils} as the {@link ZongPlatformUtils} instance
	 * and {@link JsePlatformUtils} as the {@link PlatformUtils} instance with the given {@link Resources}.
	 */
	public static void init(Resources resources) {
		AndroidPlatformUtils.init(resources);
		ZongPlatformUtils.init(instance);
	}

	@Override public SvgPathReader<?> getSvgPathReader() {
		return new AndroidSvgPathReader();
	}

}
