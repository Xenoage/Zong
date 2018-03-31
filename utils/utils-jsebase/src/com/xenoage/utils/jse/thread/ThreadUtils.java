package com.xenoage.utils.jse.thread;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;

/**
 * Useful methods for working with {@link Thread}s.
 * 
 * @author Andreas Wenger
 */
public class ThreadUtils {

	/**
	 * {@link Thread#sleep(long)}, but in seconds, with ignoring the {@link InterruptedException}.
	 */
	public static void sleepS(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * {@link Thread#sleep(long)}, with ignoring the {@link InterruptedException}.
	 */
	public static void sleepMs(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Gets the current stack trace.
	 */
	public static List<StackTraceElement> getCurrentStackTrace() {
		return alist(Thread.currentThread().getStackTrace());
	}

	/**
	 * Gets the stack trace of the given Throwable as a string.
	 */
	public static String getStackTraceString(Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		throwable.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 * Gets the caller of a method. If unknown, null is returned.
	 * @param level       The level of the caller: 1 = the caller, 2 = the caller of the caller, ...
	 *                    For example, when A.a() calls B.b(), and in B.b() you want to know who
	 *                    called B.b(), use getCaller(1).
	 */
	public static StackTraceElement getCaller(int level) {
		//TODO: this methods needs unit tests
		List<StackTraceElement> stackTrace = getCurrentStackTrace();
		if (stackTrace.size() > 2 + level)
			return stackTrace.get(2 + level);
		else
			return null;
	}

}
