package com.xenoage.utils.document;

import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.utils.document.io.NotesFormats;

import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.kernel.Range.range;

/**
 * Class for testing the {@link Document} interface.
 * It contains just a list of text notes.
 * 
 * @author Andreas Wenger
 */
public class Notes
	implements Document {

	private List<String> lines = new ArrayList<>();

	private CommandPerformer commandPerformer = new CommandPerformer(this);
	private NotesFormats supportedFormats = new NotesFormats();

	
	public List<String> getLines() {
		return lines;
	}
	
	@Override public CommandPerformer getCommandPerformer() {
		return commandPerformer;
	}

	@Override public NotesFormats getSupportedFormats() {
		return supportedFormats;
	}

	@Override public String toString() {
		StringBuilder ret = new StringBuilder();
		for (int i : range(lines)) {
			ret.append(lines.get(i));
			if (i < lines.size() - 1)
				ret.append(";");
		}
		return ret.toString();
	}

}
