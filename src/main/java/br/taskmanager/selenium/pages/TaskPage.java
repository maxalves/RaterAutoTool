package br.taskmanager.selenium.pages;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.taskmanager.app.model.Task;

public class TaskPage {

	private WebDriver driver;

	public TaskPage(WebDriver driver) {
		this.driver = driver;
	}

	public Task getTaskData() throws NoSuchElementException {
		waitForLoad(driver);
		Task task = new Task();
		Long taskId = Long.parseLong(driver.findElement(By.id("taskIds")).getAttribute("value"));
		String taskType = driver.findElement(By.cssSelector("ul.ewok-task-action-header>li:nth-child(2)")).getText();
		String taskTime = driver
				.findElement(By.cssSelector("ul.ewok-task-action-header span.ewok-estimated-task-weight")).getText();
		task.setId(taskId);
		task.setTime(formatAsDouble(taskTime));
		task.setType(taskType);

		return task;
	}

	public void openTaskLinks(boolean openLinks) throws NoSuchElementException {
		if (openLinks) {
			waitForLoad(driver);
			List<WebElement> items = driver.findElements(By.cssSelector(".ewok-buds-result-html>div>a:nth-child(1)"));
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			String url;

			for (WebElement element : items) {
				url = element.getAttribute("old-href");
				if (url != null) {
					((JavascriptExecutor) driver).executeScript("window.open('" + url + "');");

					if (items.size() > 10) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			driver.switchTo().window(tabs.get(0));
		}
	}

	public void submit() throws NoSuchElementException {
			driver.findElement(By.id("ewok-task-submit-button")).sendKeys(Keys.ENTER);
	}
	
	public String getCurrentTime() throws NoSuchElementException {
		return driver.findElement(By.className("ewok-rater-progress-bar-timer-digital-display")).getText();
	}
	
	//Convert task time to double ex: 8.9m task should be 8m and 54sec
	private Double formatAsDouble(String taskTime) {
		taskTime = taskTime.replaceAll("[^\\.0123456789]", "");
		String[] array = taskTime.split("\\.", -1);
		Double seconds = Double.parseDouble("0." + array[1]) * 60;
		array[1] = String.valueOf(seconds.intValue());
		
		Double time = Double.parseDouble(String.join(".", array));
		time = BigDecimal.valueOf(time).setScale(2, RoundingMode.HALF_UP).doubleValue();
		
		return time;
	}

	private void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(pageLoadCondition);
	}
}
