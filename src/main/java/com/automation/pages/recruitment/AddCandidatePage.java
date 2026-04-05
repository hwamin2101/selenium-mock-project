package com.automation.pages.recruitment;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddCandidatePage extends BasePage {
    private static final By PAGE_TITLE = By.xpath("//h6[normalize-space()='Add Candidate']");
    private static final By SAVE_BUTTON = By.xpath("//button[normalize-space()='Save']");
    private static final By CANCEL_BUTTON = By.xpath("//button[normalize-space()='Cancel']");
    private static final By REQUIRED_MESSAGES = By.xpath("//span[normalize-space()='Required']");
    private static final By LOADER = By.cssSelector(".oxd-form-loader");

    private static final By FIRST_NAME_INPUT = By.name("firstName");
    private static final By MIDDLE_NAME_INPUT = By.name("middleName");
    private static final By LAST_NAME_INPUT = By.name("lastName");
    private static final By VACANCY_DROPDOWN = By.xpath(
            "//label[normalize-space()='Vacancy']/ancestor::div[contains(@class,'oxd-input-group')]"
                    + "//div[contains(@class,'oxd-select-text-input')]");
    private static final By EMAIL_INPUT = By.xpath(
            "//label[normalize-space()='Email']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By CONTACT_NUMBER_INPUT = By.xpath(
            "//label[normalize-space()='Contact Number']"
                    + "/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By KEYWORDS_INPUT = By.xpath(
            "//label[normalize-space()='Keywords']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By APPLICATION_DATE_INPUT = By.xpath(
            "//label[normalize-space()='Date of Application']"
                    + "/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By NOTES_TEXTAREA = By.xpath(
            "//label[normalize-space()='Notes']/ancestor::div[contains(@class,'oxd-input-group')]//textarea");
    private static final By CONSENT_CHECKBOX = By.xpath(
            "//label[normalize-space()='Consent to keep data']"
                    + "/ancestor::div[contains(@class,'oxd-input-group')]"
                    + "//span[contains(@class,'oxd-checkbox-input')]");
    private static final List<By> FORM_ELEMENTS = Arrays.asList(
            By.xpath("//label[normalize-space()='Full Name']"),
            By.xpath("//label[normalize-space()='Vacancy']"),
            By.xpath("//label[normalize-space()='Email']"),
            By.xpath("//label[normalize-space()='Contact Number']"),
            By.xpath("//label[normalize-space()='Resume']"),
            By.xpath("//label[normalize-space()='Keywords']"),
            By.xpath("//label[normalize-space()='Date of Application']"),
            By.xpath("//label[normalize-space()='Notes']"),
            By.xpath("//label[normalize-space()='Consent to keep data']"),
            SAVE_BUTTON,
            CANCEL_BUTTON);

    public AddCandidatePage(WebDriver driver) {
        super(driver);
    }

    public AddCandidatePage waitForPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
        wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_INPUT));
        wait.until(ExpectedConditions.visibilityOfElementLocated(SAVE_BUTTON));
        waitForLoaderToDisappear();
        return this;
    }

    public boolean hasExpectedFormLayout() {
        return FORM_ELEMENTS.stream().allMatch(this::isDisplayed);
    }

    public AddCandidatePage saveWithoutEnteringData() {
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

    public CandidateProfilePage addCandidate(String firstName, String middleName, String lastName,
                                             String vacancy, String email, String contactNumber,
                                             String keywords, String dateOfApplication, String notes,
                                             boolean consentToKeepData) {
        sendKeys(FIRST_NAME_INPUT, firstName);

        if (!middleName.isBlank()) {
            sendKeys(MIDDLE_NAME_INPUT, middleName);
        }

        sendKeys(LAST_NAME_INPUT, lastName);

        if (!vacancy.isBlank()) {
            selectDropdownOption(VACANCY_DROPDOWN, vacancy);
        }

        sendKeys(EMAIL_INPUT, email);

        if (!contactNumber.isBlank()) {
            sendKeys(CONTACT_NUMBER_INPUT, contactNumber);
        }

        if (!keywords.isBlank()) {
            sendKeys(KEYWORDS_INPUT, keywords);
        }

        if (!dateOfApplication.isBlank()) {
            sendKeys(APPLICATION_DATE_INPUT, dateOfApplication);
        }

        if (!notes.isBlank()) {
            sendKeys(NOTES_TEXTAREA, notes);
        }

        if (consentToKeepData) {
            click(CONSENT_CHECKBOX);
        }

        click(SAVE_BUTTON);
        wait.until(ExpectedConditions.urlContains("/recruitment/addCandidate/"));
        waitForLoaderToDisappear();
        return new CandidateProfilePage(driver).waitForPage();
    }

    private void selectDropdownOption(By dropdownLocator, String optionText) {
        click(dropdownLocator);
        By optionLocator = By.xpath("//div[@role='listbox']//span[normalize-space()='" + optionText + "']");
        click(optionLocator);
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER));
    }
}
