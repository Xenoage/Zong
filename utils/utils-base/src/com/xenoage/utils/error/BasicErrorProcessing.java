package com.xenoage.utils.error;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Log.log;

import com.xenoage.utils.log.Level;
import com.xenoage.utils.log.Report;

/**
 * This class handles all errors and warnings that are caught
 * within this program. All errors are just logged.
 * 
 * It can be extended to process the error further, e.g. by
 * showing dialogs.
 * 
 * @author Andreas Wenger
 */
public class BasicErrorProcessing
	implements ErrorProcessing {

	//when an error occurs within this class, we do not want to process
	//this error, since this can lead to infinite loop
	private boolean working = false;


	@Override public synchronized void report(Report report) {
		if (!working) {
			//set working flag
			working = true;
			//log the error
			log(report);
			//console
			if (report.error != null)
				report.error.printStackTrace();
			//handle error
			handleError(report);
			//reset working flag
			working = false;
		}
	}

	/**
	 * Closes the program, when a fatal error is reported.
	 * Can be overridden to process the error further, e.g.
	 * by showing a dialog.
	 */
	protected void handleError(Report report) {
		if (report.level == Level.Fatal)
			platformUtils().exit(report.error);
	}

}
