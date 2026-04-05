package com.automation.tests.pim;

import com.automation.pages.pim.EmployeeList;
import com.automation.pages.pim.PIMPage;
import com.automation.tests.BaseTest;
import com.automation.utils.ConfigReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class EmployeeListTest extends BaseTest {
    PIMPage pimPage;
    EmployeeList employeeList;
    SoftAssert softAssert;

    @BeforeMethod
    public void setUp() {
        navBar.gotoPIMPage();
        pimPage = new PIMPage(driver);
        employeeList = pimPage.clickOnEmployeeList();
        softAssert = new SoftAssert();
    }

    @Test(description = "Search with name")
    public void verifySearchWithName(){
        String name = ConfigReader.getProperty("EMPLOYEE_NAME");
        boolean verifySearch = employeeList.searchEmployeeName(name).verifySearchByName(name);
        softAssert.assertTrue(verifySearch, "Search by name successful");
    }

    @Test(description = "Search with id")
    public void verifySearchWithId(){
        String id = ConfigReader.getProperty("EMPLOYEE_ID");
        boolean verifySearch = employeeList.searchEmployeeId(id).verifySearchById(id);
        softAssert.assertTrue(verifySearch, "Search by id successful");
    }

    @Test(description = "Verify delete first record")
    public void verifyDeleteSuccessful(){
        int beforeSize = employeeList.employeeSize();
        employeeList.firstDeleteEmployee().acceptDeleteFirstEmployee();
        int afterSize = employeeList.employeeSize();
        softAssert.assertTrue(afterSize == beforeSize -1, "Delete  successful");
    }

    @Test(description = "Verify cancel delete first record")
    public void verifyCancelDelete(){
        int beforeSize = employeeList.employeeSize();
        employeeList.firstDeleteEmployee().cancelDeleteFirstEmployee();
        int afterSize = employeeList.employeeSize();
        softAssert.assertTrue(afterSize == beforeSize, "Cancel delete  successful");
    }

    @Test(description = "Verify reset button")
    public void verifyResetButton(){
        String name = ConfigReader.getProperty("EMPLOYEE_NAME");
        String resetText = employeeList.verifyResetButton(name);
        softAssert.assertNull(resetText, "Reset successful");
    }
}
