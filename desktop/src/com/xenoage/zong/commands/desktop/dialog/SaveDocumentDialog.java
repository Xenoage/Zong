package com.xenoage.zong.commands.desktop.dialog;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.zong.desktop.App.app;
import static com.xenoage.zong.desktop.gui.utils.FileChooserUtils.addFilter;

import java.io.File;
import java.util.HashMap;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.utils.jse.settings.FileSettings;

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
@AllArgsConstructor public class SaveDocumentDialog
	extends TransparentCommand {
	
	private Window ownerWindow;

	/**
	 * Shows the dialog.
	 */
	@Override public void execute() {
		FileChooser fileChooser = new FileChooser();
		//use last document directory, when running as a desktop application
		File initDir = FileSettings.getLastDir();
		if (initDir != null)
			fileChooser.setInitialDirectory(initDir);
		//add formats
		HashMap<ExtensionFilter, FileFormat<?>> formats = map();
		SupportedFormats<?> supportedFormats = app().getSupportedFormats();
		for (FileFormat<?> fileFormat : supportedFormats.getWriteFormats()) {
			ExtensionFilter filter = addFilter(fileChooser, fileFormat);
			formats.put(filter, fileFormat);
			if (supportedFormats.getWriteDefaultFormat() == fileFormat) {
				//set default
				fileChooser.setSelectedExtensionFilter(filter);
			}
		}
		//show the dialog
		File file = fileChooser.showSaveDialog(ownerWindow);
		if (file != null) {
			log(remark("Dialog closed (OK), saving file \"" + file.getName() + "\""));
			FileFormat<?> format = formats.get(fileChooser.getSelectedExtensionFilter());
			String defaultExt = format.getDefaultExtension();
			if (!file.getName().endsWith(defaultExt)) {
				file = new File(file.getAbsolutePath() + defaultExt);
			}
			app().saveDocument(file.getAbsolutePath(), format);
			//save document directory
			FileSettings.rememberDir(file);
		}
		else {
			log(remark("Dialog closed (Cancel)"));
		}
	}
}
