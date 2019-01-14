package br.taskmanager.raterhub.main;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import br.taskmanager.raterhub.mainpage.MainPage;

public class BrowserSetup {
	
	static WebDriver driver;
	static MainPage raterWebpage; 

	public static void main(String[] args) {

		/*
		 * Instructions to open browser C:\Program Files (x86)\Google\Chrome\Application
		 * chrome.exe --remote-debugging-port=9222
		 * --user-data-dir="C:\selenium\AutomationProfile"
		 */

		// Driver folder
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");

		// Chrome setup and port
		ChromeOptions options = new ChromeOptions().setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
		WebDriver driver = new ChromeDriver(options);

		// Raterhub website & setup
		// driver.get("https://www.raterhub.com/evaluation/rater");
		driver.get("C:/Users/max_a/Desktop/HTMLPage1.html");
		raterWebpage = new MainPage(driver);
		raterWebpage.autoRefreshUntilTask();
		raterWebpage.autoAcquireTask();
		driver.quit();
	}

}
