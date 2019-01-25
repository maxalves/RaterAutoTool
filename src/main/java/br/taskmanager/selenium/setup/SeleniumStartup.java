package br.taskmanager.selenium.setup;

import java.awt.Toolkit;
import java.sql.Connection;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import br.taskmanager.app.connectionfactory.ConnectionFactory;
import br.taskmanager.app.controller.ConfigController;
import br.taskmanager.app.dao.TaskDao;
import br.taskmanager.app.model.Task;
import br.taskmanager.selenium.pages.MainPage;
import br.taskmanager.selenium.pages.TaskPage;
import javafx.fxml.FXML;

public class SeleniumStartup {
	private MainPage mainPage;
	private TaskPage taskPage;
	private WebDriver driver;
	
	private String mainURL = "https://www.raterhub.com/evaluation/rater";

	public void seleniumWebDriverConfig(SingletonBrowserSetup instance) {
		driver = instance.getDriver();
	}

	public void closeSelenium() {
		try {
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
				driver.close();
			}
			driver.quit();
		} catch (RuntimeException e) {
			System.out.println("Driver is closed!");
			Thread.currentThread().interrupt();
		}

	}

	public void startAutomation() {
		Connection connection = ConnectionFactory.connector();
		TaskDao taskDao = new TaskDao(connection);
		mainPage = new MainPage(driver);
		taskPage = new TaskPage(driver);
		driver.get(mainURL);
		
		for (;;) {
			try {
				if (whatPageIamAt() == "mainpage") {
					mainPage.autoRefreshUntilTask(refresh, refreshRate);
					mainPage.autoAcquireTask(acquire);
				} else if (true) {
					Task task = new Task();
					task = taskPage.getTaskData();
					if (!isTaskAlreadyCompleted(task)) {
						taskDao.add(task);
						taskPage.openTaskLinks(openTaskLinks);
						if (submit) {
							System.out.println("wait submit");
							waitUntilSubmitTime(task);
						}
					}
				}
				waitAndSync();
			} catch (NoSuchElementException e) {
				System.out.println("Element not Found");
				waitAndSync();
			} catch (TimeoutException e) {
				System.out.println("Timeout -> Retry");
				waitAndSync();
			} catch (WebDriverException e) {
				System.out.println("Selenium Stopped -> Retry");
				waitAndSync();
			} catch (RuntimeException e) {
				System.out.println("Error! -> Close Selenium");
				closeSelenium();
			}
		}
	}

	private void waitUntilSubmitTime(Task task) {
		
		ExpectedCondition<Boolean> timeCondition = new ExpectedCondition<Boolean>() {
			boolean alreadySubmitted = false;
			public Boolean apply(WebDriver driver) {
				try {
					if (formatTimeAsDouble(taskPage.getCurrentTime()) >= considerSubmitPercentage(task.getTime()) && alreadySubmitted == false) {
						System.out.println("Autosubmit Task!");
						Toolkit.getDefaultToolkit().beep();
						taskPage.submit();
						alreadySubmitted = true;
						return alreadySubmitted;
					} else if (driver.getCurrentUrl().contains(String.valueOf(task.getId()))) {
						System.out.println("still same page");
						// if i am still in the same page then user didn't submit yet
						return false;
					} else {
						System.out.println("User submitted task!");
						return true;
					}
				} catch (NoSuchElementException e) {
					System.out.println("User submitted task!");
					return true;
				}
			}
		};

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofMinutes(task.getTime().intValue() + 1)).pollingEvery(Duration.ofSeconds(1));

		wait.until(timeCondition);
	}

	private boolean isTaskAlreadyCompleted(Task task) {
		List<Task> listThreeTasks = taskDao.getlastThreeTasksAdded();
		for (Task tasks : listThreeTasks) {
			if (tasks.getId() == task.getId()) {
				return true;
			}
		}
		return false;
	}

	private String whatPageIamAt() {
		boolean taskURL = driver.getCurrentUrl().contains("/task/show");
		boolean taskURL2 = driver.getCurrentUrl().contains("/task/new");
		boolean mainpageURL = driver.getCurrentUrl().contains(mainURL);

		if (!taskURL && mainpageURL && !taskURL2) {
			return "mainpage";
		} else if (taskURL && mainpageURL) {
			return "taskpage";
		} else {
			return null;
		}
	}

	private Double formatTimeAsDouble(String currentTime) {
		currentTime = currentTime.replaceAll(":", ".");
		Double timeAsDouble = Double.parseDouble(currentTime);
		return timeAsDouble;
	}

	private Double considerSubmitPercentage(Double timeToSubmit) {
		timeToSubmit = (submitPercentage * timeToSubmit) / 100;
		return timeToSubmit;
	}

	private void waitAndSync() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}
}
