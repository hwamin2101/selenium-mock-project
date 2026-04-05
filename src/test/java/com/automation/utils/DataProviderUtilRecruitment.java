package com.automation.utils;
import org.testng.annotations.DataProvider;

public class DataProviderUtilRecruitment {
    @DataProvider(name = "candidateValidationData")
    public static Object[][] candidateValidationData() {
        return ExcelUtilsRecruitment.getDataProvider("CandidateValidation");
    }

    @DataProvider(name = "candidateData")
    public static Object[][] candidateData() {
        return ExcelUtilsRecruitment.getDataProvider("CandidateData");
    }

    @DataProvider(name = "vacancyData")
    public static Object[][] vacancyData() {
        return ExcelUtilsRecruitment.getDataProvider("VacancyData");
    }

    @DataProvider(name = "candidateStatusData")
    public static Object[][] candidateStatusData() {
        return ExcelUtilsRecruitment.getDataProvider("CandidateStatusData");
    }

    @DataProvider(name = "candidateMethodData")
    public static Object[][] candidateMethodData() {
        return ExcelUtilsRecruitment.getDataProvider("CandidateMethodData");
    }

    @DataProvider(name = "vacancyStatusData")
    public static Object[][] vacancyStatusData() {
        return ExcelUtilsRecruitment.getDataProvider("VacancyStatusData");
    }

    @DataProvider(name = "vacancyCreateData")
    public static Object[][] vacancyCreateData() {
        return ExcelUtilsRecruitment.getDataProvider("VacancyCreateData");
    }
}
