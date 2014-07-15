package com.xenoage.zong.commands.player.dialog;

import javafx.stage.Window;
import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.zong.desktop.gui.utils.Dialog;
import com.xenoage.zong.gui.dialog.AboutDialog;

/**
 * Shows an about-dialog.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class AboutDialogShow
	extends TransparentCommand {
	
	private Window owner;

	@Override public void execute() {
		Dialog.dialog(AboutDialog.class).showDialog(owner);
	}

}
