package com.xenoage.zong.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

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

	}

	@FXML void onSave(ActionEvent event) {

	}

	@FXML void onInfo(ActionEvent event) {

	}

	@FXML void onExit(ActionEvent event) {

	}

	@FXML void onConvertFileToMidi(ActionEvent event) {

	}

	@FXML void onConvertDirToMidi(ActionEvent event) {

	}

	@FXML void onSettings(ActionEvent event) {

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
