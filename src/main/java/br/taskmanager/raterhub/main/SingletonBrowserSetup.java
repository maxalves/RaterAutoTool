package br.taskmanager.raterhub.main;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SingletonBrowserSetup {
	
	private static SingletonBrowserSetup instance = null;
	private WebDriver driver;
	private String chromeDriverLocation = "C:/chromedriver.exe";
	private String port = "9222";
	
	private SingletonBrowserSetup() {
		
		//CMD Commands to run chrome browser
		openBrowserCMD();
		
		// WebDriver folder
		System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
		
		// Chrome setup and port
		ChromeOptions options = new ChromeOptions().setExperimentalOption("debuggerAddress", "127.0.0.1:"+ port);
		driver = new ChromeDriver(options);
		driver.get("C:/Users/max_a/Desktop/HTMLPage1.html");
	}

	public static SingletonBrowserSetup getInstance() {
		if (instance == null) {
			instance = new SingletonBrowserSetup();
		}
		return instance;
	}

	public WebDriver getDriver() {
		return driver;
	}

	private void openBrowserCMD() {
		try {
			String str ="start /d \"C:\\Program Files (x86)\\Google\\Chrome\\Application\\\" chrome.exe --remote-debugging-port=9222 --user-data-dir=\"C:\\selenium\\AutomationProfile\"";
		    Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c",str});
		} catch(Exception e) {
			System.out.println(e.toString());  
	        e.printStackTrace();  
		}
	}

}
