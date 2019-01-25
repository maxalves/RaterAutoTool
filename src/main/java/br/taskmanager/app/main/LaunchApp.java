package br.taskmanager.app.main;

import br.taskmanager.selenium.setup.SingletonBrowserSetup;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class LaunchApp extends Application {

	public static Stage stage = null;

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/br/taskmanager/app/ui/UI.fxml"));
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		LaunchApp.stage = stage;
		stage.show();
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	SingletonBrowserSetup.getDriver().quit();
                Platform.exit();
            }

        });
	}

	public static void main(String[] args) {
		launch(args);
	}
}
