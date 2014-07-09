package com.xenoage.zong.desktop.gui.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.xenoage.utils.jse.lang.LangResourceBundle;
import com.xenoage.zong.Voc;

/**
 * Some useful JavaFX helper functions.
 * 
 * @author Andreas Wenger
 */
public class FXUtils {
	
	/**
	 * Creates and returns a {@link Stage} from the given FXML file.
	 * The vocabulary from {@link Voc} is used.
	 * @param fxmlName  the path to the FXML name within the classpath, e.g. "/my/package/Test.fxml"
	 * @param title     the title of the stage
	 */
	public static Stage createStageFromFXML(String fxmlPath, String title)
		throws IOException {
		Parent root = createNodeFromFXML(fxmlPath);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(title);
		stage.setScene(scene);
		return stage;
	}
	
	/**
	 * Creates and returns a {@link Parent} node from the given FXML file.
	 * The vocabulary from {@link Voc} is used.
	 * @param fxmlName  the path to the FXML name within the classpath, e.g. "/my/package/Test.fxml"
	 */
	public static Parent createNodeFromFXML(String fxmlPath)
		throws IOException {
		return FXMLLoader.load(
			FXUtils.class.getResource(fxmlPath), new LangResourceBundle(Voc.values()));
	}
	
	/**
	 * Gets the parent stage of the given node.
	 */
	public static Stage getStage(Node node) {
		return (Stage) node.getScene().getWindow();
	}
	
	public static void showModal(Stage stage, Stage parentStage) {
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(parentStage);
		stage.show();
	}
	
	/**
	 * Returns the selected item of the given {@link ComboBox}.
	 */
	public static <T> T getValue(ComboBox<T> cmb) {
		return cmb.getSelectionModel().getSelectedItem();
	}
	
	/**
	 * Selects the item of the given {@link ComboBox} with the given value.
	 * If not available, nothing happens.
	 */
	public static <T> void setValue(ComboBox<T> cmb, T value) {
		int index = cmb.getItems().indexOf(value);
		if (index != -1)
			cmb.getSelectionModel().select(index);
	}

}
