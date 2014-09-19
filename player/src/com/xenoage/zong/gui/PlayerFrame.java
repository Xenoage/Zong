package com.xenoage.zong.gui;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.zong.desktop.App.app;
import static com.xenoage.zong.player.Player.pApp;

import java.io.File;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.xenoage.utils.jse.files.RecentFiles;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.Zong;
import com.xenoage.zong.commands.desktop.app.DocumentOpen;
import com.xenoage.zong.commands.desktop.app.Exit;
import com.xenoage.zong.commands.desktop.app.WebsiteOpen;
import com.xenoage.zong.commands.desktop.dialog.AudioSettingsDialogShow;
import com.xenoage.zong.commands.desktop.dialog.OpenDocumentDialog;
import com.xenoage.zong.commands.desktop.dialog.SaveDocumentDialog;
import com.xenoage.zong.commands.player.convert.DirToMidiConvert;
import com.xenoage.zong.commands.player.convert.FileToMidiConvert;
import com.xenoage.zong.commands.player.dialog.AboutDialogShow;
import com.xenoage.zong.commands.player.dialog.InfoDialogShow;
import com.xenoage.zong.commands.player.playback.PlaybackPause;
import com.xenoage.zong.commands.player.playback.PlaybackStart;
import com.xenoage.zong.commands.player.playback.PlaybackStop;
import com.xenoage.zong.core.Score;
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
	@FXML private SeparatorMenuItem mnuFileSepRecentFiles;
	@FXML private MenuItem mnuFileExit;
	@FXML private Menu mnuConvert;
	@FXML private MenuItem mnuConvertFileToMidi;
	@FXML private MenuItem mnuConvertDirToMidi;
	@FXML private Menu mnuSettings;
	@FXML private MenuItem mnuSettingsAudio;
	@FXML private Menu mnuHelp;
	@FXML private MenuItem mnuHelpReadme;
	@FXML private MenuItem mnuHelpWebsite;
	@FXML private MenuItem mnuHelpBlog;
	@FXML private MenuItem mnuHelpAbout;
	@FXML private Menu mnuBeta;
	@FXML private MenuItem mnuBetaReport;
	
	//recent files menu items
	private List<MenuItem> mnusRecentFiles = alist();

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
	
	private Map<String, Image> volumeIcons = map();
	
	
	public void initialize() {
		//load icons
		volumeIcons.put("high", readImage("audio-volume-high.png"));
		volumeIcons.put("medium", readImage("audio-volume-medium.png"));
		volumeIcons.put("low", readImage("audio-volume-low.png"));
		volumeIcons.put("muted", readImage("audio-volume-muted.png"));
		//reset values
		lblTimePos.setText("");
		lblTimeDuration.setText("");
		progress.setProgress(0);
		sliderVolume.setValue(70);
		imgVolume.setImage(volumeIcons.get("medium"));
		//recent files list
		RecentFiles.addListener(() -> updateRecentFiles());
		updateRecentFiles();
		//handle progress bar clicks
		pApp().getPlayer().addPlaybackListener(this);
		//handle volume slider events
		sliderVolume.valueProperty().addListener((property, oldValue, newValue) -> {
			float value = newValue.floatValue() * 0.01f;
			pApp().getPlayer().setVolume(value);
			String s = "muted";
			if (value > 0.8f)
				s = "high";
			else if (value > 0.4f)
				s = "medium";
			else if (value > 0f)
				s = "low";
			imgVolume.setImage(volumeIcons.get(s));
		});
	}
	
	private Image readImage(String filename) {
		return new Image(getClass().getResourceAsStream("img/" + filename));
	}
	
	private void updateRecentFiles() {
		//clear old menu items
		for (MenuItem mnu : mnusRecentFiles) {
			mnuFile.getItems().remove(mnu);
		}
		//add new menu items
		List<File> recentFiles = RecentFiles.getRecentFiles();
		int recentFilesMenuOffset = mnuFile.getItems().indexOf(mnuFileSepRecentFiles) + 1;
		for (int i = 0; i < recentFiles.size() && i < 10; i++) {
			final File file = recentFiles.get(i);
			MenuItem mnu = new MenuItem((i + 1) + ": " + file.getName());
			mnu.setOnAction(e -> new DocumentOpen(file.getAbsolutePath()).execute());
			mnusRecentFiles.add(mnu);
			mnuFile.getItems().add(recentFilesMenuOffset + i, mnu);
		}
	}
	
	public void setScore(Score score) {
		//set title
		String title = notNull(score.getInfo().getComposer());
		if (title.length() > 0)
			title += " - ";
		title += notNull(score.getInfo().getTitle());
		lblTitle.setText(title);
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
		if (mins.length() < 2)
			mins = "0" + mins;
		if (secs.length() < 2)
			secs = "0" + secs;
		return mins + ":" + secs;
	}


	@FXML void onOpen(ActionEvent event) {
		app().execute(new OpenDocumentDialog(stage));
	}

	@FXML void onSave(ActionEvent event) {
		app().execute(new SaveDocumentDialog(stage));
	}

	@FXML void onInfo(ActionEvent event) {
		if (pApp().getActiveDocument() == null) {
			pApp().showMessageDialog(Lang.get(Voc.NoFileLoaded));
		}
		else {
			app().execute(new InfoDialogShow(pApp().getActiveDocument(), stage));
		}
	}

	@FXML void onExit(ActionEvent event) {
		app().execute(new Exit());
	}

	@FXML void onConvertFileToMidi(ActionEvent event) {
		app().execute(new FileToMidiConvert(stage));
	}

	@FXML void onConvertDirToMidi(ActionEvent event) {
		app().execute(new DirToMidiConvert(stage));
	}

	@FXML void onSettings(ActionEvent event) {
		app().execute(new AudioSettingsDialogShow(stage));
	}
	
	@FXML void onReadme(ActionEvent event) {
		app().execute(new AboutDialogShow(stage));
	}
	
	@FXML void onWebsite(ActionEvent event) {
		app().execute(new WebsiteOpen(Zong.website));
	}
	
	@FXML void onBlog(ActionEvent event) {
		app().execute(new WebsiteOpen(Zong.blog));
	}

	@FXML void onAbout(ActionEvent event) {
		app().execute(new AboutDialogShow(stage));
	}
	
	@FXML void onErrorReport(ActionEvent event) {
		app().execute(new AboutDialogShow(stage));
	}

	@FXML void onStart(ActionEvent event) {
		app().execute(new PlaybackStart());
	}

	@FXML void onPause(ActionEvent event) {
		app().execute(new PlaybackPause());
	}

	@FXML void onStop(ActionEvent event) {
		app().execute(new PlaybackStop());
	}
	
	@FXML void onProgressBarClick(MouseEvent event) {
		MidiScorePlayer player = pApp().getPlayer();
		if (player.getSequence() != null) {
			double pos = event.getX() / progress.getWidth();
			long mis = (long) (pos * player.getMicrosecondLength());
			player.setMicrosecondPosition(mis);
			playbackAtMs(mis / 1000);
		}
	}

}
