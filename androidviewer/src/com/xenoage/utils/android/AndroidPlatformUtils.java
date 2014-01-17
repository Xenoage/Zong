package com.xenoage.utils.android;

import java.io.IOException;

import android.content.res.Resources;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.android.font.AndroidTextMeasurer;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.JseInputStream;

/**
 * Android specific {@link PlatformUtils} implementation.
 * 
 * It is based on the {@link JsePlatformUtils} as far as Android
 * supports the Java SE platform.
 * 
 * @author Andreas Wenger
 */
public class AndroidPlatformUtils
	extends JsePlatformUtils {

	private Resources resources;
	private AndroidTextMeasurer textMeasurer = new AndroidTextMeasurer();


	/**
	 * Creates a {@link AndroidPlatformUtils} with the given {@link Resources}.
	 */
	public AndroidPlatformUtils(Resources resources) {
		this.resources = resources;
	}

	@Override public TextMeasurer getTextMeasurer() {
		return textMeasurer;
	}

	@Override public InputStream openFile(String filePath)
		throws IOException {
		return new JseInputStream(resources.getAssets().open(filePath));
	}

}
