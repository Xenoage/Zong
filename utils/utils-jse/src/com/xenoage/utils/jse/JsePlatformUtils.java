package com.xenoage.utils.jse;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.io.ZipReader;
import com.xenoage.utils.jse.font.AwtTextMeasurer;
import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.jse.io.JseZipReader;
import com.xenoage.utils.jse.thread.ThreadUtils;
import com.xenoage.utils.jse.xml.JseXmlReader;
import com.xenoage.utils.jse.xml.JseXmlWriter;
import com.xenoage.utils.promise.Executor;
import com.xenoage.utils.promise.Promise;
import com.xenoage.utils.promise.Return;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

import java.io.IOException;
import java.util.List;

/**
 * Java SE specific {@link PlatformUtils} implementation.
 * 
 * @author Andreas Wenger
 */
public class JsePlatformUtils
	extends PlatformUtils {

	private static JsePlatformUtils instance = null;

	private DesktopIO io = null;
	private AwtTextMeasurer textMeasurer = new AwtTextMeasurer();
	
	/**
	 * Gets the {@link JsePlatformUtils} instance.
	 * If not initialized yet, it is initialized for the testing environment.
	 */
	public static JsePlatformUtils jsePlatformUtils() {
		if (instance == null)
			initForTest();
		return instance;
	}
	
	/**
	 * Initializes the {@link PlatformUtils} class for usage within a
	 * desktop Java SE environment (using an instance of {@link JsePlatformUtils}),
	 * using the given program name.
	 */
	public static void init(String programName) {
		instance = new JsePlatformUtils();
		instance.io = new DesktopIO(programName);
		PlatformUtils.init(instance);
	}
	
	/**
	 * Initializes the {@link PlatformUtils} class with an instance of {@link JsePlatformUtils}
	 * for testing (e.g. unit tests).
	 * See {@link DesktopIO#createTestIO()} for details about the filesystem.
	 */
	public static void initForTest() {
		instance = new JsePlatformUtils();
		instance.io = DesktopIO.createTestIO();
		PlatformUtils.init(instance);
	}
	
	/**
	 * Gets the {@link DesktopIO} instance.
	 * If the {@link JsePlatformUtils} are not initialized yet,
	 * they are initialized for testing (e.g. unit tests).
	 */
	public static DesktopIO io() {
		if (instance == null)
			initForTest();
		return instance.io;
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
	
	@Override public DesktopIO getFilesystemInput() {
		return io;
	}

	@Override public void openFileAsync(String filePath, AsyncResult<InputStream> callback) {
		try {
			InputStream stream = openFile(filePath);
			callback.onSuccess(stream);
		} catch (IOException ex) {
			callback.onFailure(ex);
		}
	}

	@Override public Promise<InputStream> openFileAsync(final String filePath) {
		return new Promise<>(ret -> openFileAsync(filePath, new AsyncResult<InputStream>() {
			@Override public void onSuccess(InputStream data) {
				ret.resolve(data);
			}
			@Override public void onFailure(Exception ex) {
				ret.reject(ex);
			}
		}));
	}

	/**
	 * Convenience method for opening an {@link InputStream} for the file at the given relative path.
	 * This method is blocking.
	 */
	@NonNull public JseInputStream openFile(String filePath)
		throws IOException {
		return io().openFile(filePath);
	}

	@Override public XmlReader createXmlReader(InputStream inputStream) {
		return new JseXmlReader(new JseInputStream(inputStream));
	}

	@Override public XmlWriter createXmlWriter(OutputStream outputStream) {
		return new JseXmlWriter(new JseOutputStream(outputStream));
	}

	@Override public ZipReader createZipReader(InputStream inputStream)
		throws IOException {
		return new JseZipReader(inputStream);
	}

	@Override public void exit(Throwable ex) {
		System.exit(1);
	}

}
