package com.xenoage.utils.document.io;

import com.xenoage.utils.document.Document;

import static com.xenoage.utils.kernel.Range.range;

/**
 * General information on a file format for a document.
 * 
 * @author Andreas Wenger
 */
public final class FileFormat<T extends Document> {

	private final String id;
	private final String name;
	private final String defaultExtension;
	private final String[] otherExtensions;
	private final FileInput<T> input;
	private final FileOutput<T> output;
	
	/**
	 * Creates a new {@link FileFormat}.
	 */
	public FileFormat(String id, String name, String defaultExtension, String[] otherExtensions,
		FileInput<T> input, FileOutput<T> output) {
		super();
		this.id = id;
		this.name = name;
		this.defaultExtension = defaultExtension;
		this.otherExtensions = otherExtensions;
		this.input = input;
		this.output = output;
	}

	/**
	 * Creates a new {@link FileFormat} with no input and output class.
	 */
	public FileFormat(String id, String name, String defaultExtension, String... otherExtensions) {
		this.id = id;
		this.name = name;
		this.defaultExtension = defaultExtension;
		this.otherExtensions = otherExtensions;
		this.input = null;
		this.output = null;
	}
	
	/**
	 * Gets the unique ID of the format, like "MP3".
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gets the name of the format, like "MPEG Audio Layer III".
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the default extension, like ".mp3" or ".xml".
	 */
	public String getDefaultExtension() {
		return defaultExtension;
	}
	
	/**
	 * Get more supported extensions, like ".mid" or ".xml".
	 */
	public String[] getOtherExtensions() {
		return otherExtensions;
	}
	
	/**
	 * Gets all supported extensions, like ".mid" or ".xml".
	 */
	public String[] getAllExtensions() {
		String[] ret = new String[otherExtensions.length + 1];
		ret[0] = defaultExtension;
		for (int i : range(otherExtensions))
			ret[1+i] = otherExtensions[i];
		return ret;
	}
	
	/**
	 * Gets all supported extensions with a leading asterisk, like "*.mid" or "*.xml".
	 */
	public String[] getAllExtensionsWithAsterisk() {
		String[] ret = new String[otherExtensions.length + 1];
		ret[0] = "*" + defaultExtension;
		for (int i : range(otherExtensions))
			ret[1+i] = "*" + otherExtensions[i];
		return ret;
	}
	
	/**
	 * Gets the reader for this file format, or null if unsupported. */
	public FileInput<T> getInput() {
		return input;
	}
	
	/**
	 * Gets the writer for this file format, or null if unsupported.
	 */
	public FileOutput<T> getOutput() {
		return output;
	}
	
	/**
	 * Returns a copy of this {@link FileFormat} with the given input and output class.
	 */
	public FileFormat<T> withIO(FileInput<T> input, FileOutput<T> output) {
		return new FileFormat<>(id, name, defaultExtension, otherExtensions, input, output);
	}

	/**
	 * Gets the file filter description of the format. By default, this
	 * is the name of the format with the default extensions and
	 * other extensions in parentheses, like "MPEG Audio Layer III (.mp3, .mpeg)".
	 */
	public String getFilterDescription() {
		StringBuilder ret = new StringBuilder(name);
		ret.append(" (");
		ret.append(defaultExtension);
		for (String ext : otherExtensions) {
			ret.append(", ");
			ret.append(ext);
		}
		ret.append(")");
		return ret.toString();
	}

}
