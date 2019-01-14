package br.taskmanager.raterhub.main;

import org.openqa.selenium.WebDriver;

import br.taskmanager.raterhub.pages.MainPage;

public class Teste {

	private static MainPage raterWebpage;
	
	public static void main(String[] args) {
		
		SingletonBrowserSetup instance = SingletonBrowserSetup.getInstance("C:\\chromedriver.exe", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\\"", "C:\\selenium\\AutomationProfile", "9222");
		WebDriver driver = instance.getDriver();
		
		raterWebpage = new MainPage(driver);
		raterWebpage.autoRefreshUntilTask();
		raterWebpage.autoAcquireTask();
		System.out.println(SingletonBrowserSetup.getChromeDriverLocation());
		driver.quit();
	}
}
