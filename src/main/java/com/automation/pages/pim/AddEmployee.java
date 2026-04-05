package com.automation.pages.pim;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class AddEmployee extends BasePage {
    private final By pageTitle= By.xpath("//h6[normalize-space()='Add Employee']");
    private final By firstNameInput= By.xpath("//input[@placeholder='First Name']");
    private final By middleNameInput= By.xpath("//input[@placeholder='Middle Name']");
    private final By lastNameInput= By.xpath("//input[@placeholder='Last Name']");
    private final By employeeIdInput= By.xpath("//div[@class='oxd-input-group oxd-input-field-bottom-space']//div//input[@class='oxd-input oxd-input--active']");
    private final By saveButton= By.xpath("//button[normalize-space()='Save']");
    private final By imgInput= By.xpath("//input[@type=\"file\"]");

    public AddEmployee(WebDriver driver) {
        super(driver);
    }

    private void fillNewEmployee(String firstname, String middlename, String lastname){
        sendKeys(firstNameInput,firstname);
        sendKeys(middleNameInput, middlename);
        sendKeys(lastNameInput, lastname);
        sendKeys(employeeIdInput, String.valueOf(System.currentTimeMillis()/1000));
        String imagePath = new File("src/test/resources/img/avata.jpg").getAbsolutePath();
        driver.findElement(imgInput).sendKeys(imagePath);
        driver.findElement(saveButton).click();

    }

    public PersonalDetailPage fillCorrectNewEmployee(String firstname, String middlename, String lastname){
        fillNewEmployee(firstname, middlename,lastname);
        return new PersonalDetailPage(driver);
    }

    public void fillNewEmployeeWithNullFirstName( String middlename, String lastname) {
        fillNewEmployee("", middlename,lastname);
    }

}
