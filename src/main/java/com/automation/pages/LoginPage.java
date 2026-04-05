package com.automation.pages;

import com.automation.component.NavBar;
import com.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private static final By USER_NAME = By.name("username");
    private static final By PASS_WORD = By.name("password");
    private static final By LOGIN_BTN = By.xpath("//button[@type='submit']");

    public LoginPage (WebDriver driver) {
        super(driver);
    }

    public LoginPage gotoLoginPage() {
        driver.get(ConfigReader.getProperty("url"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(USER_NAME));
        return this;
    }

    public NavBar login() {
        String username = ConfigReader.getProperty("username");
        String password = ConfigReader.getProperty("password");

        sendKeys(USER_NAME, username);
        sendKeys(PASS_WORD, password);
        click(LOGIN_BTN);
        return new NavBar(driver);
    }
}
