package com.xenoage.zong.gui.dialog;

import static com.xenoage.zong.desktop.App.app;

import java.net.MalformedURLException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.controlsfx.control.HyperlinkLabel;

import com.xenoage.zong.Zong;
import com.xenoage.zong.desktop.gui.utils.Dialog;

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
			try {
				app().openWebsite(new URL(Zong.website), true);
			} catch (MalformedURLException ex) {
			}
		});
	}
	
	@FXML public void onOK(ActionEvent event) {
		onOK();
	}
	
}
