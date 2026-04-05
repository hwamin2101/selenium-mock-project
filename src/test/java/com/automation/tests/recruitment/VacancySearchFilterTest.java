package com.automation.tests.recruitment;

import com.automation.pages.recruitment.VacanciesPage;
import com.automation.pages.recruitment.VacancyDetailsPage;
import com.automation.tests.BaseTest;
import com.automation.utils.ConfigReader;
import com.automation.utils.DataProviderUtilRecruitment;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VacancySearchFilterTest extends BaseTest {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Test(enabled = false)
    public void shouldSearchByJobTitle() {
        VacancySeed vacancy = seedVacancy();

        VacanciesPage vacanciesPage = openVacanciesPage()
                .searchByFilters(vacancy.jobTitle(), "", "", "");

        assertVacancyPresent(vacanciesPage, vacancy, "Job Title filter");
    }

    @Test(enabled = false)
    public void shouldSearchByHiringManager() {
        VacancySeed vacancy = seedVacancy();

        VacanciesPage vacanciesPage = openVacanciesPage()
                .searchByFilters("", "", vacancy.hiringManager(), "");

        assertVacancyPresent(vacanciesPage, vacancy, "Hiring Manager filter");
    }

    @Test(enabled = false, dataProvider = "vacancyStatusData",
            dataProviderClass = DataProviderUtilRecruitment.class)
    public void shouldSearchByStatus(String status) {
        VacancySeed vacancy = seedVacancy();

        VacanciesPage vacanciesPage = openVacanciesPage()
                .searchByFilters("", "", "", status);

        assertVacancyPresent(vacanciesPage, vacancy, "Status filter");
    }

    @Test(enabled = false)
    public void shouldApplyCombinedVacancyFiltersWithAndLogic() {
        VacancySeed vacancy = seedVacancy();

        VacanciesPage vacanciesPage = openVacanciesPage()
                .searchByFilters(vacancy.jobTitle(), vacancy.name(), vacancy.hiringManager(), vacancy.status());

        assertVacancyPresent(vacanciesPage, vacancy, "Combined vacancy filters");
    }

    @Test(enabled = false)
    public void shouldShowNoRecordsFoundWhenVacancyAndStatusDoNotMatch() {
        VacanciesPage vacanciesPage = openVacanciesPage()
                .searchByFilters("", ConfigReader.getProperty("recruitment.default.unmatched.vacancy"), "",
                        ConfigReader.getProperty("recruitment.default.unmatched.vacancy.status"));

        Assert.assertTrue(vacanciesPage.hasNoRecords(),
                "No Records Found should be displayed for unmatched vacancy filters.");
    }

    private VacancySeed seedVacancy() {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String vacancyName = ConfigReader.getProperty("recruitment.seed.vacancy.name_prefix") + timestamp;
        String jobTitle = ConfigReader.getProperty("recruitment.seed.vacancy.job_title");
        String hiringManagerHint = ConfigReader.getProperty("recruitment.seed.vacancy.hiring_manager_hint");

        VacancyDetailsPage detailsPage = openVacanciesPage()
                .openAddVacancyForm()
                .addVacancy(vacancyName, jobTitle, "", hiringManagerHint, "", true, true);

        VacanciesPage baselinePage = detailsPage.goToVacanciesList()
                .searchByVacancy(vacancyName);

        Assert.assertTrue(baselinePage.isVacancyPresent(vacancyName),
                "Seed vacancy did not appear in the baseline vacancy results after saving.");

        return new VacancySeed(vacancyName, jobTitle,
                detailsPage.getSelectedHiringManager(),
                ConfigReader.getProperty("recruitment.seed.vacancy.status"));
    }

    private VacanciesPage openVacanciesPage() {
        return navBar.gotoRecruitmentPage().waitForPage().gotoVacanciesPage();
    }

    private void assertVacancyPresent(VacanciesPage vacanciesPage, VacancySeed vacancy, String filterName) {
        Assert.assertTrue(vacanciesPage.isVacancyPresent(vacancy.name()),
                filterName + " did not return the seeded vacancy. "
                        + "The vacancy is available in baseline results, so this points to unstable filter or save "
                        + "behavior on the OrangeHRM demo site.");
    }

    private record VacancySeed(String name, String jobTitle, String hiringManager, String status) {
    }
}
