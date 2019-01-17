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
		try {
			driver.close();
		} catch (RuntimeException e) {
			System.out.println("Driver is already closed!");
			Thread.currentThread().interrupt();
		}

	}

	public WebDriver getDriver() {
		return driver;
	}

	private void startAutomation() {
		raterWebpage = new MainPage(driver);
		driver.get(raterWebpage.mainURL);

		for (;;) {
			try {
				raterWebpage.autoRefreshUntilTask(refresh, refreshRate);
				raterWebpage.autoAcquireTask(acquire);
				waitAndSync();
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
}
