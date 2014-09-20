package com.xenoage.zong.desktop.gui.dialogs;

import static com.xenoage.zong.desktop.App.app;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;

import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.Zong;
import com.xenoage.zong.commands.desktop.app.WebsiteOpen;
import com.xenoage.zong.desktop.App;
import com.xenoage.zong.desktop.gui.utils.Dialog;

/**
 * Controller for the <code>FeedbackController.fxml</code>.
 * 
 * This dialog allows the user to send in error reports
 * and feature requests.
 * 
 * If a path to a document is given, a checkbox to send
 * this document is shown.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class FeedbackDialog
	extends Dialog {
	
	/** Optional path to a document. If not null, a checkbox to send
	 * this file is shown. */
	private final String documentPath;
	/** The editable problem description. */
	private final String text;

	@FXML private Pane root;
	
	public void initialize() {
	}
	
	@Override public void onStageInit() {
		stage.setTitle(Lang.get(Voc.FeedbackTitle));
	}
	
	@FXML void onBugtracker(ActionEvent event) {
		app().execute(new WebsiteOpen(Zong.bugtracker));
	}
	
	@FXML void onOK(ActionEvent event) {
		//save, ...
		onOK();
	}

	@FXML void onCancel(ActionEvent event) {
		onCancel();
	}
	
}
