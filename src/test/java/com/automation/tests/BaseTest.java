package com.automation.tests;

import com.automation.component.NavBar;
import com.automation.pages.LoginPage;
import com.automation.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.automation.utils.ConfigReader;

public class BaseTest {
    protected WebDriver driver;
    protected NavBar navBar;

    @BeforeClass(alwaysRun = true)
    @Parameters("isCI")
    public void setUp(@Optional("false") String isCIFromXml) {
        boolean isCI = Boolean.parseBoolean(System.getProperty("ci", isCIFromXml));

        ConfigReader.loadConfig(isCI);
        driver = DriverManager.initDriver();

        // Login
        navBar = new LoginPage(driver).gotoLoginPage().login();
    }

    public WebDriver getDriver() {
        return driver;
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
