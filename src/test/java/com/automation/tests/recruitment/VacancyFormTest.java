package com.automation.tests.recruitment;

import com.automation.pages.recruitment.AddVacancyPage;
import com.automation.pages.recruitment.VacanciesPage;
import com.automation.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class VacancyFormTest extends BaseTest {
    @Test
    public void shouldNavigateToAddVacancyForm() {
        AddVacancyPage addVacancyPage = openVacanciesPage().openAddVacancyForm();

        Assert.assertTrue(addVacancyPage.hasExpectedFormLayout(),
                "Add Vacancy form was not displayed correctly.");
    }

    @Test
    public void shouldDisplayAddVacancyFormLayout() {
        AddVacancyPage addVacancyPage = openVacanciesPage().openAddVacancyForm();

        Assert.assertTrue(addVacancyPage.hasExpectedFormLayout(),
                "Add Vacancy form layout is incomplete.");
    }

    private VacanciesPage openVacanciesPage() {
        return navBar.gotoRecruitmentPage().waitForPage().gotoVacanciesPage();
    }
}
