package com.automation.pages.recruitment;

import com.automation.pages.BasePage;
import com.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CandidateProfilePage extends BasePage {
    private static final By STATUS_BADGE = By.cssSelector(".orangehrm-recruitment-status p");

    public CandidateProfilePage(WebDriver driver) {
        super(driver);
    }

    public CandidateProfilePage waitForPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(STATUS_BADGE));
        return this;
    }

    public String getCandidateStatus() {
        return getText(STATUS_BADGE).replace("Status:", "").trim();
    }

    public CandidatesPage goToCandidatesList() {
        String candidatesUrl = ConfigReader.getProperty("url")
                .replace("/auth/login", "/recruitment/viewCandidates");
        driver.get(candidatesUrl);
        return new CandidatesPage(driver).waitForPage();
    }
}
