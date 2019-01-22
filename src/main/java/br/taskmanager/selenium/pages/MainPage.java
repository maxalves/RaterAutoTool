package br.taskmanager.raterhub.pages;

import java.awt.Toolkit;
import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
	private String selectorTaskAvailable = "div[class='container'] ul";
	private String selectorAcquire = "div[class='container'] ul.ewok-rater-task-option>li>a";
	public String mainURL = "https://www.raterhub.com/evaluation/rater";

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
						if (driver.findElements(By.cssSelector(selectorTaskAvailable)).size() == 0) {
							driver.navigate().refresh();
						}
						return driver.findElement(By.cssSelector(selectorTaskAvailable));
					} else {
						return null;
					}
				}
			});
		}
	}

	// Waits until task button shows even if autorefresh is disabled
	public void autoAcquireTask(boolean acquire) throws TimeoutException {
		if (acquire && isMainPage()) {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			Toolkit.getDefaultToolkit().beep();
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selectorAcquire)));
			driver.findElement(By.cssSelector(selectorAcquire)).sendKeys(Keys.ENTER);
		}
	}

	// Check if it's a mainpage and returns false if it isn't
	private boolean isMainPage() {
		boolean taskURL = driver.getCurrentUrl().contains("/task/show");
		boolean taskURL2 = driver.getCurrentUrl().contains("/task/new");
		boolean mainpageURL = driver.getCurrentUrl().contains(mainURL);

		if (!taskURL && mainpageURL && !taskURL2) {
			return true;
		} else {
			return false;
		}
	}
}