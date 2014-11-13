package com.xenoage.zong.gui.dialog;

import static com.xenoage.zong.desktop.App.app;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.controlsfx.control.HyperlinkLabel;

import com.xenoage.utils.jse.javafx.Dialog;
import com.xenoage.zong.Zong;
import com.xenoage.zong.commands.desktop.app.WebsiteOpen;

/**
 * About dialog.
 * 
 * @author Andreas Wenger
 */
public class AboutDialog
	extends Dialog {

	@FXML private Label lblAppName;
	@FXML private Label lblVersionValue;
	@FXML private Label lblCopyrightValue;
	@FXML private HyperlinkLabel lblWebsiteValue;
	
	public void initialize() {
		lblAppName.setText(app().getName());
		lblVersionValue.setText(Zong.projectVersionLong);
		lblCopyrightValue.setText(Zong.copyright);
		lblWebsiteValue.setText("[" + Zong.website + "]");
		lblWebsiteValue.setOnAction(e -> {
			app().execute(new WebsiteOpen(Zong.website));
		});
	}
	
	@FXML public void onOK(ActionEvent event) {
		onOK();
	}
	
}
