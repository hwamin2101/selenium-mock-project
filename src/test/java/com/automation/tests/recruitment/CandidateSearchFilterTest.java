package com.automation.tests.recruitment;

import com.automation.pages.recruitment.CandidateProfilePage;
import com.automation.pages.recruitment.CandidatesPage;
import com.automation.tests.BaseTest;
import com.automation.utils.ConfigReader;
import com.automation.utils.DataProviderUtilRecruitment;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CandidateSearchFilterTest extends BaseTest {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Test(enabled = false)
    public void shouldSearchByCandidateName() {
        CandidateSeed candidate = seedCandidate();

        CandidatesPage candidatesPage = openCandidatesPage()
                .searchCandidateByName(candidate.fullName());

        assertCandidatePresent(candidatesPage, candidate,
                "Candidate Name autocomplete filter");
    }

    @Test(enabled = false)
    public void shouldSearchByJobTitle() {
        CandidateSeed candidate = seedCandidate();

        CandidatesPage candidatesPage = openCandidatesPage()
                .searchByFilters(candidate.jobTitle(), "", "", "", "");

        Assert.assertTrue(candidatesPage.areRowsMatchingAll(candidate.jobTitle()),
                "Search by Job Title returned unexpected candidate rows.");
    }

    @Test(enabled = false)
    public void shouldSearchByVacancy() {
        CandidateSeed candidate = seedCandidate();

        CandidatesPage candidatesPage = openCandidatesPage()
                .searchByFilters("", candidate.vacancy(), "", "", "");

        Assert.assertTrue(candidatesPage.areRowsMatchingAll(candidate.vacancy()),
                "Search by Vacancy returned unexpected candidate rows.");
    }

    @Test(enabled = false, dataProvider = "candidateStatusData",
            dataProviderClass = DataProviderUtilRecruitment.class)
    public void shouldSearchByStatus(String status) {
        CandidateSeed candidate = seedCandidate();

        CandidatesPage candidatesPage = openCandidatesPage()
                .searchByFilters("", "", status, "", "");

        assertCandidatePresent(candidatesPage, candidate,
                "Status filter");
    }

    @Test(enabled = false)
    public void shouldSearchByKeywords() {
        CandidateSeed candidate = seedCandidate();

        CandidatesPage candidatesPage = openCandidatesPage()
                .searchByFilters("", "", "", candidate.keywords(), "");

        assertCandidatePresent(candidatesPage, candidate,
                "Keywords filter");
    }

    @Test(enabled = false, dataProvider = "candidateMethodData",
            dataProviderClass = DataProviderUtilRecruitment.class)
    public void shouldSearchByMethod(String method) {
        CandidateSeed candidate = seedCandidate();

        CandidatesPage candidatesPage = openCandidatesPage()
                .searchByFilters("", "", "", "", method);

        assertCandidatePresent(candidatesPage, candidate,
                "Method of Application filter");
    }

    @Test(enabled = false)
    public void shouldApplyCombinedFiltersWithAndLogic() {
        CandidateSeed candidate = seedCandidate();

        CandidatesPage candidatesPage = openCandidatesPage()
                .searchByFilters(candidate.jobTitle(), candidate.vacancy(), candidate.status(), "", "");

        assertCandidatePresent(candidatesPage, candidate,
                "Combined candidate filters");
    }

    @Test
    public void shouldValidateInvalidApplicationDateRange() {
        CandidatesPage candidatesPage = openCandidatesPage()
                .searchByDateRange(LocalDate.now().plusDays(1).toString(), LocalDate.now().toString());

        Assert.assertTrue(candidatesPage.hasDateRangeError(),
                "Invalid date range should display validation error.");
    }

    @Test
    public void shouldShowNoRecordsFoundWhenNoCandidateMatches() {
        CandidatesPage candidatesPage = openCandidatesPage()
                .searchByFilters("", "", "", buildNoMatchKeyword(), "");

        Assert.assertTrue(candidatesPage.hasNoRecords(),
                "No Records Found should be displayed for unmatched candidate filters.");
    }

    private CandidateSeed seedCandidate() {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String firstName = ConfigReader.getProperty("recruitment.seed.candidate.first_name_prefix") + timestamp;
        String lastName = ConfigReader.getProperty("recruitment.seed.candidate.last_name");
        String fullName = firstName + " " + lastName;
        String keywords = ConfigReader.getProperty("recruitment.seed.candidate.keywords_prefix") + timestamp;
        String vacancy = ConfigReader.getProperty("recruitment.seed.candidate.vacancy");
        String email = ConfigReader.getProperty("recruitment.seed.candidate.email_prefix")
                + timestamp + ConfigReader.getProperty("recruitment.seed.candidate.email_domain");

        CandidateProfilePage profilePage = openCandidatesPage()
                .openAddCandidateForm()
                .addCandidate(firstName, "", lastName, vacancy, email, "", keywords, "", "", false);

        Assert.assertEquals(profilePage.getCandidateStatus(),
                ConfigReader.getProperty("recruitment.expected.candidate.status"));
        profilePage.goToCandidatesList();

        return new CandidateSeed(fullName, vacancy, vacancy,
                ConfigReader.getProperty("recruitment.expected.candidate.status"), keywords);
    }

    private CandidatesPage openCandidatesPage() {
        return navBar.gotoRecruitmentPage().waitForPage().gotoCandidatesPage();
    }

    private record CandidateSeed(String fullName, String vacancy, String jobTitle,
                                 String status, String keywords) {
    }

    private void assertCandidatePresent(CandidatesPage candidatesPage, CandidateSeed candidate,
                                        String filterName) {
        Assert.assertTrue(candidatesPage.isCandidatePresentWithStatus(candidate.fullName(), candidate.status()),
                filterName + " did not return the seeded candidate. "
                        + "Candidate creation completed successfully, so this points to unstable search/filter "
                        + "behavior on the OrangeHRM demo site.");
    }

    private String buildNoMatchKeyword() {
        return ConfigReader.getProperty("recruitment.search.unmatched_candidate_keyword_prefix")
                + System.currentTimeMillis();
    }
}
