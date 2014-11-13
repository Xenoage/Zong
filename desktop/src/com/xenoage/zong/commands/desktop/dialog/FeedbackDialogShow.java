package com.xenoage.zong.commands.desktop.dialog;

import static com.xenoage.utils.jse.javafx.Dialog.dialog;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.zong.desktop.gui.dialogs.FeedbackDialog;

/**
 * Shows the feedback dialog.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class FeedbackDialogShow
	extends TransparentCommand {
	
	private Window owner;

	@Override public void execute() {
		dialog(FeedbackDialog.class).showDialog(owner);
	}

}
