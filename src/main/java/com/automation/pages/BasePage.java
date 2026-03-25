package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    protected WebElement findElement(By locator) {
        return driver.findElement(locator);
    }
    protected void waitForVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForListNotEmpty(List<WebElement> elements) {
        wait.until(d -> elements.size() > 0);
    }

//    protected void click(WebElement element) {
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);;
//        waitForClickable(element);
//        element.click();
//    }

    protected void click(WebElement element) {
        for (int i = 0; i < 2; i++) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                waitForClickable(element);
                element.click();
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                if (i == 1) throw e;
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                break;
            }
        }
    }

    protected void sendKeys(WebElement element, String text) {
        waitForVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        wait.until(d -> {
            String text = element.getText();
            return text != null && !text.trim().isEmpty();
        });
        return element.getText();
    }

    protected String getValue(WebElement element) {
        waitForVisible(element);
        return element.getAttribute("value");
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getAttribute(WebElement element, String name) {
        return element.getAttribute(name);
    }
}
