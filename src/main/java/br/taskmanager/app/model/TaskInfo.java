package br.taskmanager.app.model;

import java.time.LocalDate;

public class TaskInfo {

	private LocalDate date;
	private String type;
	private int numberCompleted;
	private Double totalTime;
	
	public int getNumberCompleted() {
		return numberCompleted;
	}

	public void setNumberCompleted(int numberCompleted) {
		this.numberCompleted = numberCompleted;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Double totalTime) {
		this.totalTime = totalTime;
	}

}
