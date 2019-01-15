package br.taskmanager.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import br.taskmanager.raterhub.setup.SeleniumSetup;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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

	private SeleniumSetup seleniumSetup = new SeleniumSetup();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		refreshSlider.setValue(10);
		submitSlider.setValue(90);
	}

	@FXML
	private void closeBtn(MouseEvent event) {
		((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
		seleniumSetup.closeSelenium();
	}

	@FXML
	private void saveBtn(MouseEvent event) {
		sendConfigData.restart();
	}

	// thread responsible to send configuration data to SeleniumSetup.class
	Service<Void> sendConfigData = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					seleniumSetup.seleniumWebDriverConfig(refreshToggle.isSelected(), acquireToggle.isSelected(),
							openLinksToggle.isSelected(), submitToggle.isSelected(), refreshSlider.getValue(),
							submitSlider.getValue());
					return null;
				}
			};
		}
	};
}
