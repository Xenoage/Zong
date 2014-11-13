package com.xenoage.zong.gui.dialog;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.desktop.App.app;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.xenoage.utils.jse.javafx.Dialog;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.info.Creator;
import com.xenoage.zong.core.info.Rights;
import com.xenoage.zong.core.info.ScoreInfo;
import com.xenoage.zong.core.music.Part;

/**
 * Dialog with information about a {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class InfoDialog
	extends Dialog {

	@FXML private Label lblTitle;
	@FXML private TextField txtWorkNumber;
	@FXML private TextField txtWorkTitle;
	@FXML private TextField txtMovementNumber;
	@FXML private TextField txtMovementTitle;
	@FXML private TextArea txtCreators;
	@FXML private TextArea txtRights;
	@FXML private TextArea txtParts;
	
	
	@Override public void onStageInit() {
		stage.setTitle(app().getName() + " - " + Lang.get(Voc.Info));
	}
	
	public void setScore(Score score) {
		ScoreInfo info = score.getInfo();
		lblTitle.setText(info.getTitle());
		txtWorkNumber.setText(info.getWorkNumber());
		txtWorkTitle.setText(info.getWorkTitle());
		txtMovementNumber.setText(info.getMovementNumber());
		txtMovementTitle.setText(info.getMovementTitle());
		//creators
		String s = "-";
		if (info.getCreators().size() > 0) {
			s = "";
			for (Creator creator : info.getCreators())
				s += (creator.getType() != null ? creator.getType() + ": " : "") + creator.getName() + "\n";
		}
		txtCreators.setText(s);
		//rights
		s = "-";
		if (info.getRights().size() > 0) {
			s = "";
			for (Rights rights : info.getRights())
				s += (rights.getType() != null ? rights.getType() + ": " : "") + rights.getText() + "\n";
		}
		txtRights.setText(s);
		//parts
		s = "-";
		if (score.getStavesList().getParts().size() > 0) {
			s = "";
			for (int i : range(score.getStavesList().getParts())) {
				Part part = score.getStavesList().getParts().get(i);
				s += i + ": " + part.getName() + "\n";
			}
		}
		txtParts.setText(s);
	}

	@FXML void onOK(ActionEvent event) {
		onOK();
	}

}
