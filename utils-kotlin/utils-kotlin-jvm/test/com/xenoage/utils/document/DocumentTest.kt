package com.xenoage.utils.document

import com.xenoage.utils.io.StringInputStream
import com.xenoage.utils.io.StringOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for the document framework, based on the test data class [Notes].
 */
class DocumentTest {

	private fun createTestNotes(): Notes {
		val notes = Notes(mutableListOf("1", "2", "3", "4", "5"))
		assertEquals("1;2;3;4;5", notes.toString())
		return notes
	}

	@Test fun testCommands() {
		val notes = createTestNotes()
		//remove the second line
		notes.commandPerformer.execute(LineRemove(notes, 1))
		assertEquals("1;3;4;5", notes.toString())
		//add a line
		notes.commandPerformer.execute(LineAdd(notes, "6"))
		assertEquals("1;3;4;5;6", notes.toString())
		//undo last step
		notes.commandPerformer.undo()
		assertEquals("1;3;4;5", notes.toString())
		//redo last undo
		notes.commandPerformer.redo()
		assertEquals("1;3;4;5;6", notes.toString())
		//undo last two steps
		notes.commandPerformer.undo()
		notes.commandPerformer.undo()
		assertEquals("1;2;3;4;5", notes.toString())
		//no undo should be possible any more
		assertEquals(false, notes.commandPerformer.isUndoPossible)
		//add two lines
		notes.commandPerformer.execute(LineAdd(notes, "7"))
		notes.commandPerformer.execute(LineAdd(notes, "8"))
		assertEquals("1;2;3;4;5;7;8", notes.toString())
		//undo last step
		notes.commandPerformer.undo()
		assertEquals("1;2;3;4;5;7", notes.toString())
		//add another line
		notes.commandPerformer.execute(LineAdd(notes, "9"))
		assertEquals("1;2;3;4;5;7;9", notes.toString())
		//no redo should be possible
		assertEquals(false, notes.commandPerformer.isRedoPossible)
	}

	@Test fun testIO() {
		val notes = createTestNotes()
		val format = notes.supportedFormats.writeDefaultFormat
		//write to string buffer
		val outStream = StringOutputStream()
		format.output!!.write(notes, 0, outStream)
		val data = outStream.toString()
		//read again
		val inStream = StringInputStream(data)
		val nodesRead = format.input!!.read(inStream, null)
		assertEquals("1;2;3;4;5", nodesRead.toString())
	}

}
