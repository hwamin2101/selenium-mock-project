package com.automation.pages.pim;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PersonalDetailPage extends BasePage {
    private final By employeeName=By.xpath("//div[@class='orangehrm-edit-employee-name']//h6");

    public PersonalDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getDetailName() throws InterruptedException {
        return getText(employeeName);
    }

}
