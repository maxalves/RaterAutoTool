package br.taskmanager.raterhub.setup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SingletonBrowserSetup {
	
	private static SingletonBrowserSetup instance = null;
	
	private WebDriver driver;
	private static String chromeDriverLocation;
	private static String chromeExeLocation;
	private static String userDataDirLocation;
	private static String port;
	
	private SingletonBrowserSetup() {
		
		//CMD Commands to run chrome browser
		openBrowserCMD();
		
		// Chrome setup, location and port
		System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
		ChromeOptions options = new ChromeOptions().setExperimentalOption("debuggerAddress", "127.0.0.1:" + port);
		driver = new ChromeDriver(options);
		
		//test page
		driver.get("C:/Users/max_a/Desktop/HTMLPage1.html");
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	public static SingletonBrowserSetup getInstance(String chromeDriverLocation, String chromeExeLocation, String userDataDirLocation, String port) {
		
		if (instance == null) {
			SingletonBrowserSetup.chromeDriverLocation = chromeDriverLocation;
			SingletonBrowserSetup.chromeExeLocation = chromeExeLocation;
			SingletonBrowserSetup.userDataDirLocation = userDataDirLocation;
			SingletonBrowserSetup.port = port;
			instance = new SingletonBrowserSetup();
		}
		return instance;
	}
	
	private void openBrowserCMD() {
		try {
			String str ="start /d \""+ chromeExeLocation + " chrome.exe --remote-debugging-port=" + port + " --user-data-dir=" + "\"" + userDataDirLocation;
		    Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c",str});
		} catch(Exception e) {
			System.out.println(e.toString());  
	        e.printStackTrace();  
		}
	}
}
