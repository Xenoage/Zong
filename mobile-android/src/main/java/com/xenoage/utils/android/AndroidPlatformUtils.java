package com.xenoage.utils.android;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.android.font.AndroidTextMeasurer;
import com.xenoage.utils.android.io.AndroidIO;
import com.xenoage.utils.android.xml.AndroidXmlReader;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.io.ZipReader;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.thread.ThreadUtils;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * Android specific {@link PlatformUtils} implementation.
 * 
 * @author Andreas Wenger
 */
public class AndroidPlatformUtils
	extends PlatformUtils {

	private static AndroidPlatformUtils instance = null;

	private AndroidIO androidIO = null;
	private AndroidTextMeasurer textMeasurer = new AndroidTextMeasurer();

	/**
	 * Gets the {@link AndroidPlatformUtils} instance.
	 * {@link #init(Resources)} has to be called before.
	 */
	public static AndroidPlatformUtils androidPlatformUtils() {
		assertInitialized();
		return instance;
	}

	/**
	 * Initializes the {@link PlatformUtils} class for usage within an
	 * Android environment (using an instance of {@link AndroidPlatformUtils}),
	 * using the given {@link Resources}.
	 */
	public static void init(Resources res) {
		instance = new AndroidPlatformUtils();
		instance.androidIO = new AndroidIO(res);
		PlatformUtils.init(instance);
	}

	private static void assertInitialized() {
		if (instance == null)
			throw new IllegalStateException(AndroidPlatformUtils.class.getSimpleName() +
					" not initialized");
	}

	/**
	 * Gets the {@link AndroidIO} instance.
	 * {@link #init(Resources)} has to be called before.
	 */
	public static AndroidIO io() {
		assertInitialized();
		return instance.androidIO;
	}

	@Override public List<StackTraceElement> getCurrentStackTrace() {
		return ThreadUtils.getCurrentStackTrace();
	}

	@Override public String getStackTraceString(Throwable throwable) {
		return ThreadUtils.getStackTraceString(throwable);
	}

	@Override public StackTraceElement getCaller(int level) {
		return ThreadUtils.getCaller(level + 1);
	}

	@Override public TextMeasurer getTextMeasurer() {
		return textMeasurer;
	}
	
	@Override public AndroidIO getFilesystemInput() {
		return io();
	}

	@Override public void openFileAsync(String filePath, AsyncResult<InputStream> callback) {
		try {
			InputStream stream = openFile(filePath);
			callback.onSuccess(stream);
		} catch (IOException ex) {
			callback.onFailure(ex);
		}
	}

	/**
	 * Convenience method for opening an {@link InputStream} for the file at the given relative path.
	 * This method is blocking.
	 */
	@NonNull public InputStream openFile(String filePath)
		throws IOException {
		return io().openFile(filePath);
	}
	
	@Override public XmlReader createXmlReader(InputStream inputStream) {
		return new AndroidXmlReader(new JseInputStream(inputStream));
	}

	@Override public XmlWriter createXmlWriter(OutputStream outputStream) {
		return null;
	}

	@Override public ZipReader createZipReader(InputStream inputStream) {
		return null;
	}

	@Override public void exit(Throwable ex) {
		System.exit(1); //TODO
	}

}
