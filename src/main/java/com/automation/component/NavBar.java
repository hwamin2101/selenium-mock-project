package com.automation.component;

import com.automation.pages.BasePage;
import com.automation.pages.performance.PerformancePage;
import com.automation.pages.pim.PIMPage;
import com.automation.pages.recruitment.RecruitmentPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavBar extends BasePage {
    private static final By PIM_LINK = By.xpath("//a[@href='/web/index.php/pim/viewPimModule']");
    private static final By PERFORM_LINK = By.xpath("//a[@href='/web/index.php/performance/viewPerformanceModule']");
    private static final By RECRUIT_LINK = By.xpath("//a[@href='/web/index.php/recruitment/viewRecruitmentModule']");

    public NavBar(WebDriver driver) {
        super(driver);
    }

    public PIMPage gotoPIMPage() {
        click(PIM_LINK);
        return new PIMPage(driver);
    }

    public PerformancePage gotoPerformancePage() {
        click(PERFORM_LINK);
        return new PerformancePage(driver);
    }

    public RecruitmentPage gotoRecruitmentPage() {
        click(RECRUIT_LINK);
        return new RecruitmentPage(driver);
    }
}
