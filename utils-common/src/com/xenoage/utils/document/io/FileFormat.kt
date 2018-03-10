package com.xenoage.utils.document.io

import com.xenoage.utils.document.Document

/**
 * General information on a file format for a document.
 *
 */
data class FileFormat<T : Document>(

		/** The unique ID of the format, like "MP3" */
		val id: String,

		/** The name of the format, like "MPEG Audio Layer III" */
		val name: String,

		/* Gets the default extension, like ".mp3" or ".xml" */
		val defaultExtension: String,

		/** Nore supported extensions, like ".mid" or ".xml" */
		val otherExtensions: List<String> = emptyList(),

		/** The reader for this file format, or null if unsupported */
		val input: FileInput<T>? = null,

		/** The writer for this file format, or null if unsupported */
		val output: FileOutput<T>? = null) {

	/** All supported extensions, like ".mid" or ".xml". */
	val allExtensions: List<String>
		get() = listOf(defaultExtension).plus(otherExtensions)

	/** All supported extensions with a leading asterisk, like "*.mid" or "*.xml". */
	val allExtensionsWithAsterisk: List<String>
		get() = allExtensions.map { "*.$it" }

	/**
	 * The file filter description of the format. By default, this
	 * is the name of the format with the default extensions and
	 * other extensions in parentheses, like "MPEG Audio Layer III (.mp3, .mpeg)".
	 */
	val filterDescription: String
		get() = "$name (${allExtensions.map { ".$it" }.joinToString()})"

	/** Returns a copy of this [FileFormat] with the given input and output class. */
	fun withIO(input: FileInput<T>, output: FileOutput<T>): FileFormat<T> {
		return FileFormat(id, name, defaultExtension, otherExtensions, input, output)
	}

}
