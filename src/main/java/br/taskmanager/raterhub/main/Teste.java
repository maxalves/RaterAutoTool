package br.taskmanager.raterhub.main;

import org.openqa.selenium.WebDriver;

import br.taskmanager.raterhub.pages.MainPage;

public class Teste {

	private static MainPage raterWebpage;
	
	public static void main(String[] args) {
		
		
		SingletonBrowserSetup instance = SingletonBrowserSetup.getInstance();
		WebDriver driver = instance.getDriver();
		
		raterWebpage = new MainPage(driver);
		raterWebpage.autoRefreshUntilTask();
		raterWebpage.autoAcquireTask();
		driver.quit();
	}
}