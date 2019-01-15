package br.taskmanager.app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import br.taskmanager.app.main.AppUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class UIController implements Initializable {
	@FXML
	private Pane pane;
	@FXML
	private AnchorPane parent;

	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		makeStageDrageable();
	}

	private void makeStageDrageable() {
		parent.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				AppUI.stage.setX(event.getScreenX() - xOffset);
				AppUI.stage.setY(event.getScreenY() - yOffset);
				AppUI.stage.setOpacity(0.7f);
			}
		});
		parent.setOnDragDone((e) -> {
			AppUI.stage.setOpacity(1.0f);
		});
		parent.setOnMouseReleased((e) -> {
			AppUI.stage.setOpacity(1.0f);
		});

	}

	@FXML
	private void open_config(MouseEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/br/taskmanager/app/ui/ConfigTab.fxml"));
		pane.getChildren().removeAll();
		pane.getChildren().setAll(fxml);
	}

	@FXML
	private void open_time_data(MouseEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/br/taskmanager/app/ui/TimeDataTab.fxml"));
		pane.getChildren().removeAll();
		pane.getChildren().setAll(fxml);
	}

}
