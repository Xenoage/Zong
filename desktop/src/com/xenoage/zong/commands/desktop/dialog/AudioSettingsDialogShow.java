package com.xenoage.zong.commands.desktop.dialog;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.zong.desktop.gui.utils.Dialog.dialog;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.zong.Voc;
import com.xenoage.zong.desktop.gui.dialogs.AudioSettingsDialog;
import com.xenoage.zong.desktop.gui.utils.DialogResult;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;

/**
 * This command shows a dialog that allows to change
 * the audio settings, load a soundfont and so on.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class AudioSettingsDialogShow
	extends TransparentCommand {
	
	private Window owner;

	@Override public void execute() {
		AudioSettingsDialog dialog = dialog(AudioSettingsDialog.class);
		DialogResult result = dialog.showDialog(owner);
		if (result == DialogResult.OK) {
			//re-init midi
			try {
				SynthManager.init(true);
			} catch (Exception ex) {
				handle(warning(Voc.MidiNotAvailable));
			}
		}
	}

}
