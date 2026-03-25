package com.automation.utils;

import com.aventstack.extentreports.ExtentTest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ExtentTestManager {
    private static Map<Long, ExtentTest> extentTestMap = new ConcurrentHashMap<>();

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get(Thread.currentThread().getId());
    }

    public static synchronized ExtentTest startTest(String testName) {
        ExtentTest test = ExtentManager.getExtentReports().createTest(testName);
        extentTestMap.put(Thread.currentThread().getId(), test);
        return test;
    }
}
