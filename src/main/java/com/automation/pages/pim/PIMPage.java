package com.automation.pages.pim;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class PIMPage extends BasePage {
    //Page Elements
    private final By employeeList = By.xpath("//a[text()='Employee List']");
    private final By addEmployee = By.xpath("//a[text()='Add Employee']");
    private final By reports = By.xpath("//a[text()='Reports']");

    public PIMPage(WebDriver driver) {
        super(driver);
    }

    //Click On Employee List
    public EmployeeList clickOnEmployeeList() {
        click(employeeList);
        return new EmployeeList(driver);
    }

    //Click On Add Employee
    public AddEmployee clickOnAddEmployee() {
        click(addEmployee);
        return new AddEmployee(driver);
    }

    //Click On Reports
    public ReportPage clickOnReports() {
        click(reports);
        return new ReportPage(driver);
    }

}
