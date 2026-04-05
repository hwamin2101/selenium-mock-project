package com.automation.tests.recruitment;

import com.automation.pages.recruitment.VacanciesPage;
import com.automation.tests.BaseTest;
import com.automation.utils.ConfigReader;
import com.automation.utils.DataProviderUtilRecruitment;
import org.testng.Assert;
import org.testng.annotations.Test;

public class VacanciesListTest extends BaseTest {
    @Test
    public void shouldAccessVacanciesScreen() {
        VacanciesPage vacanciesPage = openVacanciesPage();

        Assert.assertTrue(vacanciesPage.hasExpectedListLayout(),
                "Vacancies screen did not load correctly.");
    }

    @Test
    public void shouldDisplayVacanciesListLayout() {
        VacanciesPage vacanciesPage = openVacanciesPage();

        Assert.assertTrue(vacanciesPage.hasExpectedListLayout(),
                "Vacancies list screen layout is incomplete.");
    }

    @Test
    public void shouldSearchWithoutFiltersAndReturnVacancies() {
        VacanciesPage vacanciesPage = openVacanciesPage()
                .searchAllVacancies();

        Assert.assertTrue(vacanciesPage.getVacancyCount() > 0,
                "Expected at least one vacancy in the list.");
    }

    @Test(dataProvider = "vacancyData",
            dataProviderClass = DataProviderUtilRecruitment.class)
    public void shouldSearchByVacancy(String vacancyName) {
        VacanciesPage vacanciesPage = openVacanciesPage()
                .searchByVacancy(vacancyName);

        Assert.assertTrue(vacanciesPage.isVacancyPresent(vacancyName),
                "The expected vacancy was not found after searching.");
    }

    @Test(dataProvider = "vacancyData",
            dataProviderClass = DataProviderUtilRecruitment.class)
    public void shouldResetVacancyFilters(String vacancyName) {
        VacanciesPage vacanciesPage = openVacanciesPage()
                .searchByVacancy(vacancyName);

        vacanciesPage.resetSearchFilters();

        Assert.assertEquals(vacanciesPage.getSelectedVacancyFilterText(),
                ConfigReader.getProperty("recruitment.default.dropdown"),
                "Vacancy filter should return to the default value after reset.");
        Assert.assertFalse(vacanciesPage.hasNoRecords(),
                "Full vacancy list should be shown after reset.");
    }

    private VacanciesPage openVacanciesPage() {
        return navBar.gotoRecruitmentPage().waitForPage().gotoVacanciesPage();
    }
}
