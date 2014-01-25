package com.xenoage.zong.desktop.util;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.zong.desktop.io.symbols.AwtSvgPathReader;
import com.xenoage.zong.desktop.io.symbols.SymbolPoolReader;
import com.xenoage.zong.io.symbols.SvgPathReader;
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
		instance.symbolPool = SymbolPoolReader.readSymbolPool("default");
	}

	@Override public SymbolPool getSymbolPool() {
		return symbolPool;
	}

	@Override public SvgPathReader<?> getSvgPathReader() {
		return new AwtSvgPathReader();
	}

}
