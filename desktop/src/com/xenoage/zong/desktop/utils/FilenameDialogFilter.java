package com.xenoage.zong.desktop.utils;

import com.xenoage.utils.filter.Filter;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import javafx.scene.control.ChoiceDialog;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Filter dialog which allows selecting a single filename.
 * When there is only one file given, it is chosen without
 * showing a dialog.
 * 
 * @author Andreas Wenger
 */
public class FilenameDialogFilter
	implements Filter<String> {

	@Override public List<String> filter(List<String> values) {
		List<String> ret = new LinkedList<>();
		//when there is only one file, select it. when there is no file, also don't
		//show a dialog.
		if (values.size() < 2) {
			ret.addAll(values);
		}
		else {
			ChoiceDialog<String> dialog = new ChoiceDialog<>(values.get(0), values); //TODO: use Zong! dialog factory
			dialog.setContentText(Lang.getLabel(Voc.SelectDocument));
			Optional<String> selectedFile = dialog.showAndWait();
			//open file
			selectedFile.ifPresent(ret::add);
		}
		return ret;
	}

}
