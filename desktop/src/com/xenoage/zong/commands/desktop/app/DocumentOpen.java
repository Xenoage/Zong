package com.xenoage.zong.commands.desktop.app;

import static com.xenoage.zong.desktop.App.app;

import java.io.File;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.utils.jse.files.RecentFiles;

/**
 * Command for opening a given file.
 * 
 * The file is added to the list of recent files, if it
 * can be referenced as a {@link File}.
 *
 * @author Andreas Wenger
 */
public class DocumentOpen
	extends TransparentCommand {

	private final String filepath;


	public DocumentOpen(String filepath) {
		this.filepath = filepath;
	}

	@Override public void execute() {
		app().openDocument(filepath);
		File file = new File(filepath);
		RecentFiles.addRecentFile(file);
	}

}
