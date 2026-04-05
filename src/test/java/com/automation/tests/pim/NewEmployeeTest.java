package com.automation.tests.pim;

import com.automation.pages.pim.AddEmployee;
import com.automation.pages.pim.EmployeeList;
import com.automation.pages.pim.PIMPage;
import com.automation.tests.BaseTest;
import com.automation.utils.ConfigReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class NewEmployeeTest extends BaseTest {

    PIMPage pimPage;
    AddEmployee addEmployee;
    SoftAssert softAssert;

    @BeforeMethod
    public void setUp() {
        navBar.gotoPIMPage();
        pimPage = new PIMPage(driver);
        addEmployee = pimPage.clickOnAddEmployee();
        softAssert = new SoftAssert();
    }

    @Test(description = "Add new employee successful")
    public void verifyAddSuccessful(){
        String fname = ConfigReader.getProperty("EMPLOYEE_FNAME");
        String mname = ConfigReader.getProperty("EMPLOYEE_MNAME");
        String lname = ConfigReader.getProperty("EMPLOYEE_LNAME");
        addEmployee.fillCorrectNewEmployee(fname,mname, lname);
        softAssert.assertTrue(driver.getCurrentUrl().contains("viewPersonalDetails"), "Add new employee successful");
    }

    @Test(description = "Add new employee fail")
    public void verifyAddWithNullFName(){
        String mname = ConfigReader.getProperty("EMPLOYEE_MNAME");
        String lname = ConfigReader.getProperty("EMPLOYEE_LNAME");
        addEmployee.fillNewEmployeeWithNullFirstName(mname, lname);
        softAssert.assertFalse(driver.getCurrentUrl().contains("viewPersonalDetails"), "Add new employee fail");
    }
}
