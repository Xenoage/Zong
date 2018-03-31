package com.xenoage.utils.document;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.xenoage.utils.document.command.LineAdd;
import com.xenoage.utils.document.command.LineRemove;
import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.io.JseOutputStream;

/**
 * Tests for the document framework, based on the test data class {@link Notes}.
 * 
 * @author Andreas Wenger
 */
public class DocumentTest {

	private Notes createTestNotes() {
		Notes notes = new Notes();
		//add 5 lines
		for (int i : range(1, 5))
			notes.getLines().add("" + i);
		assertEquals("1;2;3;4;5", notes.toString());
		return notes;
	}

	@Test public void testCommands() {
		Notes notes = createTestNotes();
		//remove the second line
		notes.getCommandPerformer().execute(new LineRemove(notes, 1));
		assertEquals("1;3;4;5", notes.toString());
		//add a line
		notes.getCommandPerformer().execute(new LineAdd(notes, "6"));
		assertEquals("1;3;4;5;6", notes.toString());
		//undo last step
		notes.getCommandPerformer().undo();
		assertEquals("1;3;4;5", notes.toString());
		//redo last undo
		notes.getCommandPerformer().redo();
		assertEquals("1;3;4;5;6", notes.toString());
		//undo last two steps
		notes.getCommandPerformer().undo();
		notes.getCommandPerformer().undo();
		assertEquals("1;2;3;4;5", notes.toString());
		//no undo should be possible any more
		assertEquals(false, notes.getCommandPerformer().isUndoPossible());
		//add two lines
		notes.getCommandPerformer().execute(new LineAdd(notes, "7"));
		notes.getCommandPerformer().execute(new LineAdd(notes, "8"));
		assertEquals("1;2;3;4;5;7;8", notes.toString());
		//undo last step
		notes.getCommandPerformer().undo();
		assertEquals("1;2;3;4;5;7", notes.toString());
		//add another line
		notes.getCommandPerformer().execute(new LineAdd(notes, "9"));
		assertEquals("1;2;3;4;5;7;9", notes.toString());
		//no redo should be possible
		assertEquals(false, notes.getCommandPerformer().isRedoPossible());
	}

	@Test public void testIO()
		throws IOException {
		Notes notes = createTestNotes();
		//write to byte buffer
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileFormat<Notes> format = notes.getSupportedFormats().getWriteDefaultFormat();
		format.getOutput().write(notes, 0, new JseOutputStream(out));
		//read again
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Notes nodesRead = format.getInput().read(new JseInputStream(in), null);
		assertEquals("1;2;3;4;5", nodesRead.toString());
	}

}
