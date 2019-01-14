package br.taskmanager.raterhub.pages;

import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class MainPage {

	private static WebDriver driver;

	public MainPage(WebDriver driver) {
		this.driver = driver;
	}

	public void autoRefreshUntilTask() {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofHours(24))
				.pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);

		@SuppressWarnings("unused")
		WebElement taskAvailable = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				System.out.println("refresh");
				driver.navigate().refresh();
				return driver.findElement(By.className("ewok-rater-task-option"));
			}
		});

	}

	public void autoAcquireTask() {
		driver.findElement(By.cssSelector("ul.ewok-rater-task-option>li>a")).click();
	}
}