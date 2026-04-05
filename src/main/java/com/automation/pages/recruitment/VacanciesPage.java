package com.automation.pages.recruitment;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;

public class VacanciesPage extends BasePage {
    private static final String PAGE_PATH = "/recruitment/viewJobVacancy";
    private static final By PAGE_TITLE = By.xpath("//h6[normalize-space()='Recruitment']");
    private static final By SEARCH_BUTTON = By.xpath("//button[normalize-space()='Search']");
    private static final By RESET_BUTTON = By.xpath("//button[normalize-space()='Reset']");
    private static final By ADD_BUTTON = By.xpath("//button[normalize-space()='Add']");
    private static final By VACANCY_FILTER_DROPDOWN = By.xpath(
            "//label[normalize-space()='Vacancy']/ancestor::div[contains(@class,'oxd-input-group')]"
                    + "//div[contains(@class,'oxd-select-text-input')]");
    private static final By VACANCY_FILTER_TEXT = By.xpath(
            "//label[normalize-space()='Vacancy']/ancestor::div[contains(@class,'oxd-input-group')]"
                    + "//div[contains(@class,'oxd-select-text-input')]");
    private static final By VACANCY_ROWS = By.cssSelector(".oxd-table-card");
    private static final By LOADER = By.cssSelector(".oxd-form-loader");
    private static final List<By> LIST_SCREEN_ELEMENTS = Arrays.asList(
            By.xpath("//label[normalize-space()='Job Title']"),
            By.xpath("//label[normalize-space()='Vacancy']"),
            By.xpath("//label[normalize-space()='Hiring Manager']"),
            By.xpath("//label[normalize-space()='Status']"),
            SEARCH_BUTTON,
            RESET_BUTTON,
            ADD_BUTTON);

    public VacanciesPage(WebDriver driver) {
        super(driver);
    }

    public VacanciesPage waitForPage() {
        wait.until(ExpectedConditions.urlContains(PAGE_PATH));
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
        wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_BUTTON));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[normalize-space()='Job Title']")));
        waitForLoaderToDisappear();
        return this;
    }

    public VacanciesPage searchByVacancy(String vacancyName) {
        selectDropdownOption(VACANCY_FILTER_DROPDOWN, vacancyName);
        click(SEARCH_BUTTON);
        waitForLoaderToDisappear();
        return this;
    }

    public VacanciesPage searchByFilters(String jobTitle, String vacancy, String hiringManager,
                                         String status) {
        if (!jobTitle.isBlank()) {
            selectDropdownOption("Job Title", jobTitle);
        }
        if (!vacancy.isBlank()) {
            selectDropdownOption("Vacancy", vacancy);
        }
        if (!hiringManager.isBlank()) {
            selectDropdownOption("Hiring Manager", hiringManager);
        }
        if (!status.isBlank()) {
            selectDropdownOption("Status", status);
        }
        click(SEARCH_BUTTON);
        waitForLoaderToDisappear();
        return this;
    }

    public VacanciesPage searchAllVacancies() {
        click(SEARCH_BUTTON);
        waitForLoaderToDisappear();
        return this;
    }

    public VacanciesPage resetSearchFilters() {
        click(RESET_BUTTON);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(VACANCY_FILTER_TEXT, "-- Select --"));
        return this;
    }

    public AddVacancyPage openAddVacancyForm() {
        click(ADD_BUTTON);
        return new AddVacancyPage(driver).waitForPage();
    }

    public boolean hasExpectedListLayout() {
        return LIST_SCREEN_ELEMENTS.stream().allMatch(this::isDisplayed);
    }

    public boolean isVacancyPresent(String vacancyName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(VACANCY_ROWS));
        for (WebElement row : findElements(VACANCY_ROWS)) {
            if (row.getText().contains(vacancyName)) {
                return true;
            }
        }
        return false;
    }

    public String getSelectedVacancyFilterText() {
        return getText(VACANCY_FILTER_TEXT);
    }

    public int getVacancyCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(VACANCY_ROWS));
        return findElements(VACANCY_ROWS).size();
    }

    public boolean hasNoRecords() {
        return driver.getPageSource().contains("No Records Found");
    }

    public boolean areRowsMatchingAll(String... expectedTexts) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(VACANCY_ROWS));
        return findElements(VACANCY_ROWS)
                .stream()
                .map(WebElement::getText)
                .allMatch(text -> Arrays.stream(expectedTexts)
                        .filter(expected -> expected != null && !expected.isBlank())
                        .allMatch(text::contains));
    }

    private void selectDropdownOption(By dropdownLocator, String optionText) {
        click(dropdownLocator);
        By optionLocator = By.xpath("//div[@role='listbox']//span[normalize-space()='" + optionText + "']");
        click(optionLocator);
    }

    private void selectDropdownOption(String label, String optionText) {
        By dropdownLocator = By.xpath("//label[normalize-space()='" + label + "']"
                + "/ancestor::div[contains(@class,'oxd-input-group')]"
                + "//div[contains(@class,'oxd-select-text-input')]");
        selectDropdownOption(dropdownLocator, optionText);
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER));
    }
}
