package com.xenoage.zong.commands.desktop.dialog;

import static com.xenoage.zong.desktop.App.app;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.zong.Zong;

/**
 * Shows an about-dialog.
 *
 * @author Andreas Wenger
 */
public class AboutDialog
	extends TransparentCommand {

	@Override public void execute() {
		app().showMessageDialog(
			app().getNameAndVersion() + " (" + Zong.projectIterationName + ")\n\n" + Zong.copyright);
	}

}
