package com.automation.tests.pim;

import com.automation.pages.pim.EmployeeList;
import com.automation.pages.pim.PIMPage;
import com.automation.pages.pim.ReportPage;
import com.automation.tests.BaseTest;
import com.automation.utils.ConfigReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ReportsTest extends BaseTest {

    PIMPage pimPage;
    ReportPage reportPage;
    SoftAssert softAssert;

    @BeforeMethod
    public void setUp() {
        navBar.gotoPIMPage();
        pimPage = new PIMPage(driver);
        reportPage = pimPage.clickOnReports();
        softAssert = new SoftAssert();
    }

    @Test(description = "Search report by name")
    public void verifySearchReportName(){
        String name = ConfigReader.getProperty("REPORT_NAME");
        String searchName = reportPage.searchReportName(name).getFirstReport();
        softAssert.assertTrue(searchName.contains(name), "Search successful");
    }

    @Test(description = "Verify delete report")
    public void verifyDeleteFirstReport(){
        int beforeSize = reportPage.reportSize();
        reportPage.acceptDeleteFirstReport();
        int afterSize = reportPage.reportSize();
        softAssert.assertTrue(beforeSize == afterSize, "Delete report successful");
    }
}
