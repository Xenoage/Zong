package com.xenoage.zong.desktop.gui.dialogs;

import static javafx.collections.FXCollections.observableArrayList;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;

/**
 * Controller for the <code>AudioSettingsDialog.fxml</code>.
 * @author andi
 *
 */
public class AudioSettingsDialogController {

	@FXML private ComboBox<String> cmbDeviceName;

	@FXML private Button btnSoundbankUseDefault;
	@FXML private Button btnSoundbankSelect;
	
	public void initialize() {
		//fill device names
		List<String> deviceNames = SynthManager.getAudioMixers();
		deviceNames.add(0, Lang.get(Voc.Default));
		cmbDeviceName.setItems(observableArrayList(deviceNames));
	}


	@FXML void onSoundbankSelect(ActionEvent event) {
		
	}

	@FXML void onSoundbankUseDefault(ActionEvent event) {

	}

}
