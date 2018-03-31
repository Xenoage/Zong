package com.xenoage.utils.jse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.xenoage.utils.annotations.Unneeded;

/**
 * Useful methods to work with operating system specific tasks.
 * 
 * @author Andreas Wenger
 */
public class OSUtils {

	public enum OS {
		Linux,
		Windows,
		MacOSX,
		Solaris,
		Other;
	}


	private static OS os = null;


	/**
	 * Gets the current operating system.
	 */
	public static OS getOS() {
		if (os == null) {
			String osName = System.getProperty("os.name");
			if (osName.contains("Linux"))
				os = OS.Linux;
			else if (osName.contains("Windows"))
				os = OS.Windows;
			else if (osName.contains("Mac OS X"))
				os = OS.MacOSX;
			else if (osName.contains("Solaris"))
				os = OS.Solaris;
			else
				os = OS.Other;
		}
		return os;
	}

	/**
	 * Gets the ID of the given operating system. This is:
	 * <ul>
	 * 	<li><code>linux-i586</code> for Linux 32bit</li>
	 *  <li><code>linux-amd64</code> for Linux 64bit</li>
	 *  <li><code>windows-i586</code> for Windows 32bit</li>
	 *  <li><code>windows-amd64</code> for Windows 64bit</li>
	 *  <li><code>macosx-universal</code> for Mac OS X</li>
	 * </ul>
	 * For other operating systems, an {@link UnsupportedOperationException} is thrown.
	 */
	@Unneeded public static String getID(OS os) {
		if (os == OS.MacOSX) {
			return "macosx-universal";
		}
		else {
			//64 bit or 32 bit?
			String osArch = System.getProperty("os.arch");
			boolean is64bit = "amd64".equals(osArch) || "x86_64".equals(osArch);
			String postfix = (is64bit ? "-amd64" : "-i586");
			if (os == OS.Linux)
				return "linux" + postfix;
			else if (os == OS.Windows)
				return "windows" + postfix;
			else
				throw new UnsupportedOperationException("Unsupported OS");
		}
	}

	/**
	 * Gets the file extension for the given operating system.
	 * E.g. for Mac OS X <code>".jnilib"</code> is returned.
	 * For unsupported operating systems, an {@link UnsupportedOperationException} is thrown.
	 */
	@Unneeded public static String getNativeLibExtension(OS os) {
		if (os == OS.Linux || os == OS.Solaris)
			return ".so";
		else if (os == OS.Windows)
			return ".dll";
		else if (os == OS.MacOSX)
			return ".jnilib";
		else
			throw new UnsupportedOperationException("Unsupported OS");
	}

	/**
	 * Loads the native library of the current OS with the given name from the given path.
	 * For example, when <code>librariesDir</code> is <code>"../lib"</code>
	 * and <code>library</code> is <code>"jogl"</code>, all <code>".jnilib"</code> files
	 * from <code>../lib/macosx-universal/jogl/</code> are loaded.
	 * If the directory can not be found, a {@link FileNotFoundException} is thrown,
	 * for all other problems an {@link IOException} is thrown.
	 */
	@Unneeded public static void loadNativeLibs(File librariesDir, String library)
		throws IOException {
		String osID = getID(getOS());
		File dir = new File(librariesDir, osID + "/" + library);
		if (!dir.exists())
			throw new FileNotFoundException();
		try {
			String ext = getNativeLibExtension(os);
			for (File file : dir.listFiles())
				if (file.getName().endsWith(ext))
					System.load(file.getAbsolutePath());
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

}
