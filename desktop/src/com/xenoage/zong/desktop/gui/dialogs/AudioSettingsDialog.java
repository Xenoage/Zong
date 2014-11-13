package com.xenoage.zong.desktop.gui.dialogs;

import static com.xenoage.utils.jse.javafx.FXUtils.getValue;
import static com.xenoage.utils.jse.javafx.FXUtils.setValue;
import static javafx.collections.FXCollections.observableArrayList;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import lombok.Getter;

import com.xenoage.utils.jse.javafx.Dialog;
import com.xenoage.utils.jse.javafx.FileChooserUtils;
import com.xenoage.utils.jse.settings.Settings;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.desktop.io.filefilter.SoundbankFileFilter;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;

/**
 * Controller for the <code>AudioSettingsDialog.fxml</code>.
 * 
 * @author Andreas Wenger
 */
public class AudioSettingsDialog
	extends Dialog {

	@FXML private Pane root;
	@FXML private ComboBox<String> cmbDeviceName;
	@FXML private ComboBox<String> cmbSampleRateHz;
	@FXML private ComboBox<String> cmbChannels;
	@FXML private ComboBox<String> cmbBits;
	@FXML private ComboBox<String> cmbLatencyMs;
	@FXML private ComboBox<String> cmbMaxPolyphony;
	@FXML private ComboBox<String> cmbInterpolation;

	@FXML private Label lblSoundbank;
	@FXML private Button btnSoundbankUseDefault;
	@FXML private Button btnSoundbankSelect;

	@Getter private String soundbankPath = "";
	
	
	public void initialize() {
		//fill device names and select default value
		List<String> deviceNames = SynthManager.getAudioMixers();
		deviceNames.add(0, Lang.get(Voc.Default));
		cmbDeviceName.setItems(observableArrayList(deviceNames));
		cmbDeviceName.getSelectionModel().select(0);
		//fill other settings
		cmbSampleRateHz.setItems(observableArrayList("11025", "22050", "44100"));
		cmbSampleRateHz.getSelectionModel().select(2);
		cmbChannels.setItems(observableArrayList("1", "2"));
		cmbChannels.getSelectionModel().select(1);
		cmbBits.setItems(observableArrayList("8", "16"));
		cmbBits.getSelectionModel().select(1);
		cmbLatencyMs.setItems(observableArrayList("100", "200", "400", "800"));
		cmbLatencyMs.getSelectionModel().select(0);
		cmbMaxPolyphony.setItems(observableArrayList("32", "64", "96", "128", "256"));
		cmbMaxPolyphony.getSelectionModel().select(1);
		cmbInterpolation.setItems(observableArrayList(
			Lang.get(Voc.Linear), Lang.get(Voc.Cubic),
			Lang.get(Voc.Sinc), Lang.get(Voc.Point)));
		cmbInterpolation.getSelectionModel().select(0);
		//fill with current values, if available
		Settings s = Settings.getInstance();
		String file = SynthManager.configFile;
		setValue(cmbDeviceName, s.getSetting("devicename", file));
		setValue(cmbSampleRateHz, s.getSetting("samplerate", file));
		setValue(cmbChannels, s.getSetting("channels", file));
		setValue(cmbBits, s.getSetting("bits", file));
		setValue(cmbLatencyMs, s.getSetting("latency", file));
		setValue(cmbMaxPolyphony, s.getSetting("polyphony", file));
		setValue(cmbInterpolation, s.getSetting("interpolation", file));
		String soundbank = s.getSetting("soundbank", file);
		if (soundbank != null && soundbank.length() > 0) {
			try {
				File soundbankFile = new File(soundbank);
				if (soundbankFile.exists()) {
					soundbankPath = soundbankFile.getAbsolutePath();
					lblSoundbank.setText(soundbankFile.getName());
				}
			} catch (Exception ex) {
			}
		}
	}
	
	@Override public void onStageInit() {
		stage.setTitle(Lang.get(Voc.AudioSettings));
	}


	@FXML void onSoundbankSelect(ActionEvent event) {
		FileChooser fc = new FileChooser();
		FileChooserUtils.setInitialDir(fc, soundbankPath);
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
			SoundbankFileFilter.getDescription(), SoundbankFileFilter.getExtensions()));
    File file = fc.showOpenDialog(stage);
    if (file != null) {
      soundbankPath = file.getAbsolutePath();
			lblSoundbank.setText(file.getName());
    }
	}

	@FXML void onSoundbankUseDefault(ActionEvent event) {
		lblSoundbank.setText(Lang.get(Voc.Default));
	}
	
	@FXML void onOK(ActionEvent event) {
		Settings s = Settings.getInstance();
		String file = SynthManager.configFile;
		s.setSetting("devicename", file, cmbDeviceName.getSelectionModel().getSelectedIndex() == 0 ?
			"" : getValue(cmbDeviceName));
		s.setSetting("samplerate", file, getValue(cmbSampleRateHz));
		s.setSetting("channels", file, getValue(cmbChannels));
		s.setSetting("bits", file, getValue(cmbBits));
		s.setSetting("latency", file, getValue(cmbLatencyMs));
		s.setSetting("polyphony", file, getValue(cmbMaxPolyphony));
		s.setSetting("interpolation", file, getValue(cmbInterpolation));
		s.setSetting("soundbank", file, soundbankPath);
		s.save(file);
		onOK();
	}

	@FXML void onCancel(ActionEvent event) {
		onCancel();
	}
	
}
