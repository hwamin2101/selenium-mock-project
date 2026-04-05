package com.automation.tests.recruitment;

import com.automation.pages.recruitment.AddCandidatePage;
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
import java.util.List;

public class CandidateFormTest extends BaseTest {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Test
    public void shouldNavigateToAddCandidateForm() {
        AddCandidatePage addCandidatePage = openCandidatesPage().openAddCandidateForm();

        Assert.assertTrue(addCandidatePage.hasExpectedFormLayout(),
                "Add Candidate form was not displayed correctly.");
    }

    @Test(dataProvider = "candidateValidationData",
            dataProviderClass = DataProviderUtilRecruitment.class)
    public void shouldValidateRequiredFieldsWhenAddCandidate(String expectedMessage,
                                                             int expectedErrorCount) {
        AddCandidatePage addCandidatePage = openCandidatesPage()
                .openAddCandidateForm()
                .saveWithoutEnteringData();

        List<String> requiredMessages = addCandidatePage.getRequiredMessages();

        Assert.assertEquals(requiredMessages.size(), expectedErrorCount,
                "Unexpected number of required field messages.");
        Assert.assertTrue(requiredMessages.stream().allMatch(expectedMessage::equals),
                "Required field messages are not consistent.");
    }

    @Test(dataProvider = "candidateData",
            dataProviderClass = DataProviderUtilRecruitment.class)
    public void shouldAddCandidateSuccessfully(String firstName, String middleName, String lastName,
                                               String vacancy, String email, String contactNumber,
                                               String keywords, String dateOfApplication, String notes,
                                               boolean consentToKeepData, String expectedStatus) {
        CandidateInput candidate = buildCandidateInput(firstName, middleName, lastName, vacancy, email,
                contactNumber, keywords, dateOfApplication, notes, consentToKeepData, expectedStatus);

        CandidateProfilePage candidateProfilePage = openCandidatesPage()
                .openAddCandidateForm()
                .addCandidate(candidate.firstName(), candidate.middleName(), candidate.lastName(),
                        candidate.vacancy(), candidate.email(), candidate.contactNumber(),
                        candidate.keywords(), candidate.dateOfApplication(), candidate.notes(),
                        candidate.consentToKeepData());

        Assert.assertEquals(candidateProfilePage.getCandidateStatus(), candidate.expectedStatus(),
                "Candidate status on profile is not correct after saving.");

        Assert.assertTrue(candidateProfilePage.goToCandidatesList().hasExpectedListLayout(),
                "Candidates list did not load after saving candidate.");
    }

    private CandidatesPage openCandidatesPage() {
        String candidatesUrl = ConfigReader.getProperty("url")
                .replace("/auth/login", "/recruitment/viewCandidates");
        driver.get(candidatesUrl);
        return new CandidatesPage(driver).waitForPage();
    }

    private CandidateInput buildCandidateInput(String firstName, String middleName, String lastName,
                                               String vacancy, String email, String contactNumber,
                                               String keywords, String dateOfApplication, String notes,
                                               boolean consentToKeepData, String expectedStatus) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String resolvedFirstName = resolveDynamicValue(firstName, timestamp);
        String resolvedMiddleName = resolveDynamicValue(middleName, timestamp);
        String resolvedLastName = resolveDynamicValue(lastName, timestamp);

        return new CandidateInput(resolvedFirstName, resolvedMiddleName, resolvedLastName, vacancy,
                resolveDynamicValue(email, timestamp), resolveDynamicValue(contactNumber, timestamp),
                resolveDynamicValue(keywords, timestamp), resolveDynamicValue(dateOfApplication, timestamp),
                resolveDynamicValue(notes, timestamp), consentToKeepData, expectedStatus,
                String.join(" ", resolvedFirstName, resolvedMiddleName, resolvedLastName)
                        .trim().replaceAll("\\s+", " "));
    }

    private String resolveDynamicValue(String value, String timestamp) {
        String resolvedValue = value == null ? "" : value.replace("{timestamp}", timestamp);

        if (resolvedValue.contains("{today}")) {
            String applicationDate = ConfigReader.getProperty("recruitment.default.application.date");
            if (applicationDate == null || applicationDate.isBlank()) {
                applicationDate = LocalDate.now().toString();
            }
            resolvedValue = resolvedValue.replace("{today}", applicationDate);
        }

        return resolvedValue;
    }

    private record CandidateInput(String firstName, String middleName, String lastName, String vacancy,
                                  String email, String contactNumber, String keywords,
                                  String dateOfApplication, String notes, boolean consentToKeepData,
                                  String expectedStatus, String fullName) {
    }
}
