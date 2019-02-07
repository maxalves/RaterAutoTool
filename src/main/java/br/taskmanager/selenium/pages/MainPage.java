package br.taskmanager.selenium.pages;

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

	public MainPage(WebDriver driver) {
		this.driver = driver;
	}

	public void autoRefreshUntilTask(boolean refresh, Double refreshRate) throws TimeoutException {
		if (refresh) {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(Duration.ofSeconds(refreshRate.intValue() * 2))
					.pollingEvery(Duration.ofSeconds(refreshRate.intValue())).ignoring(NoSuchElementException.class);

			wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					if (driver.findElement(By.cssSelector("div[class='container'] ul")) == null) {
						driver.navigate().refresh();
					}

					return driver.findElement(By.cssSelector("div[class='container'] ul"));
				}
			});
		}
	}

	// Waits until task button shows even if autorefresh is disabled
	public void autoAcquireTask(boolean acquire) throws TimeoutException {
		if (acquire) {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			Toolkit.getDefaultToolkit().beep();
			wait.until(ExpectedConditions
					.elementToBeClickable(By.cssSelector("div[class='container'] ul.ewok-rater-task-option>li>a")));
			driver.findElement(By.cssSelector("div[class='container'] ul.ewok-rater-task-option>li>a"))
					.sendKeys(Keys.ENTER);
		}
	}
}