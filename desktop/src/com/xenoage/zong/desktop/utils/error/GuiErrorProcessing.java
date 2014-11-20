package com.xenoage.zong.desktop.utils.error;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.zong.desktop.App.app;

import org.controlsfx.dialog.Dialogs;

import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.log.Level;
import com.xenoage.utils.log.Report;

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
		if (report.level != Level.Remark) {
			//create message
			String message = (report.message != null ? report.message : "");
			if (report.messageID != null)
				message = Lang.get(report.messageID);
			//file
			String filePath = null;
			if (report.filePaths != null && report.filePaths.size() > 0)
				filePath = report.filePaths.get(0);
			//create dialog - TODO: level and filepath - TODO: enhance with error report via HTTP
			Dialogs dialog = app().dialog().message(message);
			if (report.error != null)
				dialog.showException(report.error);
			else
				dialog.showError();
		}
		//if it is a fatal error, close application
		if (report.level == Level.Fatal) {
			app().showMessageDialog(platformUtils().getStackTraceString(report.error));
			platformUtils().exit(report.error);
		}
	}

}
