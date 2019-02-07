package br.taskmanager.app.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import br.taskmanager.app.connectionfactory.ConnectionFactory;
import br.taskmanager.app.dao.TaskDao;
import br.taskmanager.app.main.LaunchApp;
import br.taskmanager.app.model.TaskInfo;
import br.taskmanager.selenium.setup.SeleniumStartup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UIController implements Initializable {

	@FXML
	private Pane pane;
	@FXML
	private AnchorPane parent;
	@FXML
	private TableView<TaskInfo> table;
	@FXML
	private TableColumn<TaskInfo, LocalDate> dateCol;
	@FXML
	private TableColumn<TaskInfo, String> typeCol;
	@FXML
	private TableColumn<TaskInfo, Integer> numberCol;
	@FXML
	private TableColumn<TaskInfo, Double> timeCol;
	@FXML
	private Label sumWeek, sumMonth;
	@FXML
	private JFXSlider refreshSlider, submitSlider;
	@FXML
	private JFXToggleButton refreshToggle, acquireToggle, openLinksToggle, submitToggle;

	private SeleniumStartup seleniumSetup = new SeleniumStartup();
	Connection connection = ConnectionFactory.connector();
	private TaskDao taskDao = new TaskDao(connection);

	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		makeStageDrageable();
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		numberCol.setCellValueFactory(new PropertyValueFactory<>("numberCompleted"));
		timeCol.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
		sumWeek.setText(String.valueOf(getSumWeek()));
		sumMonth.setText(String.valueOf(getSumMonth()));
		updateTime.start();
	}

	Service<Void> updateTime = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					for (;;) {
						table.setItems(listTaskInfo());
						sumWeek.setText(String.valueOf(getSumWeek()));
						sumMonth.setText(String.valueOf(getSumMonth()));
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}
	};

	private Double getSumWeek() {
		return taskDao.sumWeek();
	}

	private Double getSumMonth() {
		return taskDao.sumMonth();
	}

	private ObservableList<TaskInfo> listTaskInfo() {
		try {
			return FXCollections.observableArrayList(taskDao.getSummary());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
					seleniumSetup.seleniumWebDriverConfig(refreshToggle.isSelected(), acquireToggle.isSelected(),
							openLinksToggle.isSelected(), submitToggle.isSelected(), refreshSlider.getValue(),
							submitSlider.getValue());
					return null;
				}
			};
		}
	};

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
				LaunchApp.stage.setX(event.getScreenX() - xOffset);
				LaunchApp.stage.setY(event.getScreenY() - yOffset);
				LaunchApp.stage.setOpacity(0.7f);
			}
		});
		parent.setOnDragDone((e) -> {
			LaunchApp.stage.setOpacity(1.0f);
		});
		parent.setOnMouseReleased((e) -> {
			LaunchApp.stage.setOpacity(1.0f);
		});

	}
}
