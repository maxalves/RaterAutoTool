package br.taskmanager.app.main;

import br.taskmanager.app.controller.ConfigController;
import javafx.application.Application;

public class Main {
	
	public static ConfigController config = new ConfigController();
	
	public static void main(String[] args) {
		Application.launch(AppUI.class, args);
	}
}
