package com.xenoage.zong.commands.dialogs;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.utils.swing.JFileChooserUtil.createFileFilter;
import static com.xenoage.zong.SwingApp.app;

import java.io.File;

import javax.swing.JFileChooser;

import com.xenoage.utils.swing.JFileChooserUtil;
import com.xenoage.zong.commands.Command;
import com.xenoage.zong.commands.CommandPerformer;
import com.xenoage.zong.commands.app.OpenDocumentCommand;
import com.xenoage.zong.io.ScoreFileFormat;
import com.xenoage.zong.io.SupportedFormats;


/**
 * This command shows a dialog that allows to
 * select a file for opening.
 * 
 * The possible file formats are the
 * {@link SupportedFormats} of the app.
 *
 * @author Andreas Wenger
 */
public class OpenDocumentDialogCommand
	extends Command
{


	/**
	 * Shows the dialog.
	 */
	@Override public void execute(CommandPerformer performer)
	{
		JFileChooser fileChooser = new JFileChooser();

		//use last document directory
		JFileChooserUtil.setDirFromSettings(fileChooser);

		//add filters
		SupportedFormats supportedFormats = app().getSupportedFormats();
		for (ScoreFileFormat fileFormat : supportedFormats.getWriteFormats()) {
			fileChooser.addChoosableFileFilter(createFileFilter(fileFormat.getInfo()));
		}

		//show the dialog
		int dialogResult = fileChooser.showOpenDialog(app().getMainFrame());
		if (dialogResult == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			log(remark("Dialog closed (OK), opening file \"" + file.getName() + "\""));
			new OpenDocumentCommand(file.getAbsolutePath()).execute(performer);
		} else {
			log(remark("Dialog closed (Cancel)"));
		}

		//save document directory
		JFileChooserUtil.rememberDir(fileChooser);
	}

}
