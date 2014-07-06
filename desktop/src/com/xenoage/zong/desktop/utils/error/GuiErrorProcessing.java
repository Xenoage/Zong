package com.xenoage.zong.desktop.utils.error;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.zong.desktop.App.app;

import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.log.Level;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.desktop.App;
import com.xenoage.zong.desktop.App.AppType;

/**
 * This class handles error and warning {@link Report}s by
 * showing dialogs to the user and logging them.
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
			app().dialog().message(message).showException(report.error);
		}
		//if it is a fatal error, close application
		if (report.level == Level.Fatal) {
			if (app().getAppType() == AppType.DesktopApp) {
				System.exit(1);
			}
			else {
				app().showMessageDialog(platformUtils().getStackTraceString(report.error));
				throw new Error("Fatal"); //TODO: find nice way to closedown applet
			}
		}
	}

}
