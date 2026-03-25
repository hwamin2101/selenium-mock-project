package com.automation.utils;

import com.automation.tests.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Write log for each test
        ExtentTestManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // 1. Get driver is running
        Object testClass = result.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();

        if (driver != null) {
            String screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            ExtentTestManager.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable());
            ExtentTestManager.getTest().addScreenCaptureFromBase64String(screenshot, "Error at here");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        // Export report.html
        ExtentManager.getExtentReports().flush();
    }
}
