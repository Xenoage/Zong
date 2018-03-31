package com.xenoage.utils;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.io.ZipReader;
import com.xenoage.utils.promise.Promise;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

import java.io.IOException;
import java.util.List;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;

/**
 * General methods which implementation is platform dependent.
 * 
 * At the beginning of the startup of an application, call the
 * {@link #init(PlatformUtils)} method, using the
 * implementation of the current platform. If this is not done,
 * this class will call the {@link PlatformUtilsBootstrap} class
 * to try to init itself.
 * 
 * @author Andreas Wenger
 */
public abstract class PlatformUtils {

	private static PlatformUtils platformUtils = null;
	
	/**
	 * Gets the current {@link PlatformUtils}.
	 */
	@NonNull public static PlatformUtils platformUtils() {
		if (platformUtils == null) {
			//use bootstrap helper class to init this class
			PlatformUtilsBootstrap.tryInit();
			//successfull?
			if (platformUtils == null) {
				throw new IllegalStateException(PlatformUtils.class.getName() + " not initialized");
			}
		}
		return platformUtils;
	}
	
	/**
	 * Initializes this class with the given platform-specific implementation.
	 * Do not call this method directly. Use the <code>init</code> methods of the specific implementations.
	 */
	public static void init(PlatformUtils platformUtils) {
		checkArgsNotNull(platformUtils);
		PlatformUtils.platformUtils = platformUtils;
	}
	
	/**
	 * Gets the current stack trace.
	 * If this platform is not able to retrieve a stack trace, null is returned.
	 */
	public abstract List<StackTraceElement> getCurrentStackTrace();
	
	/**
	 * Gets the stack trace of the given Throwable as a string.
	 * If this platform is not able to retrieve it, null is returned.
	 */
	public abstract String getStackTraceString(Throwable throwable);
	
	/**
	 * Gets the caller of a method. If unknown, null is returned.
	 * @param level       The level of the caller: 1 = the caller, 2 = the caller of the caller, ...
	 *                    For example, when A.a() calls B.b(), and in B.b() you want to know who
	 *                    called B.b(), use getCaller(1).
	 */
	public abstract StackTraceElement getCaller(int level);
	
	/**
	 * Returns the {@link TextMeasurer}.
	 */
	@NonNull public abstract TextMeasurer getTextMeasurer();
	
	/**
	 * Gets the input filesystem for this platform.
	 */
	@NonNull public abstract FilesystemInput getFilesystemInput();
	
	/**
	 * Convenience method for asynchronous opening of an {@link InputStream} for the file
	 * at the given relative path.
	 * Only asynchronous file reading is supported by all platforms. Specific platform implementations
	 * may provide also blocking methods for file reading.
	 */
	@NonNull public abstract void openFileAsync(String filePath, AsyncResult<InputStream> result);

	/**
	 * Like {@link #openFileAsync(String, AsyncResult)}, but realized using a {@link Promise}.
	 */
	@NonNull public abstract Promise<InputStream> openFileAsync(String filePath);
	
	/**
	 * Returns an {@link XmlReader} for the given {@link InputStream} for this platform.
	 */
	@NonNull public abstract XmlReader createXmlReader(InputStream inputStream);
	
	/**
	 * Returns an {@link XmlWriter} for the given {@link OutputStream} for this platform.
	 * If this platform is not able to retrieve it, null is returned.
	 */
	@NonNull public abstract XmlWriter createXmlWriter(OutputStream outputStream);
	
	/**
	 * Returns an {@link ZipReader} for the given {@link InputStream} for this platform.
	 * If this platform is not able to retrieve it, e.g. because it does not exist, null is returned.
	 */
	@NonNull public abstract ZipReader createZipReader(InputStream inputStream)
		throws IOException;
	
	/**
	 * Exits the program immediately because of an error (like System.exit(1) in the JRE).
	 * @param error  the error with more information, or null
	 */
	public abstract void exit(Throwable error);
	
}
