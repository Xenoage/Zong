package com.xenoage.zong.gui.dialog;

import com.xenoage.utils.jse.javafx.Dialog;
import com.xenoage.zong.Zong;
import com.xenoage.zong.commands.desktop.app.WebsiteOpen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.HyperlinkLabel;

import static com.xenoage.zong.desktop.App.app;

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
		lblVersionValue.setText(Zong.INSTANCE.getProjectVersionLong());
		lblCopyrightValue.setText(Zong.INSTANCE.getCopyright());
		lblWebsiteValue.setText("[" + Zong.INSTANCE.getWebsite() + "]");
		lblWebsiteValue.setOnAction(e -> app().execute(new WebsiteOpen(Zong.INSTANCE.getWebsite())));
	}
	
	@FXML public void onOK(ActionEvent event) {
		onOK();
	}
	
}
