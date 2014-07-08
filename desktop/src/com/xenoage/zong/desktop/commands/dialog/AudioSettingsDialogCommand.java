package com.xenoage.zong.commands.dialogs;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.zong.SwingApp.app;

import com.xenoage.zong.Voc;
import com.xenoage.zong.commands.Command;
import com.xenoage.zong.commands.CommandPerformer;
import com.xenoage.zong.gui.dialogs.AudioSettingsDialog;
import com.xenoage.zong.io.midi.out.SynthManager;


/**
 * This command shows a dialog that allows to change
 * the audio settings, load a soundfont and so on.
 * 
 * @author Andreas Wenger
 */
public class AudioSettingsDialogCommand
	extends Command
{


	/**
	 * Shows the dialog.
	 */
	@Override public void execute(CommandPerformer performer)
	{
		AudioSettingsDialog dlg = new AudioSettingsDialog(app().getMainFrame());
		dlg.setVisible(true);
		if (dlg.isOK()) {
			//re-init midi
			try {
				SynthManager.init(true);
			} catch (Exception ex) {
				handle(warning(Voc.MidiNotAvailable));
			}
		}
	}
	
}
