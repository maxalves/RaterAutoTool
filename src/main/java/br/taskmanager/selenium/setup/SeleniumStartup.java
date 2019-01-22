package br.taskmanager.selenium.setup;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import br.taskmanager.app.model.Task;
import br.taskmanager.selenium.pages.MainPage;
import br.taskmanager.selenium.pages.TaskPage;

public class SeleniumStartup {
	private MainPage mainPage;
	private TaskPage taskPage;
	private WebDriver driver = null;
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
		mainPage = new MainPage(driver, mainURL);
		taskPage = new TaskPage(driver, mainURL);
		driver.get(mainURL);
		
		startAutomation();
	}

	public void closeSelenium() {
		try {
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
				driver.close();
			}
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
					System.out.println("teste");
					mainPage.autoRefreshUntilTask(refresh, refreshRate);
					System.out.println("teste2");
					mainPage.autoAcquireTask(acquire);
					waitAndSync();
				} else if (whatPageIamAt() == "taskpage") {
					
				} else {
					waitAndSync();
				}
			} catch (TimeoutException e) {
				System.out.println("Timeout -> Retry");
				waitAndSync();
			} catch (WebDriverException e) {
				System.out.println("Selenium Stopped -> Retry");
				waitAndSync();
			}
		}
	}
	
	private String whatPageIamAt() {
		boolean taskURL = driver.getCurrentUrl().contains("/task/show");
		boolean taskURL2 = driver.getCurrentUrl().contains("/task/new");
		boolean mainpageURL = driver.getCurrentUrl().contains(mainURL);

		if (!taskURL && mainpageURL && !taskURL2) {
			return "mainpage";
		} else if (taskURL && mainpageURL){
			return "taskpage";
		} else {
			return null;
		}
	}

	private void waitAndSync() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}
}
