package com.automation.pages.pim;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class EmployeeList extends BasePage {

    private final By searchCollapseButton = By.xpath("//div[@class='--toggle']//button[@type='button']");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By employeeNameInput = By.xpath("//input[@placeholder='Type for hints...']");
    private final By employeeIdInput = By.xpath("//input[@class='oxd-input oxd-input--active']");
    private final By addEmployeeButton = By.xpath("//button[normalize-space()='Add']");
    private final By employeeTable = By.xpath("//div[@class='oxd-table-card']//div");
    private final By deleteDialogButton = By.xpath("//button[normalize-space()='Yes, Delete']");
    private final By deleteRecordsText = By.xpath("//p[text()='Are you Sure?']");
    private final By canceldeleteButton = By.xpath("//button[normalize-space()='No, Cancel']");
    private final By acceptDeleteButton= By.xpath("//button[normalize-space()='Yes, Delete']");
    private final By resultText = By.xpath("//span[contains(text(),'Records Found')]");
    private final By noResultText = By.xpath("//span[text()='No Records Found')]");

    public EmployeeList(WebDriver driver) {
        super(driver);
    }

    public EmployeeList searchEmployeeName(String name) {
        if (!isDisplayed(searchButton)) {
            click(searchCollapseButton);
        }
        sendKeys(employeeNameInput, name);
        click(searchButton);
        return this;
    }

    public EmployeeList searchEmployeeId(String id) {
        if (!isDisplayed(searchButton)) {
            click(searchCollapseButton);
        }
        sendKeys(employeeIdInput, id);
        click(searchButton);
        return this;
    }

    public boolean verifyNoResultFound() {
        return isDisplayed(noResultText);
    }

    public boolean verifySearchByName(String name) {
        List<WebElement> employeeList = driver.findElements(employeeTable);
        if (employeeList.size() > 0) {
            boolean check = false;
            for (WebElement emp : employeeList) {
                List<WebElement> cells = emp.findElements(By.xpath(".//div"));
                String firstName = cells.get(2).getText();
                String lastName = cells.get(3).getText();
                if (firstName.contains(name) || lastName.contains(name)) {
                    check = true;
                    break;
                }
            }
            return check;
        } else {
            return false;
        }
    }

    public boolean verifySearchById(String id) {
        return verifyNoResultFound();
    }

    public EmployeeList firstDeleteEmployee() {
        boolean verifyNoResult = verifyNoResultFound();
        if (!verifyNoResult) {
            List<WebElement> employeeList = driver.findElements(employeeTable);
            WebElement firstEmployee = employeeList.getFirst();
            List<WebElement> cells = firstEmployee.findElements(By.xpath(".//div"));
            WebElement actionCell = cells.get(8);
            List<WebElement> buttons = actionCell.findElements(By.xpath(".//button"));
            WebElement deleteBtn = buttons.get(1);
            deleteBtn.click();
        }
        return this;
    }

    public void cancelDeleteFirstEmployee() {
        click(canceldeleteButton);
    }

    public void acceptDeleteFirstEmployee() {
        click(acceptDeleteButton);
    }

    public String verifyResetButton(String name) {
        sendKeys(employeeNameInput, name);
        click(resetButton);
        return getText(employeeNameInput);
    }

    public int employeeSize() {
        int employeeCount = Integer.parseInt(getText(resultText).replaceAll("[^0-9]", ""));
        return employeeCount;
    }
}
