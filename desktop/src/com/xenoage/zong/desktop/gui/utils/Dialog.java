package com.xenoage.zong.desktop.gui.utils;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.fatal;
import static com.xenoage.zong.desktop.App.app;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.jse.lang.LangResourceBundle;
import com.xenoage.zong.Voc;
import com.xenoage.zong.desktop.App;

/**
 * A dialog based on JavaFX.
 * 
 * @author Andreas Wenger
 */
public class Dialog<Controller> {
	
	/** The title of the dialog. */
	@Getter @Setter private String title = null;
	
	/** The root node of the dialog. */
	@Getter private Parent root;
	
	/** The controller of the dialog. */
	@Getter private Controller controller;
	
	
	/**
	 * Creates a {@link Dialog} for the given controller, based on a FXML file
	 * and the vocabulary found in {@link Voc}.
	 * If the loading fails, a fatal error is reported.
	 * @param controllerType  the class of the controller. The FXML file is expected at the
	 *                        same path, but ending with ".fxml".
	 */
	public static <Controller> Dialog<Controller> dialog(Class<Controller> controllerType) {
		try {
			return new Dialog<>(controllerType);
		} catch (IOException ex) {
			handle(fatal(ex));
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Dialog(Class<Controller> controllerType)
		throws IOException {

		FXMLLoader loader = new FXMLLoader();
		String fxmlName = controllerType.getSimpleName() + ".fxml";
		loader.setLocation(controllerType.getResource(fxmlName));
		loader.setResources(new LangResourceBundle(Voc.values()));
		try {
			loader.load();
		} catch (IOException ex) {
			
		}
		this.root = loader.getRoot();
		this.controller = (Controller) loader.getController();
	}

	/**
	 * Shows the dialog as a blocking modal dialog, on the given owner window.
	 */
	public void showDialog(Window owner) {
		Stage stage = new Stage();
		stage.setTitle(title != null ? title : app().getName());
		stage.setScene(new Scene(root));
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(owner);
		stage.showAndWait();
	}
	
	
}
