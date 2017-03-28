package com.xenoage.utils.android;

import java.io.IOException;
import java.util.List;

import android.content.Context;
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
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.thread.ThreadUtils;
import com.xenoage.utils.promise.Promise;
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

	private Context context;

	/**
	 * Gets the {@link AndroidPlatformUtils} instance.
	 * {@link #init(Context)} has to be called before.
	 */
	public static AndroidPlatformUtils androidPlatformUtils() {
		assertInitialized();
		return instance;
	}

	/**
	 * Initializes the {@link PlatformUtils} class for usage within an
	 * Android environment (using an instance of {@link AndroidPlatformUtils}),
	 * using the given {@link Context}.
	 */
	public static void init(Context context) {
		instance = new AndroidPlatformUtils();
		instance.context = context;
		instance.androidIO = new AndroidIO(context.getResources());
		PlatformUtils.init(instance);
	}

	private static void assertInitialized() {
		if (instance == null)
			throw new IllegalStateException(AndroidPlatformUtils.class.getSimpleName() +
					" not initialized");
	}

	/**
	 * Gets the {@link AndroidIO} instance.
	 * {@link #init(Context)} has to be called before.
	 */
	public static AndroidIO io() {
		assertInitialized();
		return instance.androidIO;
	}

	/**
	 * Gets a global {@link Context} instance.
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Gets the app's resources.
	 */
	public Resources getResources() {
		return context.getResources();
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

	@Override public void openFileAsync(String filePath, AsyncResult<InputStream> result) {
		try {
			InputStream stream = openFile(filePath);
			result.onSuccess(stream);
		} catch (IOException ex) {
			result.onFailure(ex);
		}
	}

	@Override public Promise<InputStream> openFileAsync(String filePath) {
		return new Promise<InputStream>((ret) -> {
			try {
				InputStream stream = openFile(filePath);
				ret.resolve(stream);
			} catch (IOException ex) {
				ret.reject(ex);
			}
		});
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
