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
	
	private boolean ToggleBtn1, ToggleBtn2, ToggleBtn3, ToggleBtn4;
	private double refreshRateSlider, submitPercentageSlider;
	
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
		refreshRateSlider = refreshSlider.getValue();
		submitPercentageSlider = submitSlider.getValue();
		ToggleBtn1 = refreshToggle.isSelected();
		ToggleBtn2 = acquireToggle.isSelected();
		ToggleBtn3 = openLinksToggle.isSelected();
		ToggleBtn4 = submitToggle.isSelected();
	}

	public boolean isToggleBtn1() {
		return ToggleBtn1;
	}

	public boolean isToggleBtn2() {
		return ToggleBtn2;
	}

	public boolean isToggleBtn3() {
		return ToggleBtn3;
	}

	public boolean isToggleBtn4() {
		return ToggleBtn4;
	}

	public double getRefreshRateSlider() {
		return refreshRateSlider;
	}

	public double getSubmitPercentageSlider() {
		return submitPercentageSlider;
	}


}
