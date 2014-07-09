package com.xenoage.zong.commands.desktop.dialog;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.error;
import static com.xenoage.zong.desktop.gui.utils.FXUtils.showModal;

import java.io.IOException;

import javafx.stage.Stage;
import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.desktop.gui.utils.FXUtils;

/**
 * This command shows a dialog that allows to change
 * the audio settings, load a soundfont and so on.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor public class AudioSettingsDialog
	extends TransparentCommand {
	
	private Stage parentStage = null;

	@Override public void execute() {
		try {
			Stage stage = FXUtils.createStageFromFXML(
				"/com/xenoage/zong/desktop/gui/dialogs/AudioSettingsDialog.fxml", Lang.get(Voc.AudioSettings));
			showModal(stage, parentStage);
		} catch (IOException ex) {
			handle(error(ex));
		}
		
		/* if (dlg.isOK()) {
			//re-init midi
			try {
				SynthManager.init(true);
			} catch (Exception ex) {
				handle(warning(Voc.MidiNotAvailable));
			}
		} */
	}

}
