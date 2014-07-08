package com.xenoage.zong.desktop.commands.dialog;

import static com.xenoage.zong.desktop.App.app;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.zong.Zong;

/**
 * Shows an about-dialog.
 *
 * @author Andreas Wenger
 */
public class AboutDialogShow
	extends TransparentCommand {

	@Override public void execute() {
		app().showMessageDialog(
			app().getNameAndVersion() + " (" + Zong.projectIterationName + ")\n\n" + Zong.copyright);
	}

}
