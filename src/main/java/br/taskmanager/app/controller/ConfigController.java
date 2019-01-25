package br.taskmanager.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import br.taskmanager.selenium.setup.SeleniumStartup;
import br.taskmanager.selenium.setup.SingletonBrowserSetup;
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
	private String chromeDriverLocation = "C:\\chromedriver.exe";
	private String chromeExeLocation = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\\"";
	private String userDataDirLocation = "C:\\selenium\\AutomationProfile";
	private String port = "9222";
	
	private SeleniumStartup seleniumSetup = new SeleniumStartup();
	SingletonBrowserSetup instance;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		instance = SingletonBrowserSetup.getInstance(chromeDriverLocation, chromeExeLocation,
				userDataDirLocation, port);
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

	// thread responsible to send configuration data to SeleniumStartup.class
	Service<Void> sendConfigData = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					seleniumSetup.seleniumWebDriverConfig(instance);
					return null;
				}
			};
		}
	};
}
