package com.xenoage.zong.commands.desktop.app;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.awt.Desktop;

import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;

/**
 * Opens the given file in the default program.
 * 
 * The default program for the file type is defined by the OS.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class ExternalFileOpen
	extends TransparentCommand {
	
	private String filepath;

	@Override public void execute() {
		try {
			Desktop.getDesktop().open(io().findFile(filepath));
		} catch (Exception ex) {
			log(warning("Could not open file: " + filepath + " (" + ex.getMessage() + ")"));
		}
	}

}
