package com.automation.pages.recruitment;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RecruitmentPage extends BasePage {
    private static final String CANDIDATES_PATH = "/recruitment/viewCandidates";
    private static final String VACANCIES_PATH = "/recruitment/viewJobVacancy";
    private static final By CANDIDATES_LINK = By.xpath("//a[text()='Candidates']");
    private static final By VACANCIES_LINK = By.xpath("//a[text()='Vacancies']");
    private static final By PAGE_TITLE = By.xpath("//h6[normalize-space()='Recruitment']");

    public RecruitmentPage(WebDriver driver) {
        super(driver);
    }

    public RecruitmentPage waitForPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
        return this;
    }

    public CandidatesPage gotoCandidatesPage() {
        click(CANDIDATES_LINK);
        wait.until(ExpectedConditions.urlContains(CANDIDATES_PATH));
        return new CandidatesPage(driver).waitForPage();
    }

    public VacanciesPage gotoVacanciesPage() {
        click(VACANCIES_LINK);
        wait.until(ExpectedConditions.urlContains(VACANCIES_PATH));
        return new VacanciesPage(driver).waitForPage();
    }
}
