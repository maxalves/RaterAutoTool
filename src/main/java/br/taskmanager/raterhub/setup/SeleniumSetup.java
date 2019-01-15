package br.taskmanager.raterhub.setup;

import org.openqa.selenium.WebDriver;

import br.taskmanager.raterhub.pages.MainPage;

public class SeleniumSetup {
	private MainPage raterWebpage;
	private WebDriver driver;
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
		driver.close();
		driver.quit();
	}

	private void startAutomation() {
		try {
			// driver.get("C:/Users/max_a/Desktop/HTMLPage1.html");
			driver.get("http://www.raterhub.com");
			raterWebpage = new MainPage(driver);
			raterWebpage.autoRefreshUntilTask(refresh, refreshRate);
			raterWebpage.autoAcquireTask(acquire);
		} catch (RuntimeException e) {
			closeSelenium();
			System.out.println("Erro na automaçao");
		}
	}
}
