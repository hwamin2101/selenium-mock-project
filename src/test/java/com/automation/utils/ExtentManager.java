package com.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;

public class ExtentManager {
    private static ExtentReports extentReports;

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            File dir = new File("report");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            ExtentSparkReporter reporter = new ExtentSparkReporter("report/ExtentReport.html");
            reporter.config().setReportName("JPetStore Results");

            extentReports = new ExtentReports();
            extentReports.attachReporter(reporter);
        }
        return extentReports;
    }
}