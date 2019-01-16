package br.taskmanager.raterhub.setup;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import br.taskmanager.raterhub.pages.MainPage;

public class SeleniumSetup {
	private MainPage raterWebpage;
	private WebDriver driver = null;
	private boolean refresh, acquire, openTaskLinks, submit;
	private Double refreshRate, submitPercentage;
	private String chromeDriverLocation = "C:\\chromedriver.exe";
	private String chromeExeLocation = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\\"";
	private String userDataDirLocation = "C:\\selenium\\AutomationProfile";
	private String port = "9222";

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

		startAutomation();
	}

	public void closeSelenium() {

		if (driver != null) {
			driver.close();
		}
	}

	public WebDriver getDriver() {
		return driver;
	}

	private void startAutomation() {
		System.out.println(driver.toString());
		raterWebpage = new MainPage(driver);
		driver.get(raterWebpage.mainURL);
		System.out.println(driver.toString());
		for (;;) {
			try {
				raterWebpage.autoRefreshUntilTask(refresh, refreshRate);
				raterWebpage.autoAcquireTask(acquire);
			} catch (TimeoutException e) {
				System.out.println("Automation Timeout");
				waitAndSync();
			} catch (WebDriverException e) {
				System.out.println("Selenium Thread Stopped");
				waitAndSync();
			}
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

	// Check if it's a mainpage and returns false if it isn't
	private boolean isMainPage() {
		boolean taskURL = driver.getCurrentUrl().contains("/task/show/");
		boolean mainpageURL = driver.getCurrentUrl().contains("https://www.raterhub.com/evaluation/rater");

		if (!taskURL && mainpageURL) {
			return true;
		} else {
			return false;
		}
	}
}
