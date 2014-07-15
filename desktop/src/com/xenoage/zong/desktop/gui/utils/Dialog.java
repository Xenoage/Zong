package com.xenoage.zong.desktop.gui.utils;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.zong.desktop.App.app;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Getter;

import com.xenoage.utils.jse.lang.LangResourceBundle;
import com.xenoage.zong.Voc;

/**
 * A base class for dialog controllers based on JavaFX.
 * 
 * A static constructor for creating the dialog from FXML
 * and actions for OK and Cancel are provided.
 * 
 * @author Andreas Wenger
 */
public class Dialog {
	
	/** The root node of the dialog. */
	@Getter protected Parent root;
	
	/** The stage with the root node. */
	@Getter protected Stage stage = null;
	
	protected DialogResult result = DialogResult.Cancel;
	
	
	/**
	 * Creates a {@link Dialog} for the given controller, based on a FXML file
	 * and the vocabulary found in {@link Voc}.
	 * If the loading fails, a fatal error is reported.
	 * @param controllerType  the class of the controller. The FXML file is expected at the
	 *                        same path, but ending with ".fxml".
	 */
	public static <T extends Dialog> T dialog(Class<T> controllerType) {
		//load scene
		FXMLLoader loader = new FXMLLoader();
		String fxmlName = controllerType.getSimpleName() + ".fxml";
		loader.setLocation(controllerType.getResource(fxmlName));
		loader.setResources(new LangResourceBundle(Voc.values()));
		try {
			loader.load();
		} catch (IOException ex) {
			log(warning(ex));
		}
		@SuppressWarnings("unchecked") T dialog = (T) loader.getController();
		dialog.root = loader.getRoot();
		//create stage
		dialog.stage = new Stage();
		dialog.stage.setScene(new Scene(dialog.root));
		dialog.stage.sizeToScene();
		//return dialog
		return dialog;
	}
	
	public void onOK() {
		result = DialogResult.OK;
		stage.close();
	}
	
	public void onCancel() {
		result = DialogResult.Cancel;
		stage.close();
	}
	
	/**
	 * Sets the title of the dialog.
	 */
	public void setTitle(String title) {
		stage.setTitle(title != null ? title : app().getName());
	}

	/**
	 * Shows the dialog as a blocking modal dialog, on the given owner window.
	 */
	public DialogResult showDialog(Window owner) {
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(owner);
		stage.showAndWait();
		return result;
	}
	
	
}
