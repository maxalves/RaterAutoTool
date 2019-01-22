package br.taskmanager.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.taskmanager.app.model.Task;

public class TaskPage {

	private WebDriver driver;

	public TaskPage(WebDriver driver, String mainURL) {
		this.driver = driver;
	}

	public Task getTaskData() throws TimeoutException {
		waitForLoad(driver);
		Task task = new Task();
		Long taskId = Long.parseLong(driver.findElement(By.id("taskIds")).getAttribute("value"));
		String taskType = driver.findElement(By.cssSelector("ul.ewok-task-action-header>li:nth-child(2)")).getText();
		String taskTimeTxt = driver
				.findElement(By.cssSelector("ul.ewok-task-action-header span.ewok-estimated-task-weight")).getText();
		Double taskTime = Double.parseDouble(taskTimeTxt.replaceAll("[^\\.0123456789]", ""));

		task.setId(taskId);
		task.setTime(taskTime);
		task.setType(taskType);

		return task;
	}

	public void openTaskLinks() {

	}

	public void autoSubmit(boolean submit, Double submitPercentage) {

	}
	
	public void confirmDuplicate() {
		try {
			driver.findElement(By.name("nomoredupes")).click();
		} catch (NoSuchElementException e) {
			System.out.println("Confirm Dupes Not Needed");
		}
	}
	
	private void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(pageLoadCondition);
	}
}
