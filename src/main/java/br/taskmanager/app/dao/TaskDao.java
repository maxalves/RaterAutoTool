package br.taskmanager.app.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import br.taskmanager.app.model.Task;
import br.taskmanager.app.model.TaskInfo;

public class TaskDao {
	private Connection connection;

	public TaskDao(Connection connection) {
		this.connection = connection;
	}

	public void add(Task task) {
		String sql = "insert into task " + "(taskid, type,time,date) " + " values(?,?,?,CURRENT_DATE)";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setLong(1, task.getId());
			stmt.setString(2, task.getType());
			stmt.setDouble(3, task.getTime());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Task> getList() throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement("select * from task order by date");
			ResultSet rs = stmt.executeQuery();
			List<Task> tasks = new ArrayList<Task>();
			while (rs.next()) {
				Task task = new Task();
				task.setId(rs.getLong("taskid"));
				task.setType(rs.getString("type"));
				task.setTime(rs.getDouble("time"));
				String date = rs.getString("date");
				task.setDate(convertToLocalDate(date));
				tasks.add(task);
			}

			rs.close();
			stmt.close();
			return tasks;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<TaskInfo> getSummary() throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT date, type, count(type) as numberCompleted, sum(time) as totalTime FROM task \r\n"
							+ "WHERE date BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')\r\n"
							+ "group by date, type\r\n" + "order by date");
			ResultSet rs = stmt.executeQuery();
			List<TaskInfo> tasks = new ArrayList<TaskInfo>();
			while (rs.next()) {
				TaskInfo taskInfo = new TaskInfo();
				String date = rs.getString("date");
				taskInfo.setDate(convertToLocalDate(date));
				taskInfo.setType(rs.getString("type"));
				taskInfo.setNumberCompleted(rs.getInt("numberCompleted"));
				taskInfo.setTotalTime(roundDouble(rs.getDouble("totalTime")));
				tasks.add(taskInfo);
			}
			rs.close();
			stmt.close();
			return tasks;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Task> getlastThreeTasksAdded(){
		try {
			PreparedStatement stmt = connection.prepareStatement("select * from task order by id desc limit 3");
			ResultSet rs = stmt.executeQuery();
			List<Task> tasks = new ArrayList<Task>();
			while (rs.next()) {
				Task task = new Task();
				String date = rs.getString("date");
				task.setDate(convertToLocalDate(date));
				task.setType(rs.getString("type"));
				task.setTime(rs.getDouble("time"));
				task.setId(rs.getLong("taskid"));
				tasks.add(task);
			}
			rs.close();
			stmt.close();
			return tasks;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	public Double sumWeek() {
		String sql = "select sum(time) as weekTime from task where date BETWEEN date('now', 'weekday 0', '-7 days') AND datetime('now', 'localtime')";
		Double value;

		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			value = rs.getDouble("weekTime");
			rs.close();
			stmt.close();
			return roundDouble(value);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public Double sumMonth() {
		String sql = "SELECT sum(time) as monthTime FROM task WHERE date BETWEEN datetime('now', 'start of month', '-1 days') AND datetime('now', 'localtime', '+1 days')";
		Double value;
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			value = rs.getDouble("monthTime");
			rs.close();
			stmt.close();
			return roundDouble(value);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	private Double roundDouble(Double value) {
		value = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
		return value;
	}

	private LocalDate convertToLocalDate(String dateToConvert) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = format.parse(dateToConvert);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
