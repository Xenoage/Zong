package com.xenoage.zong.commands.desktop.dialog;

import static com.xenoage.utils.jse.javafx.FileChooserUtils.addFilter;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.zong.desktop.App.app;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.utils.jse.settings.FileSettings;
import com.xenoage.zong.commands.desktop.app.DocumentOpen;

/**
 * This command shows a dialog that allows to
 * select a file for opening.
 * 
 * The possible file formats are the
 * {@link SupportedFormats} of the app.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor public class OpenDocumentDialog
	extends TransparentCommand {

	private Window ownerWindow;


	@Override public void execute() {
		FileChooser fileChooser = new FileChooser();
		//use last document directory
		File initDir = FileSettings.getLastDir();
		if (initDir != null)
			fileChooser.setInitialDirectory(initDir);
		//add filters
		SupportedFormats<?> supportedFormats = app().getSupportedFormats();
		for (FileFormat<?> fileFormat : supportedFormats.getReadFormats()) {
			addFilter(fileChooser, fileFormat);
		}
		//show the dialog
		File file = fileChooser.showOpenDialog(ownerWindow);
		if (file != null) {
			log(remark("Dialog closed (OK), opening file \"" + file.getName() + "\""));
			new DocumentOpen(file.getAbsolutePath()).execute();
			//save document directory
			FileSettings.rememberDir(file);
		}
		else {
			log(remark("Dialog closed (Cancel)"));
		}
	}

}
