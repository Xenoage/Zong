package com.xenoage.zong.commands.player.dialog;

import static com.xenoage.zong.desktop.gui.utils.Dialog.dialog;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
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
		dialog(AboutDialog.class).showDialog(owner);
	}

}
