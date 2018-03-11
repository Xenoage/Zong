package com.xenoage.zong.commands.desktop.app;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.awt.Desktop;
import java.net.URI;

import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;

/**
 * Opens the given website in the default browser.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class WebsiteOpen
	extends TransparentCommand {
	
	private String uri;

	/**
	 * Execute or redo the command.
	 */
	@Override public void execute() {
		try {
			if (false == uri.contains("://"))
				uri = "http://" + uri;
			Desktop.getDesktop().browse(new URI(uri));
		} catch (Exception ex) {
			INSTANCE.log(Companion.warning("Could not open URI: " + uri + " (" + ex.getMessage() + ")"));
		}
	}

}
