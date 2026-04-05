package com.automation.pages.recruitment;

import com.automation.pages.BasePage;
import com.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class VacancyDetailsPage extends BasePage {
    private static final By EDIT_PAGE_TITLE = By.xpath("//h6[normalize-space()='Edit Vacancy']");
    private static final By VACANCY_NAME_INPUT = By.xpath(
            "//label[normalize-space()='Vacancy Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By JOB_TITLE_TEXT = By.xpath(
            "//label[normalize-space()='Job Title']/ancestor::div[contains(@class,'oxd-input-group')]"
                    + "//div[contains(@class,'oxd-select-text-input')]");
    private static final By HIRING_MANAGER_INPUT = By.xpath(
            "//label[normalize-space()='Hiring Manager']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By NUMBER_OF_POSITIONS_INPUT = By.xpath(
            "//label[normalize-space()='Number of Positions']"
                    + "/ancestor::div[contains(@class,'oxd-input-group')]//input");

    private final String selectedHiringManager;

    public VacancyDetailsPage(WebDriver driver, String selectedHiringManager) {
        super(driver);
        this.selectedHiringManager = selectedHiringManager;
    }

    public VacancyDetailsPage waitForPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(EDIT_PAGE_TITLE));
        wait.until(ExpectedConditions.visibilityOfElementLocated(VACANCY_NAME_INPUT));
        return this;
    }

    public String getVacancyName() {
        return getValue(VACANCY_NAME_INPUT);
    }

    public String getJobTitle() {
        return getText(JOB_TITLE_TEXT);
    }

    public String getHiringManager() {
        return getValue(HIRING_MANAGER_INPUT);
    }

    public String getNumberOfPositions() {
        return getValue(NUMBER_OF_POSITIONS_INPUT);
    }

    public String getSelectedHiringManager() {
        return selectedHiringManager;
    }

    public VacanciesPage goToVacanciesList() {
        String vacanciesUrl = ConfigReader.getProperty("url")
                .replace("/auth/login", "/recruitment/viewJobVacancy");
        driver.get(vacanciesUrl);
        return new VacanciesPage(driver).waitForPage();
    }
}
