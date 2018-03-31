package com.xenoage.utils.io;


/**
 * Some useful file filters.
 *
 * @author Andreas Wenger
 */
public class FileFilters {

	/**
	 * A filter for files with ".java" ending.
	 */
	public static FileFilter javaFilter =
		file -> file.getName().toLowerCase().endsWith(".java");

	/**
	 * A filter for files with ".po" ending.
	 */
	public static FileFilter poFilter =
		file -> file.getName().toLowerCase().endsWith(".po");

	/**
	 * A filter for files with ".xml" ending.
	 */
	public static FileFilter xmlFilter =
		file -> file.getName().toLowerCase().endsWith(".xml");

	/**
	 * A filter for files with ".svg" ending.
	 */
	public static FileFilter svgFilter =
		file -> file.getName().toLowerCase().endsWith(".svg");

	/**
	 * A filter for files with ".ttf" ending.
	 */
	public static FileFilter ttfFilter =
		file -> file.getName().toLowerCase().endsWith(".ttf");


	/**
	 * Returns a filter which accepts files which have one of the
	 * given extensions (if needed, include a "." before the extension, like ".xml").
	 */
	public static FileFilter extFilter(final String... extensions) {
		return file -> {
			String filename = file.getName().toLowerCase();
			for (String extension : extensions) {
				if (filename.endsWith(extension))
					return true;
			}
			return false;
		};
	}

	/**
	 * Returns a filter which accepts all of the given filters.
	 */
	public static FileFilter orFilter(final FileFilter... filters) {
		return file -> {
			for (FileFilter filter : filters) {
				if (filter.accept(file))
					return true;
			}
			return false;
		};
	}

}
