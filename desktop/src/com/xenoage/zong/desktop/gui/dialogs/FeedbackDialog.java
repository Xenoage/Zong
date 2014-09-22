package com.xenoage.zong.desktop.gui.dialogs;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.jse.JsePlatformUtils.desktopIO;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.zong.desktop.App.app;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Setter;

import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.log.DesktopLogProcessing;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.Zong;
import com.xenoage.zong.commands.desktop.app.ExternalFileOpen;
import com.xenoage.zong.commands.desktop.app.WebsiteOpen;
import com.xenoage.zong.desktop.gui.utils.Dialog;
import com.xenoage.zong.desktop.utils.http.HttpReport;

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
public class FeedbackDialog
	extends Dialog {

	/** Optional path to a document. If not null, a checkbox to send
	 * this file is shown. */
	@Setter private String documentPath;
	/** The editable problem description. */
	@Setter private String text;

	@FXML private Pane root;
	@FXML private TextArea txaText;
	@FXML private HBox boxLog;
	@FXML private CheckBox chkLog;
	@FXML private CheckBox chkFile;
	@FXML private TextField txtEmail;


	public void initialize() {
		//if text is given, fill text area
		if (text != null)
			txaText.setText(text);
		//if document is given, provide a checkbox to attach it
		if (documentPath != null) {
			//attach file
			chkFile.setText(Lang.get(Voc.AttachFile, JseFileUtils.getFileName(documentPath)));
			chkFile.setSelected(true);
		}
		else {
			chkFile.setVisible(false);
		}
	}

	@Override public void onStageInit() {
		stage.setTitle(Lang.get(Voc.FeedbackTitle));
	}

	@FXML void onBugtracker(ActionEvent event) {
		app().execute(new WebsiteOpen(Zong.bugtracker));
	}

	@FXML void onShowLog(ActionEvent event) {
		app().execute(new ExternalFileOpen(DesktopLogProcessing.defaultFileName));
	}

	@FXML void onSend(ActionEvent event) {
		stage.getScene().setCursor(Cursor.WAIT);
		send();
		stage.getScene().setCursor(Cursor.DEFAULT);
		onOK();
	}

	@FXML void onCancel(ActionEvent event) {
		onCancel();
	}

	private boolean send() {
		HttpReport report = new HttpReport();
		report.registerData("lang.txt", Lang.getCurrentLanguage().getID());
		report.registerData("info.txt", txaText.getText());
		if (txtEmail.getText().length() > 0) {
			report.registerData("email.txt", txtEmail.getText());
		}
		if (chkLog.isSelected()) {
			try {
				report.registerData("app.log", desktopIO().openFile("data/app.log"));
			} catch (Exception ex) {
				report.registerData("app.log", "Error when inserting app.log");
			}
		}
		if (chkFile.isSelected() && documentPath != null) {
			String filename = FileUtils.getFileName(documentPath);
			try {
				report.registerData(filename, desktopIO().openFile(documentPath));
			} catch (Exception ex) {
				report.registerData(filename, "Error when inserting " + filename);
			}
		}
		try {
			report.send();
			app().showMessageDialog(Lang.get(Voc.SendOK));
			return true;
		} catch (IOException ex) {
			handle(warning(Voc.SendFailed));
			return false;
		}
	}

}
