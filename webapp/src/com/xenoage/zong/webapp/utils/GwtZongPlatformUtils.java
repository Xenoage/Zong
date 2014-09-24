package com.xenoage.zong.webapp.utils;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.async.AsyncCallback;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.gwt.GwtPlatformUtils;
import com.xenoage.zong.io.symbols.SvgPathReader;
import com.xenoage.zong.io.symbols.SymbolPoolReader;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.util.ZongPlatformUtils;
import com.xenoage.zong.webapp.io.symbols.GwtSvgPathReader;

/**
 * GWT implementation of {@link ZongPlatformUtils}.
 * 
 * @author Andreas Wenger
 */
public class GwtZongPlatformUtils
	extends ZongPlatformUtils {

	public static final GwtZongPlatformUtils instance = new GwtZongPlatformUtils();

	private SymbolPool symbolPool;


	/**
	 * Initializes the {@link GwtZongPlatformUtils} as the {@link ZongPlatformUtils} instance
	 * and {@link GwtPlatformUtils} as the {@link PlatformUtils} instance.
	 */
	public static void init(final AsyncCallback finished) {
		GwtPlatformUtils.init();
		ZongPlatformUtils.init(instance);
		//load default symbol pool
		new SymbolPoolReader("default").produce(new AsyncResult<SymbolPool>() {

			@Override public void onSuccess(SymbolPool data) {
				finished.onSuccess();
			}

			@Override public void onFailure(Exception ex) {
				finished.onFailure(ex);
			}
		});
	}

	@Override public SymbolPool getSymbolPool() {
		return symbolPool;
	}

	@Override public SvgPathReader<?> getSvgPathReader() {
		return new GwtSvgPathReader();
	}

}
