package com.automation.pages.recruitment;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;

public class CandidatesPage extends BasePage {
    private static final String PAGE_PATH = "/recruitment/viewCandidates";
    private static final By PAGE_TITLE = By.xpath("//h6[normalize-space()='Recruitment']");
    private static final By ADD_BUTTON = By.xpath("//button[normalize-space()='Add']");
    private static final By SEARCH_BUTTON = By.xpath("//button[normalize-space()='Search']");
    private static final By RESET_BUTTON = By.xpath("//button[normalize-space()='Reset']");
    private static final By CANDIDATE_ROWS = By.cssSelector(".oxd-table-card");
    private static final By LOADER = By.cssSelector(".oxd-form-loader");
    private static final By CANDIDATE_NAME_FILTER = By.xpath(
            "(//label[normalize-space()='Candidate Name']"
                    + "/ancestor::div[contains(@class,'oxd-input-group')]//input)[1]");
    private static final By KEYWORDS_FILTER = By.xpath(
            "//label[normalize-space()='Keywords']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By DATE_FROM_FILTER = By.xpath(
            "//label[normalize-space()='Date of Application']"
                    + "/ancestor::div[contains(@class,'oxd-grid-4')]//input[@placeholder='From']");
    private static final By DATE_TO_FILTER = By.xpath(
            "//label[normalize-space()='Date of Application']"
                    + "/ancestor::div[contains(@class,'oxd-grid-4')]//input[@placeholder='To']");
    private static final By DATE_RANGE_ERROR = By.xpath("//span[contains(.,'To date should be after from date')]");
    private static final By AUTOCOMPLETE_OPTIONS = By.cssSelector(".oxd-autocomplete-option");
    private static final List<By> LIST_SCREEN_ELEMENTS = Arrays.asList(
            By.xpath("//label[normalize-space()='Candidate Name']"),
            By.xpath("//label[normalize-space()='Vacancy']"),
            By.xpath("//label[normalize-space()='Status']"),
            By.xpath("//label[normalize-space()='Keywords']"),
            SEARCH_BUTTON,
            RESET_BUTTON,
            ADD_BUTTON);

    public CandidatesPage(WebDriver driver) {
        super(driver);
    }

    public CandidatesPage waitForPage() {
        wait.until(ExpectedConditions.urlContains(PAGE_PATH));
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
        wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_BUTTON));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[normalize-space()='Candidate Name']")));
        waitForLoaderToDisappear();
        return this;
    }

    public AddCandidatePage openAddCandidateForm() {
        click(ADD_BUTTON);
        return new AddCandidatePage(driver).waitForPage();
    }

    public CandidatesPage searchCandidateByName(String candidateName) {
        selectAutocompleteOption(CANDIDATE_NAME_FILTER, candidateName);
        click(SEARCH_BUTTON);
        waitForLoaderToDisappear();
        return this;
    }

    public CandidatesPage resetSearchFilters() {
        click(RESET_BUTTON);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.attributeToBe(CANDIDATE_NAME_FILTER, "value", ""));
        return this;
    }

    public CandidatesPage searchAllCandidates() {
        click(SEARCH_BUTTON);
        waitForLoaderToDisappear();
        return this;
    }

    public CandidatesPage searchByFilters(String jobTitle, String vacancy, String status,
                                          String keywords, String method) {
        if (!jobTitle.isBlank()) {
            selectDropdownOption("Job Title", jobTitle);
        }
        if (!vacancy.isBlank()) {
            selectDropdownOption("Vacancy", vacancy);
        }
        if (!status.isBlank()) {
            selectDropdownOption("Status", status);
        }
        if (!keywords.isBlank()) {
            sendKeys(KEYWORDS_FILTER, keywords);
        }
        if (!method.isBlank()) {
            selectDropdownOption("Method of Application", method);
        }
        click(SEARCH_BUTTON);
        waitForLoaderToDisappear();
        return this;
    }

    public CandidatesPage searchByDateRange(String fromDate, String toDate) {
        if (!fromDate.isBlank()) {
            sendKeys(DATE_FROM_FILTER, fromDate);
        }
        if (!toDate.isBlank()) {
            sendKeys(DATE_TO_FILTER, toDate);
        }
        click(SEARCH_BUTTON);
        return this;
    }

    public boolean hasExpectedListLayout() {
        return LIST_SCREEN_ELEMENTS.stream().allMatch(this::isDisplayed);
    }

    public boolean isCandidatePresentWithStatus(String candidateName, String expectedStatus) {
        waitForLoaderToDisappear();
        try {
            return findElements(CANDIDATE_ROWS)
                    .stream()
                    .map(WebElement::getText)
                    .anyMatch(text -> text.contains(candidateName) && text.contains(expectedStatus));
        } catch (StaleElementReferenceException e) {
            return driver.getPageSource().contains(candidateName)
                    && driver.getPageSource().contains(expectedStatus);
        }
    }

    public String getCandidateNameFilterValue() {
        return getValue(CANDIDATE_NAME_FILTER);
    }

    public int getCandidateCount() {
        waitForLoaderToDisappear();
        return findElements(CANDIDATE_ROWS).size();
    }

    public boolean hasNoRecords() {
        waitForLoaderToDisappear();
        return driver.getPageSource().contains("No Records Found") || findElements(CANDIDATE_ROWS).isEmpty();
    }

    public boolean hasDateRangeError() {
        return isDisplayed(DATE_RANGE_ERROR);
    }

    public boolean areRowsMatchingAll(String... expectedTexts) {
        waitForLoaderToDisappear();
        return findElements(CANDIDATE_ROWS)
                .stream()
                .map(WebElement::getText)
                .allMatch(text -> Arrays.stream(expectedTexts)
                        .filter(expected -> expected != null && !expected.isBlank())
                        .allMatch(text::contains));
    }

    private void selectDropdownOption(String label, String optionText) {
        By dropdownLocator = By.xpath("//label[normalize-space()='" + label + "']"
                + "/ancestor::div[contains(@class,'oxd-input-group')]"
                + "//div[contains(@class,'oxd-select-text-input')]");
        click(dropdownLocator);
        By optionLocator = By.xpath("//div[@role='listbox']//span[normalize-space()='" + optionText + "']");
        click(optionLocator);
    }

    private void selectAutocompleteOption(By inputLocator, String expectedText) {
        WebElement input = findElement(inputLocator);
        input.clear();
        input.sendKeys(expectedText.substring(0, Math.min(2, expectedText.length())));

        wait.until(ExpectedConditions.visibilityOfElementLocated(AUTOCOMPLETE_OPTIONS));

        WebElement matchedOption = findElements(AUTOCOMPLETE_OPTIONS)
                .stream()
                .filter(option -> option.getText().trim().equalsIgnoreCase(expectedText))
                .findFirst()
                .orElse(findElements(AUTOCOMPLETE_OPTIONS).get(0));

        String selectedText = matchedOption.getText().trim();
        matchedOption.click();
        wait.until(ExpectedConditions.attributeToBe(inputLocator, "value", selectedText));
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER));
    }
}
