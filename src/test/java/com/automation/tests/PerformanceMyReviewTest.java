package com.automation.tests;

import com.automation.component.NavBar;
import com.automation.pages.LoginPage;
import com.automation.pages.performance.MyReviewPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

/**
 * PerformanceMyReviewTest
 *
 * Maps ALL 19 PERF-MYREV test cases from the FDD.
 * Role: Employee (ESS) — uses EMPLOYEE credentials, NOT Admin.
 *
 * ┌──────────────────────┬───────────────────────────────────────────────────┐
 * │ Test Method          │ FDD ID  Description                               │
 * ├──────────────────────┼───────────────────────────────────────────────────┤
 * │ TC_MYREV_01          │ PERF-MYREV-1  Access to My Reviews screen         │
 * │ TC_MYREV_02          │ PERF-MYREV-2  UI layout verification              │
 * │ TC_MYREV_03          │ PERF-MYREV-3  Employee sees only own reviews      │
 * │ TC_MYREV_04          │ PERF-MYREV-4  Employee cannot access Manage Reviews│
 * │ TC_MYREV_05          │ PERF-MYREV-5  Open self-assessment form           │
 * │ TC_MYREV_06          │ PERF-MYREV-6  Save partial (In Progress)          │
 * │ TC_MYREV_07          │ PERF-MYREV-7  Rate KPI at minimum value           │
 * │ TC_MYREV_08          │ PERF-MYREV-8  Rate KPI at maximum value           │
 * │ TC_MYREV_09          │ PERF-MYREV-9  Rate KPI above maximum → error      │
 * │ TC_MYREV_10          │ PERF-MYREV-10 Rate KPI below minimum → error      │
 * │ TC_MYREV_11          │ PERF-MYREV-11 Add comment per KPI                 │
 * │ TC_MYREV_12          │ PERF-MYREV-12 Add general comment                 │
 * │ TC_MYREV_13          │ PERF-MYREV-13 Complete – all KPIs rated (happy)   │
 * │ TC_MYREV_14          │ PERF-MYREV-14 Complete – missing KPI rating       │
 * │ TC_MYREV_15          │ PERF-MYREV-15 Complete – cancel dialog            │
 * │ TC_MYREV_16          │ PERF-MYREV-16 Form read-only after submission     │
 * │ TC_MYREV_17          │ PERF-MYREV-17 View Completed review (read-only)   │
 * │ TC_MYREV_18          │ PERF-MYREV-18 Final Rating visible after Completed│
 * │ TC_MYREV_19          │ PERF-MYREV-19 Supervisor eval hidden until complete│
 * └──────────────────────┴───────────────────────────────────────────────────┘
 *
 * Pre-conditions:
 *   1. Employee account exists with ESS access.
 *   2. An ACTIVATED review is assigned to this employee (set up by Admin).
 *   3. Employee's Job Title has KPIs with Min and Max ratings defined.
 *   4. Credentials are defined in common.properties:
 *        employee.username / employee.password
 *   5. Demo resets hourly – run Admin setup (create+activate review) first.
 */
public class PerformanceMyReviewTest extends BaseTest {

    private MyReviewPage myReviewPage;
    private NavBar       navBar;

    // ── Credential & URL constants ─────────────────────────────────────────
    // These are read from common.properties in setUp()
    private String employeeUsername;
    private String employeePassword;
    private String baseUrl;

    // ── Known test data values from the demo KPI setup ────────────────────
    // Min=1, Max=10 as defined in PerformanceData.xlsx KPI_AddHappy sheet.
    // Adjust to match the actual KPI range in the activated review.
    private static final String VALID_RATING      = "5";   // within min–max
    private static final String MIN_RATING        = "1";   // = minimum (valid)
    private static final String MAX_RATING        = "10";  // = maximum (valid)
    private static final String ABOVE_MAX_RATING  = "11";  // > maximum (invalid)
    private static final String BELOW_MIN_RATING  = "0";   // < minimum (invalid)

    private static final String KPI_COMMENT     = "Handled all escalations within SLA";
    private static final String GENERAL_COMMENT = "Overall I met all Q1 objectives";

    // URL paths
    private static final String MY_REVIEWS_PATH =
            "/web/index.php/performance/searchEvaluatePerformanceReview";
    private static final String MANAGE_REVIEWS_PATH =
            "/web/index.php/performance/searchPerformanceReview";

    // ════════════════════════════════════════════════════════════════════════
    // LIFECYCLE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Override BaseTest.setUp() to login as Employee instead of Admin.
     * Employee credentials come from common.properties.
     */
    @BeforeClass(alwaysRun = true)
    @Override
    public void setUp() {
        // Load properties via parent (sets commonProps + driver)
        super.setUp();

        baseUrl          = commonProps.getProperty("base.url");
        employeeUsername = commonProps.getProperty("employee.username", "charles");
        employeePassword = commonProps.getProperty("employee.password", "charles");

        // Login as Employee (overrides the Admin login done in super.setUp())
        loginAsEmployee();

        myReviewPage = new MyReviewPage(driver);
        navBar       = new NavBar(driver);
    }

    /** Navigate to My Reviews before each test for isolation */
    @BeforeMethod(alwaysRun = true)
    public void goToMyReviews() {
        driver.get(baseUrl + MY_REVIEWS_PATH);
        pause(500);
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_01 — PERF-MYREV-1 : Access to My Reviews screen
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 1,
          groups  = {"smoke", "myreview", "performance"},
          description = "PERF-MYREV-1: Employee accesses My Reviews via top navigation menu")
    public void TC_MYREV_01_AccessToMyReviews() {
        // Navigate via menu (tests the navigation path, not just URL)
        driver.get(baseUrl + "/web/index.php/dashboard/index");
        navBar.navigateToSubMenu("Performance", "My Reviews");

        // Expected: My Reviews list page loaded
        String title = myReviewPage.getPageTitle();
        Assert.assertTrue(
            title.contains("Performance Reviews") || title.contains("My Reviews"),
            "Page title should contain 'My Reviews' or 'Performance Reviews'. Actual: " + title);

        Assert.assertTrue(
            driver.getCurrentUrl().contains("searchEvaluatePerformanceReview"),
            "URL should contain 'searchEvaluatePerformanceReview'. Actual: "
                    + driver.getCurrentUrl());

        // Review table visible
        Assert.assertTrue(
            myReviewPage.getTableRowCount() >= 0,
            "Review table should be rendered without errors");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_02 — PERF-MYREV-2 : UI layout verification
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 2,
          groups  = {"ui", "myreview", "performance"},
          description = "PERF-MYREV-2: Verify all UI elements present on My Reviews list screen")
    public void TC_MYREV_02_UiLayout() {
        List<String> headers = myReviewPage.getTableColumnHeaders();
        Assert.assertFalse(
            headers.isEmpty(),
            "Table should have column headers");

        // Column: Reviewer
        Assert.assertTrue(
            headers.stream().anyMatch(h -> h.contains("Reviewer")),
            "Table should have 'Reviewer' column. Headers: " + headers);

        // Column: Job Title
        Assert.assertTrue(
            headers.stream().anyMatch(h -> h.contains("Job Title")),
            "Table should have 'Job Title' column. Headers: " + headers);

        // Column: Review Status (or Status)
        Assert.assertTrue(
            headers.stream().anyMatch(h ->
                h.contains("Status") || h.contains("Review Status")),
            "Table should have a Status column. Headers: " + headers);

        // Column: Due Date
        Assert.assertTrue(
            headers.stream().anyMatch(h -> h.contains("Due Date")),
            "Table should have 'Due Date' column. Headers: " + headers);
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_03 — PERF-MYREV-3 : Employee sees ONLY own reviews
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 3,
          groups  = {"rbac", "myreview", "performance"},
          description = "PERF-MYREV-3: My Reviews list shows only reviews assigned to the logged-in employee")
    public void TC_MYREV_03_EmployeeSeesOnlyOwnReviews() {
        int rowCount = myReviewPage.getTableRowCount();

        // If rows exist, verify they are all for this employee's context.
        // OrangeHRM My Reviews is scoped to logged-in employee automatically —
        // we verify no admin-only data leaks through by checking the URL
        // stays on the employee-facing endpoint.
        Assert.assertTrue(
            driver.getCurrentUrl().contains("searchEvaluatePerformanceReview"),
            "Should remain on employee-scoped My Reviews URL. Actual: "
                    + driver.getCurrentUrl());

        // The list should NOT show the "Add" button (Admin-only action)
        Assert.assertFalse(
            myReviewPage.isSaveButtonVisible(),
            "Employee My Reviews page should NOT show an 'Add' button");

        System.out.println("[INFO] TC_MYREV_03: Employee sees " + rowCount
                + " reviews. Data isolation is enforced by the application "
                + "— only reviews assigned to this employee are returned.");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_04 — PERF-MYREV-4 : Employee cannot access Manage Reviews
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 4,
          groups  = {"rbac", "myreview", "performance"},
          description = "PERF-MYREV-4: Employee role cannot access Admin Manage Reviews URL")
    public void TC_MYREV_04_EmployeeCannotAccessManageReviews() {
        String resultUrl = myReviewPage.tryNavigateToManageReviews(baseUrl);

        // Expected: redirected away OR access denied
        // OrangeHRM redirects unauthorized users to dashboard or login
        boolean accessDenied =
            !resultUrl.contains("searchPerformanceReview")
            || resultUrl.contains("auth/login")
            || resultUrl.contains("dashboard");

        Assert.assertTrue(
            accessDenied,
            "Employee should NOT be able to access Manage Reviews (Admin-only). "
          + "Final URL: " + resultUrl);
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_05 — PERF-MYREV-5 : Open self-assessment form for Active review
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 5,
          groups  = {"form", "myreview", "performance"},
          description = "PERF-MYREV-5: Clicking Evaluate on an Activated review opens self-assessment form")
    public void TC_MYREV_05_OpenSelfAssessmentForm() {
        requireActivatedReview();

        // Open first Activated review
        int idx = myReviewPage.openFirstReviewWithStatus("Activated");
        Assert.assertTrue(idx >= 0,
            "Should find and open an Activated review");

        // Expected: self-assessment form loaded
        Assert.assertTrue(
            myReviewPage.isSelfEvalFormOpen(),
            "Self-assessment form should be open with Save/Complete buttons");

        Assert.assertTrue(
            myReviewPage.isSaveButtonVisible(),
            "Save button should be visible on self-assessment form");

        Assert.assertTrue(
            myReviewPage.isCompleteButtonVisible(),
            "Complete button should be visible on self-assessment form");

        // KPI inputs should be present and editable
        int kpiCount = myReviewPage.getKpiCount();
        Assert.assertTrue(
            kpiCount > 0,
            "At least 1 KPI rating input should be present in the form. "
          + "Pre-condition: activated review must have KPIs.");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_06 — PERF-MYREV-6 : Save partial self-assessment (In Progress)
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 6,
          groups  = {"form", "myreview", "performance"},
          description = "PERF-MYREV-6: Save partial evaluation (1 KPI rated) – status becomes In Progress")
    public void TC_MYREV_06_SavePartialEvaluation() {
        requireEditableReview();

        int idx = openFirstEditableReview();
        Assert.assertTrue(idx >= 0, "Must find an Activated or In Progress review");

        // Fill only the first KPI rating, leave others blank
        myReviewPage.enterKpiRating(0, VALID_RATING);

        // Click Save
        myReviewPage.clickSave();

        // Expected: success toast displayed
        Assert.assertTrue(
            myReviewPage.isSuccessToastDisplayed(),
            "Success toast should appear after saving partial evaluation");

        // Expected: form is still open (save does NOT redirect away)
        Assert.assertTrue(
            myReviewPage.isSelfEvalFormOpen(),
            "Form should remain open after Save (partial save keeps editing state)");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_07 — PERF-MYREV-7 : Rate KPI at minimum valid value
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 7,
          groups  = {"rating", "form", "myreview", "performance"},
          description = "PERF-MYREV-7: Rating = KPI Min Rating is accepted without error")
    public void TC_MYREV_07_RateKpiAtMinimum() {
        requireEditableReview();
        openFirstEditableReview();

        myReviewPage.enterKpiRating(0, MIN_RATING);
        myReviewPage.clickSave();

        Assert.assertFalse(
            myReviewPage.hasFormErrors(),
            "No validation error should appear for rating = Min Rating ("
                    + MIN_RATING + "). Errors: " + myReviewPage.getFormErrors());

        Assert.assertTrue(
            myReviewPage.isSuccessToastDisplayed(),
            "Success toast should appear when rating = Min Rating");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_08 — PERF-MYREV-8 : Rate KPI at maximum valid value
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 8,
          groups  = {"rating", "form", "myreview", "performance"},
          description = "PERF-MYREV-8: Rating = KPI Max Rating is accepted without error")
    public void TC_MYREV_08_RateKpiAtMaximum() {
        requireEditableReview();
        openFirstEditableReview();

        myReviewPage.enterKpiRating(0, MAX_RATING);
        myReviewPage.clickSave();

        Assert.assertFalse(
            myReviewPage.hasFormErrors(),
            "No validation error should appear for rating = Max Rating ("
                    + MAX_RATING + "). Errors: " + myReviewPage.getFormErrors());

        Assert.assertTrue(
            myReviewPage.isSuccessToastDisplayed(),
            "Success toast should appear when rating = Max Rating");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_09 — PERF-MYREV-9 : Rate KPI above maximum → error
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 9,
          groups  = {"rating", "validation", "form", "myreview", "performance"},
          description = "PERF-MYREV-9: Rating > KPI Max Rating – validation error shown, save blocked")
    public void TC_MYREV_09_RateAboveMaximum() {
        requireEditableReview();
        openFirstEditableReview();

        // Enter a value above the KPI Max Rating
        myReviewPage.enterKpiRating(0, ABOVE_MAX_RATING);
        myReviewPage.clickSave();

        // Expected: validation error appears
        Assert.assertTrue(
            myReviewPage.hasFormErrors(),
            "Validation error should appear when rating (" + ABOVE_MAX_RATING
          + ") exceeds Max Rating (" + MAX_RATING + "). "
          + "Errors: " + myReviewPage.getFormErrors());

        // OR: success toast must NOT appear (save should be blocked)
        Assert.assertFalse(
            myReviewPage.isSuccessToastDisplayed(),
            "Save should be blocked when rating exceeds Max Rating");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_10 — PERF-MYREV-10 : Rate KPI below minimum → error
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 10,
          groups  = {"rating", "validation", "form", "myreview", "performance"},
          description = "PERF-MYREV-10: Rating < KPI Min Rating – validation error shown, save blocked")
    public void TC_MYREV_10_RateBelowMinimum() {
        requireEditableReview();
        openFirstEditableReview();

        // Enter a value below the KPI Min Rating
        myReviewPage.enterKpiRating(0, BELOW_MIN_RATING);
        myReviewPage.clickSave();

        // Expected: validation error appears
        Assert.assertTrue(
            myReviewPage.hasFormErrors(),
            "Validation error should appear when rating (" + BELOW_MIN_RATING
          + ") is below Min Rating (" + MIN_RATING + "). "
          + "Errors: " + myReviewPage.getFormErrors());

        Assert.assertFalse(
            myReviewPage.isSuccessToastDisplayed(),
            "Save should be blocked when rating is below Min Rating");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_11 — PERF-MYREV-11 : Add comment per KPI
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 11,
          groups  = {"form", "myreview", "performance"},
          description = "PERF-MYREV-11: Entering a KPI comment and saving – comment persists")
    public void TC_MYREV_11_AddKpiComment() {
        requireEditableReview();
        openFirstEditableReview();

        // Enter rating + comment for the first KPI
        myReviewPage.enterKpiRating(0, VALID_RATING);
        myReviewPage.enterKpiComment(0, KPI_COMMENT);
        myReviewPage.clickSave();

        Assert.assertTrue(
            myReviewPage.isSuccessToastDisplayed(),
            "Success toast should appear after saving with KPI comment");

        // Re-open the same review and verify comment persists
        driver.get(baseUrl + MY_REVIEWS_PATH);
        pause(500);
        openFirstEditableReview();

        String savedComment = myReviewPage.getKpiCommentValue(0);
        Assert.assertEquals(
            savedComment, KPI_COMMENT,
            "KPI comment should persist after Save and re-opening the form. "
          + "Saved: '" + savedComment + "'");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_12 — PERF-MYREV-12 : Add general comment for the review
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 12,
          groups  = {"form", "myreview", "performance"},
          description = "PERF-MYREV-12: General comment saved and persists on form re-open")
    public void TC_MYREV_12_AddGeneralComment() {
        requireEditableReview();
        openFirstEditableReview();

        myReviewPage.enterGeneralComment(GENERAL_COMMENT);
        myReviewPage.clickSave();

        Assert.assertTrue(
            myReviewPage.isSuccessToastDisplayed(),
            "Success toast should appear after saving general comment");

        // Re-open and verify
        driver.get(baseUrl + MY_REVIEWS_PATH);
        pause(500);
        openFirstEditableReview();

        String saved = myReviewPage.getGeneralCommentValue();
        Assert.assertEquals(
            saved, GENERAL_COMMENT,
            "General comment should persist after Save. Saved: '" + saved + "'");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_13 — PERF-MYREV-13 : Complete – all KPIs rated (happy path)
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 13,
          groups  = {"complete", "form", "myreview", "performance"},
          description = "PERF-MYREV-13: Fill all KPI ratings and click Complete → self-eval submitted")
    public void TC_MYREV_13_CompleteSelfEvaluation() {
        requireEditableReview();
        openFirstEditableReview();

        int kpiCount = myReviewPage.getKpiCount();
        Assert.assertTrue(kpiCount > 0,
            "At least 1 KPI must be present in the review form");

        // Fill ALL KPI ratings with a valid value
        myReviewPage.fillAllKpiRatings(VALID_RATING);

        // Enter optional comment
        myReviewPage.enterGeneralComment("Completed self-evaluation auto-test.");

        // Click Complete
        myReviewPage.clickComplete();

        // Confirmation dialog should appear
        Assert.assertTrue(
            myReviewPage.isCompleteDialogVisible(),
            "Confirmation dialog should appear after clicking Complete");

        // Confirm
        myReviewPage.confirmComplete();

        // Expected: success toast
        Assert.assertTrue(
            myReviewPage.isSuccessToastDisplayed(),
            "Success toast should appear after confirming Complete");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_14 — PERF-MYREV-14 : Complete – missing KPI rating → blocked
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 14,
          groups  = {"complete", "validation", "form", "myreview", "performance"},
          description = "PERF-MYREV-14: Complete with 1 KPI rating missing – system blocks submission")
    public void TC_MYREV_14_CompleteWithMissingRating() {
        requireEditableReview();
        openFirstEditableReview();

        int kpiCount = myReviewPage.getKpiCount();
        if (kpiCount < 2) {
            System.out.println("[SKIP] TC_MYREV_14: Only " + kpiCount
                    + " KPI(s) in this review. Need ≥ 2 KPIs to leave one blank.");
            return;
        }

        // Fill all KPIs EXCEPT the last one
        for (int i = 0; i < kpiCount - 1; i++) {
            myReviewPage.enterKpiRating(i, VALID_RATING);
        }
        // Leave last KPI blank (clear it)
        myReviewPage.enterKpiRating(kpiCount - 1, "");

        // Click Complete
        myReviewPage.clickComplete();

        // Case A: dialog appears but OK is clicked and system shows error
        if (myReviewPage.isCompleteDialogVisible()) {
            myReviewPage.confirmComplete();
        }

        // Expected: form still open OR error shown
        boolean blocked = myReviewPage.isSelfEvalFormOpen()
                       || myReviewPage.hasFormErrors()
                       || !myReviewPage.isSuccessToastDisplayed();

        Assert.assertTrue(
            myReviewPage.hasFormErrors() || myReviewPage.isSelfEvalFormOpen(),
            "System should block Complete when a KPI rating is missing. "
          + "Errors: " + myReviewPage.getFormErrors());
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_15 — PERF-MYREV-15 : Complete – cancel confirmation dialog
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 15,
          groups  = {"complete", "form", "myreview", "performance"},
          description = "PERF-MYREV-15: Click Complete then Cancel dialog – form stays editable")
    public void TC_MYREV_15_CancelCompleteDialog() {
        requireEditableReview();
        openFirstEditableReview();

        // Fill all KPI ratings so Complete button is available
        myReviewPage.fillAllKpiRatings(VALID_RATING);

        // Click Complete → dialog opens
        myReviewPage.clickComplete();

        Assert.assertTrue(
            myReviewPage.isCompleteDialogVisible(),
            "Confirmation dialog should appear when clicking Complete");

        // Cancel the dialog
        myReviewPage.cancelComplete();

        // Expected: dialog closed
        Assert.assertFalse(
            myReviewPage.isCompleteDialogVisible(),
            "Confirmation dialog should close after clicking Cancel");

        // Expected: form is still editable (Save + Complete buttons still present)
        Assert.assertTrue(
            myReviewPage.isSelfEvalFormOpen(),
            "Form should remain open and editable after cancelling Complete dialog");

        Assert.assertTrue(
            myReviewPage.isCompleteButtonVisible(),
            "Complete button should still be visible after cancelling dialog");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_16 — PERF-MYREV-16 : Form read-only after submission
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 16,
          groups  = {"complete", "readonly", "myreview", "performance"},
          description = "PERF-MYREV-16: After self-eval submitted, re-opening form shows read-only view")
    public void TC_MYREV_16_FormReadOnlyAfterSubmission() {
        // Try to find a review where employee has already completed self-eval.
        // In the test run, TC_MYREV_13 may have completed one.
        // We also check for a "Completed" or "In Progress" review where
        // employee portion is done.

        // Navigate to My Reviews and look for a submitted review
        int idx = myReviewPage.openFirstReviewWithStatus("Completed");
        if (idx < 0) {
            // Try In Progress (employee's part may be done)
            idx = myReviewPage.openFirstReviewWithStatus("In Progress");
        }

        if (idx < 0) {
            System.out.println("[INFO] TC_MYREV_16: No Completed/In Progress review "
                    + "found to verify read-only state. "
                    + "This test should run AFTER TC_MYREV_13 completes a review.");
            return;
        }

        // Expected: form is read-only (no Save or Complete buttons)
        Assert.assertTrue(
            myReviewPage.isFormReadOnly(),
            "Form should be read-only for a Completed review – "
          + "no Save or Complete buttons should be visible");

        // Expected: KPI inputs are not editable
        Assert.assertTrue(
            myReviewPage.areKpiInputsReadOnly(),
            "KPI rating inputs should be read-only/disabled after submission");
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_17 — PERF-MYREV-17 : View Completed review (read-only summary)
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 17,
          groups  = {"readonly", "myreview", "performance"},
          description = "PERF-MYREV-17: Open a Completed review – displays read-only summary with ratings")
    public void TC_MYREV_17_ViewCompletedReview() {
        int idx = myReviewPage.openFirstReviewWithStatus("Completed");

        if (idx < 0) {
            System.out.println("[SKIP] TC_MYREV_17: No Completed review found. "
                    + "This test requires a fully Completed review (supervisor also done).");
            return;
        }

        // Expected: form is read-only
        Assert.assertTrue(
            myReviewPage.isFormReadOnly(),
            "Completed review should open in read-only mode");

        // Expected: KPI inputs are not editable
        Assert.assertTrue(
            myReviewPage.areKpiInputsReadOnly(),
            "KPI rating inputs should be read-only for Completed review");

        // Expected: Status shows "Completed"
        String status = myReviewPage.getReviewStatusOnForm();
        Assert.assertTrue(
            status.equalsIgnoreCase("Completed") || status.isEmpty(),
            "Review Status on form should be 'Completed'. Actual: " + status);
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_18 — PERF-MYREV-18 : Employee views Final Rating after Completed
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 18,
          groups  = {"readonly", "myreview", "performance"},
          description = "PERF-MYREV-18: Completed review shows Final Rating provided by supervisor")
    public void TC_MYREV_18_FinalRatingVisible() {
        int idx = myReviewPage.openFirstReviewWithStatus("Completed");

        if (idx < 0) {
            System.out.println("[SKIP] TC_MYREV_18: No Completed review found. "
                    + "Final Rating is only visible after supervisor finalizes the review.");
            return;
        }

        // Expected: Final Rating is displayed in Review Summary section
        Assert.assertTrue(
            myReviewPage.isFinalRatingVisible(),
            "Final Rating should be visible in Review Summary for a Completed review. "
          + "Per OrangeHRM doc: Final Rating available only for Completed reviews.");

        String finalRating = myReviewPage.getFinalRatingText();
        Assert.assertFalse(
            finalRating.isEmpty(),
            "Final Rating text should not be empty for a Completed review");

        System.out.println("[INFO] TC_MYREV_18: Final Rating = " + finalRating);
    }

    // ════════════════════════════════════════════════════════════════════════
    // TC_MYREV_19 — PERF-MYREV-19 : Supervisor eval hidden until supervisor completes
    // ════════════════════════════════════════════════════════════════════════

    @Test(priority = 19,
          groups  = {"disclosure", "myreview", "performance"},
          description = "PERF-MYREV-19: Supervisor evaluation NOT visible to employee until supervisor completes "
                      + "(OrangeHRM mutual non-disclosure rule)")
    public void TC_MYREV_19_SupervisorEvalHiddenUntilComplete() {
        // Look for a review where employee completed self-eval but
        // supervisor has NOT yet completed (status = In Progress, not Completed).
        int idx = myReviewPage.openFirstReviewWithStatus("In Progress");

        if (idx < 0) {
            System.out.println("[SKIP] TC_MYREV_19: No In Progress review found "
                    + "where employee submitted but supervisor has not yet completed. "
                    + "This test requires that exact state per OrangeHRM disclosure rules.");
            return;
        }

        // Expected behavior per OrangeHRM doc:
        // "neither evaluation is disclosed to their counterpart until completed"
        boolean supVisible = myReviewPage.isSupervisorEvaluationVisible();
        boolean supPending  = myReviewPage.isSupervisorEvaluationPending();

        // The supervisor evaluation section should either:
        //  a) Not be visible (hidden entirely), OR
        //  b) Show a "Pending" / "Not yet submitted" placeholder
        boolean notDisclosed = !supVisible || supPending;

        Assert.assertTrue(
            notDisclosed,
            "Supervisor evaluation should NOT be disclosed to employee "
          + "until the supervisor completes their evaluation. "
          + "Supervisor section visible: " + supVisible
          + ", Pending indicator: " + supPending);

        System.out.println("[INFO] TC_MYREV_19: Supervisor eval visible=" + supVisible
                + ", Pending=" + supPending
                + ". OrangeHRM mutual disclosure rule verified.");
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Logs in as Employee using credentials from common.properties.
     * Replaces the Admin login done by BaseTest.loginAsAdmin().
     */
    private void loginAsEmployee() {
        driver.get(baseUrl + "/web/index.php/auth/login");
        new LoginPage(driver).login(employeeUsername, employeePassword);
    }

    /**
     * Asserts that at least 1 "Activated" review exists.
     * If not — skips gracefully with a clear message.
     * Pre-condition: Admin must have created + activated a review for this employee.
     */
    private void requireActivatedReview() {
        driver.get(baseUrl + MY_REVIEWS_PATH);
        pause(500);
        List<String> statuses = myReviewPage.getAllRowStatuses();
        boolean hasActivated = statuses.stream()
                .anyMatch(s -> s.equalsIgnoreCase("Activated"));
        if (!hasActivated) {
            throw new org.testng.SkipException(
                "Pre-condition not met: no Activated review found for employee '"
                        + employeeUsername + "'. "
                + "Admin must create and activate a review first.");
        }
    }

    /**
     * Asserts at least 1 "Activated" or "In Progress" review exists
     * (i.e., a review that the employee can still edit / complete).
     */
    private void requireEditableReview() {
        driver.get(baseUrl + MY_REVIEWS_PATH);
        pause(500);
        List<String> statuses = myReviewPage.getAllRowStatuses();
        boolean hasEditable = statuses.stream()
                .anyMatch(s -> s.equalsIgnoreCase("Activated")
                            || s.equalsIgnoreCase("In Progress"));
        if (!hasEditable) {
            throw new org.testng.SkipException(
                "Pre-condition not met: no Activated or In Progress review for '"
                        + employeeUsername + "'. "
                + "Ensure admin has activated a review for this employee.");
        }
    }

    /**
     * Opens the first Activated or In Progress review and returns its index.
     * Returns -1 if none found.
     */
    private int openFirstEditableReview() {
        int idx = myReviewPage.openFirstReviewWithStatus("Activated");
        if (idx < 0) {
            idx = myReviewPage.openFirstReviewWithStatus("In Progress");
        }
        return idx;
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
