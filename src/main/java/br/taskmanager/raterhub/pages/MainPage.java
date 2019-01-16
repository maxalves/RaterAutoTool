package br.taskmanager.raterhub.pages;

import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {

	private WebDriver driver;
	public String mainURL = "https://raterhub.com/evaluation/rater";

	public MainPage(WebDriver driver) {
		this.driver = driver;
	}

	public void autoRefreshUntilTask(boolean refresh, Double refreshRate) throws TimeoutException {
		if (refresh) {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofHours(24))
					.pollingEvery(Duration.ofSeconds(refreshRate.intValue())).ignoring(NoSuchElementException.class);

			wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					if (isMainPage()) {
						driver.navigate().refresh();
						return driver.findElement(By.className("ewok-rater-task-option"));
					} else {
						return null;
					}
				}
			});
		}
	}

	// Waits until task button shows even if autorefresh is disabled
	public void autoAcquireTask(boolean acquire) throws TimeoutException{
		if (acquire && isMainPage()) { 
			WebDriverWait wait = new WebDriverWait(driver,  10);
			System.out.println("esperando aparecer");
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul.ewok-rater-task-option>li>a")));
			driver.findElement(By.cssSelector("ul.ewok-rater-task-option>li>a")).click();
		}
	}

	// Check if it's a mainpage and returns false if it isn't
	private boolean isMainPage() {
		boolean taskURL = driver.getCurrentUrl().contains("/task/show/");
		boolean mainpageURL = driver.getCurrentUrl().contains("C:/Users/max_a/Desktop/HTMLPage");

		if (!taskURL && mainpageURL) {
			return true;
		} else {
			return false;
		}
	}
}