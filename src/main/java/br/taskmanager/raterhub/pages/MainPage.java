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

	private WebDriver driver;

	public MainPage(WebDriver driver) {
		this.driver = driver;
	}

	public void autoRefreshUntilTask(boolean refresh, Double refreshRate) {

		if (refresh) {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofHours(24))
					.pollingEvery(Duration.ofSeconds(refreshRate.intValue())).ignoring(NoSuchElementException.class);

			WebElement taskAvailable = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					driver.navigate().refresh();
					return driver.findElement(By.className("ewok-rater-task-option"));
				}
			});
		}
	}

	public void autoAcquireTask(boolean acquire) {
		if (acquire) {
			driver.findElement(By.cssSelector("ul.ewok-rater-task-option>li>a")).click();
		}
	}
}