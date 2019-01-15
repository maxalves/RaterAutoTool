package br.taskmanager.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ConfigController implements Initializable {

	@FXML
	private JFXSlider refreshSlider, submitSlider;
	@FXML
	private JFXToggleButton refreshToggle, acquireToggle, openLinksToggle, submitToggle;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		refreshSlider.setValue(10);
		submitSlider.setValue(90);
	}

	@FXML
	private void closeBtn(MouseEvent event) {
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}
	
	@FXML
	private void saveBtn(MouseEvent event) {
	}
}
