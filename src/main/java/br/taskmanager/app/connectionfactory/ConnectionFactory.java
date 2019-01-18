package br.taskmanager.app.connectionfactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
	public static Connection connector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:ratertool_db.db");
			return con;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
