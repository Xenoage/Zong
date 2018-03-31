package com.xenoage.utils.document

import com.xenoage.utils.collections.collect
import com.xenoage.utils.document.command.CommandPerformer
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.document.io.FileFormat
import com.xenoage.utils.document.io.FileInput
import com.xenoage.utils.document.io.FileOutput
import com.xenoage.utils.document.io.SupportedFormats
import com.xenoage.utils.io.InputStream
import com.xenoage.utils.io.OutputStream

/**
 * Class for testing the [Document] interface.
 * It contains just a list of text notes.
 */
class Notes(
		val lines: MutableList<String>
) : Document {

	override val commandPerformer = CommandPerformer(this)
	override val supportedFormats = NotesFormats()

	override fun toString(): String =
			lines.joinToString(";")
}

/**
 * Supported file formats for the [Notes] class.
 */
class NotesFormats : SupportedFormats<Notes>() {

	override val formats: List<FileFormat<Notes>>
		get() = listOf(FileFormat<Notes>("notestxt", "Notes Text", ".notestxt", emptyList(), NotesInput(), NotesOutput()))

	override val readDefaultFormat: FileFormat<Notes>
		get() = formats[0]

	override val writeDefaultFormat: FileFormat<Notes>
		get() = formats[0]

}

/**
 * This class read [Notes] from a text file.
 */
class NotesInput : FileInput<Notes> {
	override fun read(stream: InputStream, filePath: String?): Notes =
		Notes(collect { stream.readLine() })
}

/**
 * This class writes [Notes] to a text file.
 */
class NotesOutput : FileOutput<Notes>() {
	override fun write(document: Notes, fileIndex: Int, stream: OutputStream) {
		document.lines.forEach { stream.writeLine(it) }
		stream.close()
	}
}

/**
 * Adds a line at the end of [Notes].
 */
class LineAdd(
		private val notes: Notes,
		private val line: String
) : UndoableCommand() {

	override fun execute() {
		notes.lines.add(line)
	}

	override fun undo() {
		notes.lines.removeAt(notes.lines.size - 1)
	}
}

/**
 * Removes the given line from [Notes].
 */
class LineRemove(
		private val notes: Notes,
		private val lineIndex: Int
) : UndoableCommand() {

	//backup
	private var line: String = ""

	override fun execute() {
		line = notes.lines.removeAt(lineIndex)
	}

	override fun undo() {
		notes.lines.add(lineIndex, line)
	}
}