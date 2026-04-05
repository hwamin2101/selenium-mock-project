package com.automation.tests.recruitment;

import com.automation.pages.recruitment.AddVacancyPage;
import com.automation.pages.recruitment.VacanciesPage;
import com.automation.pages.recruitment.VacancyDetailsPage;
import com.automation.tests.BaseTest;
import com.automation.utils.ConfigReader;
import com.automation.utils.DataProviderUtilRecruitment;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VacancyAddTest extends BaseTest {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Test
    public void shouldValidateRequiredFieldsWhenAddVacancy() {
        AddVacancyPage addVacancyPage = openVacanciesPage()
                .openAddVacancyForm()
                .saveWithoutEnteringData();

        List<String> requiredMessages = addVacancyPage.getRequiredMessages();
        String expectedRequiredMessage = getPropertyOrDefault("recruitment.required.message", "Required");

        Assert.assertEquals(requiredMessages.size(), getIntPropertyOrDefault(
                        "recruitment.expected.required.count.vacancy", 3),
                "Unexpected required message count on Add Vacancy form.");
        Assert.assertTrue(requiredMessages.stream()
                        .allMatch(expectedRequiredMessage::equals),
                "Required messages on Add Vacancy form are inconsistent.");
    }

    @Test(enabled = false, dataProvider = "vacancyCreateData",
            dataProviderClass = DataProviderUtilRecruitment.class)
    public void shouldAddVacancySuccessfully(String suffix, String jobTitle, String description,
                                             String hiringManagerHint, String numberOfPositions) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String vacancyName = suffix + "-" + timestamp;

        VacancyDetailsPage detailsPage = openVacanciesPage()
                .openAddVacancyForm()
                .addVacancy(vacancyName, jobTitle, description, hiringManagerHint,
                        numberOfPositions, true, true);

        Assert.assertEquals(detailsPage.getVacancyName(), vacancyName,
                "Saved vacancy name does not match input.");
    }

    private VacanciesPage openVacanciesPage() {
        return navBar.gotoRecruitmentPage().waitForPage().gotoVacanciesPage();
    }

    private int getIntPropertyOrDefault(String key, int defaultValue) {
        String value = ConfigReader.getProperty(key);
        return value == null || value.isBlank() ? defaultValue : Integer.parseInt(value);
    }

    private String getPropertyOrDefault(String key, String defaultValue) {
        String value = ConfigReader.getProperty(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
