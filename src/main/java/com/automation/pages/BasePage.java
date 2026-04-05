package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    protected WebElement findElement(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                return element;
            }
        }
        if (!elements.isEmpty()) {
            return elements.get(0);
        }
        throw new NoSuchElementException("No element found for locator: " + locator);
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    protected void waitForVisible(By locator) {
        wait.until(d -> {
            List<WebElement> elements = d.findElements(locator);
            return elements.stream().anyMatch(WebElement::isDisplayed);
        });
    }

    protected void waitForClickable(By locator) {
        wait.until(d -> {
            List<WebElement> elements = d.findElements(locator);
            return elements.stream().anyMatch(element -> element.isDisplayed() && element.isEnabled());
        });
    }

    protected void waitForListNotEmpty(List<WebElement> elements) {
        wait.until(d -> elements.size() > 0);
    }

//    protected void click(WebElement element) {
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);;
//        waitForClickable(element);
//        element.click();
//    }

    protected void click(By locator) {
        scrollToElement(locator);
        waitForClickable(locator);
        WebElement element = findElement(locator);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void sendKeys(By locator, String text) {
        waitForVisible(locator);
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        waitForVisible(locator);
        return findElement(locator).getText();
    }

    protected String getValue(By locator) {
        waitForVisible(locator);
        return findElement(locator).getAttribute("value");
    }

    protected boolean isDisplayed(By locator) {
        try {
            return findElements(locator).stream().anyMatch(WebElement::isDisplayed);
        } catch (Exception e) {
            return false;
        }
    }

    protected String getAttribute(By locator, String name) {
        return findElement(locator).getAttribute(name);
    }

    protected void scrollToElement(By locator) {
        waitForVisible(locator);
        WebElement element = findElement(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }
}
