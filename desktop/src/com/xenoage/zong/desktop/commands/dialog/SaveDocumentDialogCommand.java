package com.xenoage.zong.commands.dialogs;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.utils.swing.JFileChooserUtil.createFileFilter;
import static com.xenoage.zong.SwingApp.app;

import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.xenoage.utils.swing.JFileChooserUtil;
import com.xenoage.zong.commands.Command;
import com.xenoage.zong.commands.CommandPerformer;
import com.xenoage.zong.io.ScoreFileFormat;
import com.xenoage.zong.io.SupportedFormats;


/**
 * This command shows a dialog that allows to save
 * the current document.
 * 
 * The possible file formats are the
 * {@link SupportedFormats} of the app.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class SaveDocumentDialogCommand
	extends Command
{


	/**
	 * Shows the dialog.
	 */
	@Override public void execute(CommandPerformer performer)
	{
		JFileChooser fileChooser = new JFileChooser();

		//use last document directory, when running as a desktop application
		JFileChooserUtil.setDirFromSettings(fileChooser);

		//add formats
		HashMap<FileFilter, ScoreFileFormat> formats = new HashMap<FileFilter, ScoreFileFormat>();
		SupportedFormats supportedFormats = app().getSupportedFormats();
		for (ScoreFileFormat fileFormat : supportedFormats.getWriteFormats()) {
			FileFilter filter = createFileFilter(fileFormat.getInfo());
			formats.put(filter, fileFormat);
			fileChooser.addChoosableFileFilter(filter);
			if (supportedFormats.getWriteDefaultFormat() == fileFormat) {
			//set default
				fileChooser.setFileFilter(filter);
			}
		}

		//show the dialog
		int dialogResult = fileChooser.showSaveDialog(app().getMainFrame());
		if (dialogResult == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			log(remark("Dialog closed (OK), saving file \"" + file.getName() + "\""));
			ScoreFileFormat format = formats.get(fileChooser.getFileFilter());
			String defaultExt = format.getInfo().defaultExtension;
			if (!file.getName().endsWith(defaultExt)) {
				file = new File(file.getAbsolutePath() + defaultExt);
			}
			app().saveDocument(file.getAbsolutePath(), format);
		} else {
			log(remark("Dialog closed (Cancel)"));
		}

		//save document directory
		JFileChooserUtil.rememberDir(fileChooser);
	}
}
