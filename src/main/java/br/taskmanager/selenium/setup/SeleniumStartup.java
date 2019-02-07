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

import br.taskmanager.app.connectionfactory.ConnectionFactory;
import br.taskmanager.app.dao.TaskDao;
import br.taskmanager.app.model.Task;
import br.taskmanager.selenium.pages.MainPage;
import br.taskmanager.selenium.pages.TaskPage;

public class SeleniumStartup {
	Connection connection = ConnectionFactory.connector();
	private TaskDao taskDao = new TaskDao(connection);
	private MainPage mainPage;
	private TaskPage taskPage;
	private WebDriver driver;
	private boolean refresh, acquire, openTaskLinks, submit;
	private Double refreshRate, submitPercentage;
	private String chromeDriverLocation = "C:\\chromedriver.exe";
	private String chromeExeLocation = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\\"";
	private String userDataDirLocation = "C:\\selenium\\AutomationProfile";
	private String port = "9222";
	private String mainURL = "https://www.raterhub.com/evaluation/rater";

	public void seleniumWebDriverConfig(boolean refresh, boolean acquire, boolean openTaskLinks, boolean submit,
			Double refreshRate, Double submitPercentage) {
		this.refresh = refresh;
		this.acquire = acquire;
		this.openTaskLinks = openTaskLinks;
		this.submit = submit;
		this.refreshRate = refreshRate;
		this.submitPercentage = submitPercentage;
		SingletonBrowserSetup instance = SingletonBrowserSetup.getInstance(chromeDriverLocation, chromeExeLocation,
				userDataDirLocation, port);
		driver = instance.getDriver();
		mainPage = new MainPage(driver);
		taskPage = new TaskPage(driver);
		driver.get(mainURL);

		startAutomation();
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

	public WebDriver getDriver() {
		return driver;
	}

	private void startAutomation() {
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
