package com.xenoage.utils.document.io

import com.xenoage.utils.document.Document
import kotlin.coroutines.experimental.EmptyCoroutineContext.get

/**
 * This base class provides a list of all formats which can be used
 * for loading or saving a document.
 */
abstract class SupportedFormats<T : Document> {

	/** The list of supported formats */
	abstract val formats: List<FileFormat<T>>

	/** A list of the supported formats for reading */
	val readFormats = formats.filter { it.input != null }

	/** A list of the supported formats for reading */
	val writeFormats = formats.filter { it.output != null }

	/** The default format for reading files */
	abstract val readDefaultFormat: FileFormat<T>

	/** The default format for writing files */
	abstract val writeDefaultFormat: FileFormat<T>

	/** Gets the format with the given ID. */
	fun getByID(id: String) = formats.find { it.id == id } ?:
		throw IllegalArgumentException("Format with ID \"$id\" does not exist")

	/**
	 * Gets the format of the file with the given name, guessed by its extension.
	 * If this is not possible, the default read format is returned.
	 */
	fun getFormatByExtension(filename: String) = formats.find {
			it.allExtensions.find { filename.endsWith(it, true) } != null
		} ?: readDefaultFormat

}
