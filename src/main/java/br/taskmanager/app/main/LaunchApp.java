package br.taskmanager.app.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LaunchApp extends Application {

	public static Stage stage = null;

	@Override
	public void start(Stage stage) throws Exception {
		//Parent root = FXMLLoader.load(getClass().getResource("/br/taskmanager/app/ui/UI.fxml"));
		Parent root = FXMLLoader.load(getClass().getResource("/br/taskmanager/app/dao/taskteste.fxml"));
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		LaunchApp.stage = stage;
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
