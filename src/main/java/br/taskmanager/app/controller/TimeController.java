package br.taskmanager.app.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import br.taskmanager.app.connectionfactory.ConnectionFactory;
import br.taskmanager.app.dao.TaskDao;
import br.taskmanager.app.model.TaskInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TimeController implements Initializable {

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

	Connection connection = ConnectionFactory.connector();
	private TaskDao taskDao = new TaskDao(connection);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
					update();
					return null;
				}
			};
		}
	};

	private void update() {
		for(;;) {
			table.setItems(listTaskInfo());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

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

}
