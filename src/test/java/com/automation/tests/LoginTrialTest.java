package com.automation.tests;

import org.testng.annotations.Test;

public class LoginTrialTest extends BaseTest{
    @Test
     public void loginTest() {
        navBar.gotoPIMPage();
        navBar.gotoRecruitmentPage();
        navBar.gotoPerformancePage();
    }
}
