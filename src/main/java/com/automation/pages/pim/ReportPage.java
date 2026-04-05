package com.automation.pages.pim;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ReportPage extends BasePage {

    private final By firstReportDeleteButton= By.xpath("//button[i[contains(@class, 'oxd-icon bi-trash')]][1]");
    private final By firstReportDetailButton=By.xpath("(//button[i[contains(@class, 'oxd-icon bi-file-text-fill')]])[1]");
    private final By firstReportEditButton= By.xpath("//button[i[contains(@class, 'oxd-icon bi-pencil-fill')]][1]");
    private final By deleteReportRejectButton= By.xpath("//button[normalize-space()='No, Cancel']");
    private final By deleteReportAcceptButton= By.xpath("//button[normalize-space()='Yes, Delete']");
    private final By toastPanel=By.xpath("//div[@id=\"oxd-toaster_1\"]");
    private final By reportNames= By.xpath("//div[@class='oxd-table-card']//div//div[2]//div");
    private final By nextPageButton= By.xpath("//ul[@class='oxd-pagination__ul']/li[last()]/button[@class='oxd-pagination-page-item oxd-pagination-page-item--previous-next']");
    private final By addReportPageButton= By.xpath("//button[normalize-space()='Add']");
    private final By reportNameInput= By.xpath("//label[text()='Report Name']/ancestor::div[contains(@class, 'oxd-input-group')]//input");
    private final By searchButton=By.xpath("//button[normalize-space()='Search']");
    private final By resultText = By.xpath("//span[contains(text(),'Records Found')]");

    private final By firstReportName=By.xpath("(//div[@class='oxd-table-card']//div//div[2]//div)[1]");

    public ReportPage(WebDriver driver) {
        super(driver);
    }

    public String getFirstReport(){
        return driver.findElement(firstReportName).getText();
    }

    public ReportPage searchReportName(String name){
        sendKeys(reportNameInput,name);
        click(searchButton);
        return this;
    }

    public void rejectDeleteFirstReport(){
        click(firstReportDeleteButton);
        click(deleteReportRejectButton);
    }

    public String acceptDeleteFirstReport(){
        click(firstReportDeleteButton);
        click(deleteReportAcceptButton);
        return getText(toastPanel);
    }

    public String getFirstReportName(){
        return getText(firstReportName);
    }

    public int reportSize() {
        int resultCount = Integer.parseInt(getText(resultText).replaceAll("[^0-9]", ""));
        return resultCount;
    }
}
