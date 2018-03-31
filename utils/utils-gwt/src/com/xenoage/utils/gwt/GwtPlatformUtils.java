package com.xenoage.utils.gwt;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.gwt.font.GwtTextMeasurer;
import com.xenoage.utils.gwt.io.GwtIO;
import com.xenoage.utils.gwt.io.GwtInputStream;
import com.xenoage.utils.gwt.xml.GwtXmlReader;
import com.xenoage.utils.io.*;
import com.xenoage.utils.promise.Promise;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

import java.util.List;

/**
 * Google Web Toolkit specific {@link PlatformUtils} implementation.
 * 
 * @author Andreas Wenger
 */
public class GwtPlatformUtils
	extends PlatformUtils {

	private static GwtPlatformUtils instance = null;

	private GwtIO gwtIO = null;
	private TextMeasurer textMeasurer = new GwtTextMeasurer();
	
	/**
	 * Initializes the {@link PlatformUtils} class for usage within a
	 * GWT environment (using an instance of {@link GwtPlatformUtils}).
	 */
	public static void init() {
		instance = new GwtPlatformUtils();
		instance.gwtIO = new GwtIO();
		PlatformUtils.init(instance);
	}
	
	/**
	 * Gets the {@link GwtIO} instance.
	 * If the {@link GwtPlatformUtils} are not initialized yet,
	 * they are initialized.
	 */
	public static GwtIO gwtIO() {
		if (instance == null)
			init();
		return instance.gwtIO;
	}

	/**
	 * Unsupported in GWT. Returns null.
	 */
	@Override public List<StackTraceElement> getCurrentStackTrace() {
		return null;
	}

	/**
	 * Unsupported in GWT. Returns null.
	 */
	@Override public String getStackTraceString(Throwable throwable) {
		return null;
	}

	/**
	 * Unsupported in GWT. Returns null.
	 */
	@Override public StackTraceElement getCaller(int level) {
		return null;
	}

	@Override public TextMeasurer getTextMeasurer() {
		return textMeasurer;
	}
	
	@Override public FilesystemInput getFilesystemInput() {
		return gwtIO;
	}

	@Override public void openFileAsync(String filePath, AsyncResult<InputStream> callback) {
		gwtIO().openFileAsync(filePath, callback); 
	}

	@Override public Promise<InputStream> openFileAsync(String filePath) {
		return new Promise<>(ret -> gwtIO().openFileAsync(filePath, new AsyncResult<InputStream>() {
			@Override public void onSuccess(InputStream data) {
				ret.resolve(data);
			}

			@Override public void onFailure(Exception ex) {
				ret.reject(ex);
			}
		}));
	}

	/**
	 * Supports only instances of {@link GwtInputStream} and {@link BufferedInputStream}
	 * backed by a {@link GwtInputStream}.
	 */
	@Override public XmlReader createXmlReader(InputStream inputStream) {
		if (inputStream instanceof GwtInputStream) {
			GwtInputStream stream = (GwtInputStream) inputStream;
			return new GwtXmlReader(stream.getData());
		}
		else if (inputStream instanceof BufferedInputStream) {
			return createXmlReader(((BufferedInputStream) inputStream).getInternalStream());
		}
		throw new IllegalArgumentException("Unsupported InputStream implementation");
	}

	/**
	 * Unsupported in GWT. Returns null.
	 */
	@Override public XmlWriter createXmlWriter(OutputStream outputStream) {
		return null;
	}

	/**
	 * Unsupported in GWT. Returns null.
	 */
	@Override public ZipReader createZipReader(InputStream inputStream) {
		return null;
	}

	@Override public void exit(Throwable ex) {
		//we throw an error, to stop further execution immediately
		throw new Error("Fatal error", ex);
	}

	/**
	 * Calls console.log with the given message.
	 */
	public static native void consoleLog(Object message) /*-{
      console.log(message);
  }-*/;

}
