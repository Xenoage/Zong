package com.xenoage.zong.desktop.utils.error;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.zong.desktop.App.app;

import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.log.Level;
import com.xenoage.utils.log.Report;

import javafx.scene.control.Alert;

/**
 * This class handles error and warning {@link Report}s by
 * showing dialogs to the user and logging them.
 * 
 * TODO: replace later by JavaFXErrorProcessing in utils-jse8,
 * when Java 1.8.0_40 is released
 * 
 * @author Andreas Wenger
 */
public class GuiErrorProcessing
	extends BasicErrorProcessing {

	@Override protected void handleError(Report report) {
		//if it is a fatal error, error or warning, show a dialog
		if (report.getLevel() != Level.Remark) {
			//create message
			String message = (report.getMessage() != null ? report.getMessage() : "");
			if (report.getMessageID() != null)
				message = Lang.get(report.getMessageID());
			//file
			String filePath = null;
			if (report.getFilePaths() != null && report.getFilePaths().size() > 0)
				filePath = report.getFilePaths().get(0);
			//create dialog - TODO: level and filepath - TODO: enhance with error report via HTTP
			Alert dialog = app().dialog();
			dialog.setContentText(message + "\n\n" + report.getError());
			/* TODO if (report.error != null)
				dialog.showException(report.error);
			else
				dialog.showError(); */
			dialog.showAndWait();
		}
		//if it is a fatal error, close application
		if (report.getLevel() == Level.Fatal) {
			app().showMessageDialog(platformUtils().getStackTraceString(report.getError()));
			platformUtils().exit(report.getError());
		}
	}

}
