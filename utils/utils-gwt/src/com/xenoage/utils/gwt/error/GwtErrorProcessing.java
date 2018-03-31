package com.xenoage.utils.gwt.error;

import static com.xenoage.utils.PlatformUtils.platformUtils;

import com.google.gwt.user.client.Window;
import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.log.Level;
import com.xenoage.utils.log.Report;

/**
 * Error handler for GWT.
 * 
 * Errors and warnings are shown as dialog boxes.
 * 
 * @author Andreas Wenger
 */
public class GwtErrorProcessing
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
			//create dialog - TODO: nicer dialog, add level and filepath
			Window.alert(message + (filePath != null ? "\n" + filePath : ""));
		}
		//if it is a fatal error, close application
		if (report.level == Level.Fatal) {
			Window.alert(report.error.toString());
			platformUtils().exit(report.error);
		}
	}

}
