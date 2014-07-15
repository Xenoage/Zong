package com.xenoage.zong.gui;

import static com.xenoage.zong.player.Player.pApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

import com.xenoage.utils.document.command.Command;
import com.xenoage.zong.commands.desktop.app.Exit;
import com.xenoage.zong.commands.desktop.dialog.AudioSettingsDialogShow;
import com.xenoage.zong.commands.desktop.dialog.OpenDocumentDialog;
import com.xenoage.zong.commands.desktop.dialog.SaveDocumentDialog;
import com.xenoage.zong.commands.player.convert.DirToMidiConvert;
import com.xenoage.zong.commands.player.convert.FileToMidiConvert;
import com.xenoage.zong.commands.player.dialog.AboutDialogShow;
import com.xenoage.zong.commands.player.playback.PlaybackPause;
import com.xenoage.zong.commands.player.playback.PlaybackStart;
import com.xenoage.zong.commands.player.playback.PlaybackStop;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.desktop.gui.utils.Dialog;
import com.xenoage.zong.desktop.io.midi.out.MidiScorePlayer;
import com.xenoage.zong.io.midi.out.PlaybackListener;

public class PlayerFrame
	extends Dialog
	implements PlaybackListener {

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
	
	public void initialize() {
		//handle progress bar clicks
		pApp().getPlayer().addPlaybackListener(this);
		progress.setOnMouseClicked(e -> {
			MidiScorePlayer player = pApp().getPlayer();
			if (player.getSequence() != null) {
				double pos = e.getX() / progress.getWidth();
				long mis = (long) (pos * player.getMicrosecondLength());
				player.setMicrosecondPosition(mis);
				playbackAtMs(mis / 1000);
			}
		});
	}

	@Override public void playbackAtMP(MP mp, long ms) {
	}

	@Override public void playbackAtMs(long ms) {
		MidiScorePlayer player = pApp().getPlayer();
		long lengthMs = player.getMicrosecondLength() / 1000;
		Platform.runLater(() -> {
			if (lengthMs > 0) {
				progress.setProgress(1.0 * ms / lengthMs);
				lblTimePos.setText(formatTime(ms));
				lblTimeDuration.setText(formatTime(lengthMs));
			}
			else {
				progress.setProgress(0);
				lblTimePos.setText("");
				lblTimeDuration.setText("");
			}
		});
	}

	@Override public void playbackStarted() {
	}

	@Override public void playbackPaused() {
	}

	@Override public void playbackStopped() {
		playbackAtMs(0);
	}

	@Override public void playbackAtEnd() {
		playbackAtMs(0);
	}

	//TIDY: same code somewhere in Viewer
	private String formatTime(long timeMs) {
		long timeS = timeMs / 1000;
		String mins = String.valueOf(timeS / 60);
		String secs = String.valueOf(timeS % 60);
		if (secs.length() < 2) {
			secs = "0" + secs;
		}
		return mins + ":" + secs;
	}


	@FXML void onOpen(ActionEvent event) {
		execute(new OpenDocumentDialog(stage));
	}

	@FXML void onSave(ActionEvent event) {
		execute(new SaveDocumentDialog(stage));
	}

	@FXML void onInfo(ActionEvent event) {

	}

	@FXML void onExit(ActionEvent event) {
		execute(new Exit());
	}

	@FXML void onConvertFileToMidi(ActionEvent event) {
		execute(new FileToMidiConvert(stage));
	}

	@FXML void onConvertDirToMidi(ActionEvent event) {
		execute(new DirToMidiConvert(stage));
	}

	@FXML void onSettings(ActionEvent event) {
		execute(new AudioSettingsDialogShow(stage));
	}

	@FXML void onHelp(ActionEvent event) {
		execute(new AboutDialogShow(stage));
	}

	@FXML void onStart(ActionEvent event) {
		execute(new PlaybackStart());
	}

	@FXML void onPause(ActionEvent event) {
		execute(new PlaybackPause());
	}

	@FXML void onStop(ActionEvent event) {
		execute(new PlaybackStop());
	}
	
	private void execute(Command command) {
		pApp().getCommandPerformer().execute(command);
	}

}
