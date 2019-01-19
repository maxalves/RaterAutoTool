package br.taskmanager.app.dao;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import br.taskmanager.app.connectionfactory.ConnectionFactory;
import br.taskmanager.app.model.TaskInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class TaskTeste implements Initializable{
	Connection connection = ConnectionFactory.connector();
	public TaskDao taskdao = new TaskDao(connection);
	
	@FXML
	private Label isConnected;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/*Task task = new Task();
		task.setId(949);
		task.setTime(5.0);
		task.setType("Side by Side");
		
		taskdao.add(task);*/
		
		try {
			List<TaskInfo> lista = taskdao.getSummary();
			
			for(TaskInfo task : lista) {
				System.out.println(task.getDate() + " -> " + task.getType() + " -> " + task.getNumberCompleted() + " -> " + task.getTotalTime());
			}
			System.out.println(taskdao.sumMonth());
		} catch (Exception e) {
			System.out.println("deu errado");
			e.printStackTrace();
		}
		if(taskdao.isDbConnected()) {
			isConnected.setText("Connected");
		} else {
			isConnected.setText("Not Connected");
		}
		
	}
	
}
