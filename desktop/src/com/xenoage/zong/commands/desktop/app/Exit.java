package com.xenoage.zong.commands.desktop.app;

import static com.xenoage.zong.desktop.App.app;

import com.xenoage.utils.document.command.TransparentCommand;

/**
 * Requests to close the application.
 *
 * @author Andreas Wenger
 */
public class Exit
	extends TransparentCommand {

	/**
	 * Execute or redo the command.
	 */
	@Override public void execute() {
		app().close();
	}

}
