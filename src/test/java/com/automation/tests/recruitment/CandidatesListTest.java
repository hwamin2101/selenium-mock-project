package com.automation.tests.recruitment;

import com.automation.pages.recruitment.CandidatesPage;
import com.automation.tests.BaseTest;
import com.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CandidatesListTest extends BaseTest {
    @Test
    public void shouldDisplayCandidatesListLayout() {
        CandidatesPage candidatesPage = openCandidatesPage();

        Assert.assertTrue(candidatesPage.hasExpectedListLayout(),
                "Candidates list screen layout is incomplete.");
    }

    @Test
    public void shouldSearchWithoutFiltersAndReturnCandidates() {
        CandidatesPage candidatesPage = openCandidatesPage()
                .searchAllCandidates();

        Assert.assertTrue(candidatesPage.hasExpectedListLayout(),
                "Candidates list screen should remain usable after searching without filters.");
    }

    @Test(enabled = false)
    public void shouldResetCandidateSearchFilters() {
        CandidatesPage candidatesPage = openCandidatesPage()
                .searchCandidateByName(ConfigReader.getProperty("recruitment.search.reset_candidate_name"))
                .resetSearchFilters();

        Assert.assertEquals(candidatesPage.getCandidateNameFilterValue(), "",
                "Candidate name filter should be cleared after reset.");
        Assert.assertTrue(candidatesPage.hasExpectedListLayout(),
                "Candidates list screen should remain usable after reset.");
    }

    private CandidatesPage openCandidatesPage() {
        return navBar.gotoRecruitmentPage().waitForPage().gotoCandidatesPage();
    }
}
