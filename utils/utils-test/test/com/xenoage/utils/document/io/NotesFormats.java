package com.xenoage.utils.document.io;

import com.xenoage.utils.document.Notes;

/**
 * Supported file formats for the {@link Notes} class.
 * 
 * @author Andreas Wenger
 */
public class NotesFormats
	extends SupportedFormats<Notes> {

	public NotesFormats() {
		formats.add(getTextFormat());
	}

	private FileFormat<Notes> getTextFormat() {
		return new FileFormat<>("notestxt", "Notes Text", ".notestxt", new String[0], new NotesInput(),
				new NotesOutput());
	}

	@Override
	public FileFormat<Notes> getReadDefaultFormat() {
		return formats.get(0);
	}

	@Override
	public FileFormat<Notes> getWriteDefaultFormat() {
		return formats.get(0);
	}

}
