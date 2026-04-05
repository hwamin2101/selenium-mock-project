package com.automation.pages.recruitment;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddVacancyPage extends BasePage {
    private static final By PAGE_TITLE = By.xpath("//h6[normalize-space()='Add Vacancy']");
    private static final By SAVE_BUTTON = By.xpath("//button[normalize-space()='Save']");
    private static final By CANCEL_BUTTON = By.xpath("//button[normalize-space()='Cancel']");
    private static final By REQUIRED_MESSAGES = By.xpath("//span[normalize-space()='Required']");
    private static final By VACANCY_NAME_INPUT = By.xpath(
            "//label[normalize-space()='Vacancy Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By JOB_TITLE_DROPDOWN = By.xpath(
            "//label[normalize-space()='Job Title']/ancestor::div[contains(@class,'oxd-input-group')]"
                    + "//div[contains(@class,'oxd-select-text-input')]");
    private static final By DESCRIPTION_TEXTAREA = By.xpath(
            "//label[normalize-space()='Description']/ancestor::div[contains(@class,'oxd-input-group')]//textarea");
    private static final By HIRING_MANAGER_INPUT = By.xpath(
            "//label[normalize-space()='Hiring Manager']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By NUMBER_OF_POSITIONS_INPUT = By.xpath(
            "//label[normalize-space()='Number of Positions']"
                    + "/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By EDIT_PAGE_TITLE = By.xpath("//h6[normalize-space()='Edit Vacancy']");
    private static final By HIRING_MANAGER_ERROR = By.xpath(
            "//label[normalize-space()='Hiring Manager']/ancestor::div[contains(@class,'oxd-input-group')]"
                    + "//span[contains(@class,'oxd-input-field-error-message')]");
    private static final By LOADER = By.cssSelector(".oxd-form-loader");
    private static final By AUTOCOMPLETE_OPTIONS = By.cssSelector(".oxd-autocomplete-option");
    private static final List<By> FORM_ELEMENTS = Arrays.asList(
            By.xpath("//label[normalize-space()='Vacancy Name']"),
            By.xpath("//label[normalize-space()='Job Title']"),
            By.xpath("//label[normalize-space()='Description']"),
            By.xpath("//label[normalize-space()='Hiring Manager']"),
            By.xpath("//label[normalize-space()='Number of Positions']"),
            SAVE_BUTTON,
            CANCEL_BUTTON);

    public AddVacancyPage(WebDriver driver) {
        super(driver);
    }

    public AddVacancyPage waitForPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
        wait.until(ExpectedConditions.visibilityOfElementLocated(SAVE_BUTTON));
        return this;
    }

    public boolean hasExpectedFormLayout() {
        return FORM_ELEMENTS.stream().allMatch(this::isDisplayed);
    }

    public AddVacancyPage saveWithoutEnteringData() {
        click(SAVE_BUTTON);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(REQUIRED_MESSAGES, 2));
        return this;
    }

    public List<String> getRequiredMessages() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(REQUIRED_MESSAGES));
        return findElements(REQUIRED_MESSAGES)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public VacancyDetailsPage addVacancy(String vacancyName, String jobTitle, String description,
                                         String hiringManagerHint, String numberOfPositions,
                                         boolean active, boolean publishToRss) {
        sendKeys(VACANCY_NAME_INPUT, vacancyName);
        selectDropdownOption(JOB_TITLE_DROPDOWN, jobTitle);

        if (!description.isBlank()) {
            sendKeys(DESCRIPTION_TEXTAREA, description);
        }

        String selectedHiringManager = selectHiringManager(hiringManagerHint);

        if (!numberOfPositions.isBlank()) {
            sendKeys(NUMBER_OF_POSITIONS_INPUT, numberOfPositions);
        }

        click(SAVE_BUTTON);
        wait.until(d -> {
            if (d.getCurrentUrl().contains("/recruitment/addJobVacancy/")) {
                return true;
            }
            if (!findElements(EDIT_PAGE_TITLE).isEmpty()) {
                return true;
            }
            return !findElements(HIRING_MANAGER_ERROR).isEmpty();
        });

        if (!findElements(HIRING_MANAGER_ERROR).isEmpty()) {
            throw new IllegalStateException("Demo site kept Hiring Manager invalid after " +
                    "selecting autocomplete option: "
                    + getText(HIRING_MANAGER_ERROR) + " (selected: " + selectedHiringManager + ")");
        }

        waitForLoaderToDisappear();
        return new VacancyDetailsPage(driver, selectedHiringManager).waitForPage();
    }

    private void selectDropdownOption(By dropdownLocator, String optionText) {
        click(dropdownLocator);
        By optionLocator = By.xpath("//div[@role='listbox']//span[normalize-space()='" + optionText + "']");
        click(optionLocator);
    }

    private String selectHiringManager(String hint) {
        WebElement input = findElement(HIRING_MANAGER_INPUT);
        input.clear();
        input.sendKeys(hint.substring(0, Math.min(2, hint.length())));

        wait.until(d -> findElements(AUTOCOMPLETE_OPTIONS)
                .stream()
                .map(option -> option.getText().trim())
                .anyMatch(text -> !text.isBlank() && !"Searching....".equalsIgnoreCase(text)));

        WebElement matchedOption = findElements(AUTOCOMPLETE_OPTIONS)
                .stream()
                .filter(option -> !"Searching....".equalsIgnoreCase(option.getText().trim()))
                .filter(option -> option.getText().contains(hint))
                .findFirst()
                .orElse(findElements(AUTOCOMPLETE_OPTIONS)
                        .stream()
                        .filter(option -> !"Searching....".equalsIgnoreCase(option.getText().trim()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "Demo site autocomplete did not return any employee option for hint: " + hint)));

        String selectedText = matchedOption.getText().trim();
        matchedOption.click();
        wait.until(ExpectedConditions.attributeToBe(HIRING_MANAGER_INPUT, "value", selectedText));
        return selectedText;
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER));
    }
}
