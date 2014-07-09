package com.xenoage.zong.gui;

import static com.xenoage.zong.desktop.gui.utils.FXUtils.getStage;
import static com.xenoage.zong.player.PlayerApp.pApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

import com.xenoage.zong.commands.desktop.dialog.AudioSettingsDialog;
import com.xenoage.zong.commands.desktop.dialog.OpenDocumentDialog;
import com.xenoage.zong.commands.desktop.dialog.SaveDocumentDialog;
import com.xenoage.zong.commands.player.convert.DirToMidiConvert;
import com.xenoage.zong.commands.player.convert.FileToMidiConvert;

public class PlayerController {

	//menu
	@FXML private Menu mnuFile;
	@FXML private MenuItem mnuFileOpen;
	@FXML private MenuItem mnuFileSaveAs;
	@FXML private MenuItem mnuFileInfo;
	@FXML private MenuItem mnuFileExit;
	@FXML private Menu mnuConvert;
	@FXML private MenuItem mnuConvertFileToMidi;
	@FXML private MenuItem mnuConvertDirToMidi;
	@FXML private Menu mnuSettings;
	@FXML private MenuItem mnuSettingsAudio;
	@FXML private Menu mnuHelp;
	@FXML private MenuItem mnuHelpReadme;
	@FXML private MenuItem mnuHelpAbout;
	
	//playback area
	@FXML private Label lblTitle;
	@FXML private Label lblTimePos;
	@FXML private ProgressBar progress;
	@FXML private Label lblTimeDuration;
	
	//controls
	@FXML private Button btnOpen;
	@FXML private Button btnStart;
	@FXML private Button btnPause;
	@FXML private Button btnStop;
	@FXML private ImageView imgVolume;
	@FXML private Slider sliderVolume;
	@FXML private Button btnSave;
	@FXML private Button btnInfo;
	
	@FXML void onOpen(ActionEvent event) {
		pApp().getCommandPerformer().execute(new OpenDocumentDialog(getStage(lblTitle)));
	}

	@FXML void onSave(ActionEvent event) {
		pApp().getCommandPerformer().execute(new SaveDocumentDialog(getStage(lblTitle)));
	}

	@FXML void onInfo(ActionEvent event) {

	}

	@FXML void onExit(ActionEvent event) {

	}

	@FXML void onConvertFileToMidi(ActionEvent event) {
		pApp().getCommandPerformer().execute(new FileToMidiConvert(getStage(lblTitle)));
	}

	@FXML void onConvertDirToMidi(ActionEvent event) {
		pApp().getCommandPerformer().execute(new DirToMidiConvert(getStage(lblTitle)));
	}

	@FXML void onSettings(ActionEvent event) {
		pApp().getCommandPerformer().execute(new AudioSettingsDialog(getStage(lblTitle)));
	}

	@FXML void onReadme(ActionEvent event) {

	}

	@FXML void onHelp(ActionEvent event) {

	}

	@FXML void onStart(ActionEvent event) {

	}

	@FXML void onPause(ActionEvent event) {

	}

	@FXML void onStop(ActionEvent event) {

	}

}
