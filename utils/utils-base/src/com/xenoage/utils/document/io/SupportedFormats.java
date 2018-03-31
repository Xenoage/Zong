package com.xenoage.utils.document.io;

import com.xenoage.utils.document.Document;

import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;

/**
 * This base class provides a list of all formats which can be used
 * for loading or saving a document.
 * 
 * @author Andreas Wenger
 */
public abstract class SupportedFormats<T extends Document> {

	protected List<FileFormat<T>> formats = new ArrayList<>();


	/**
	 * Gets the list of supported formats.
	 */
	public List<FileFormat<T>> getFormats() {
		return formats;
	}

	/**
	 * Gets a list of the supported formats for reading.
	 */
	public List<FileFormat<T>> getReadFormats() {
		List<FileFormat<T>> ret = alist();
		for (FileFormat<T> f : formats)
			if (f.getInput() != null)
				ret.add(f);
		return ret;
	}

	/**
	 * Gets a list of the supported formats for reading.
	 */
	public List<FileFormat<T>> getWriteFormats() {
		List<FileFormat<T>> ret = alist();
		for (FileFormat<T> f : formats) {
			if (f.getOutput() != null)
				ret.add(f);
		}
		return ret;
	}

	/**
	 * Gets the default format for reading files.
	 */
	public abstract FileFormat<T> getReadDefaultFormat();

	/**
	 * Gets the default format for writing files.
	 */
	public abstract FileFormat<T> getWriteDefaultFormat();

	/**
	 * Gets the format with the given ID.
	 */
	public FileFormat<T> getByID(String id) {
		for (FileFormat<T> f : formats)
			if (f.getId().equals(id))
				return f;
		throw new IllegalArgumentException("Format with ID \"" + id + "\" does not exist");
	}
	
	/**
	 * Gets the format of the file with the given name, guessed by its extension.
	 * If this is not possible, the default read format is returned.
	 */
	public FileFormat<T> getFormatByExtension(String filename) {
		filename = filename.toLowerCase();
		for (FileFormat<T> format : formats)
			for (String ext : format.getAllExtensions())
				if (filename.endsWith(ext.toLowerCase()))
					return format;
		return getReadDefaultFormat();
	}
 
}
