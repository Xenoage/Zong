package com.xenoage.zong.player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayerApp2
	extends Application {

	public static void main(String... args) {
		PlayerApp2.launch(args);
	}

	@Override public void start(Stage stage)
		throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("../gui/Player.fxml"));

		Scene scene = new Scene(root);

		stage.setTitle("JavaFX Symbols Test");
		stage.setScene(scene);
		stage.show();
	}

}
