package com.automation.pages.performance;

import com.automation.pages.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * MyReviewPage – Page Object for Performance > My Reviews screen.
 * Role: Employee (ESS)
 * URL : /web/index.php/performance/searchEvaluatePerformanceReview
 *
 * Covers ALL test cases:
 *   PERF-MYREV-1  : Access to My Reviews screen (Employee)
 *   PERF-MYREV-2  : UI layout verification
 *   PERF-MYREV-3  : Employee sees only their own reviews (data isolation)
 *   PERF-MYREV-4  : Employee cannot access Manage Reviews (RBAC)
 *   PERF-MYREV-5  : Open self-assessment form for Active review
 *   PERF-MYREV-6  : Save partial self-assessment (In Progress)
 *   PERF-MYREV-7  : Rate KPI at minimum valid value
 *   PERF-MYREV-8  : Rate KPI at maximum valid value
 *   PERF-MYREV-9  : Rate KPI above maximum value → error
 *   PERF-MYREV-10 : Rate KPI below minimum value → error
 *   PERF-MYREV-11 : Add comment per KPI (optional field)
 *   PERF-MYREV-12 : Add general comment for the review
 *   PERF-MYREV-13 : Complete self-assessment – all KPIs rated (happy path)
 *   PERF-MYREV-14 : Complete – missing KPI rating → blocked
 *   PERF-MYREV-15 : Complete – cancel confirmation dialog
 *   PERF-MYREV-16 : Form is read-only after submission
 *   PERF-MYREV-17 : View Completed review (read-only summary)
 *   PERF-MYREV-18 : Employee views Final Rating after review is Completed
 *   PERF-MYREV-19 : Supervisor evaluation NOT visible until supervisor completes
 */
public class MyReviewPage extends BasePage {

    // ════════════════════════════════════════════════════════════════════════
    // MY REVIEWS LIST PAGE LOCATORS
    // ════════════════════════════════════════════════════════════════════════

    /** Breadcrumb / page title */
    private static final By PAGE_TITLE =
            By.cssSelector(".oxd-topbar-header-breadcrumb .oxd-text");

    /** All review rows in the list table */
    private static final By TABLE_ROWS =
            By.cssSelector(".oxd-table-body .oxd-table-row");

    /** Status badge element in each row */
    private static final By STATUS_BADGES =
            By.cssSelector(".oxd-table-body .oxd-table-row .oxd-chip");

    /** Employee name text in each row (column 1) */
    private static final By ROW_EMPLOYEE_CELLS =
            By.cssSelector(".oxd-table-body .oxd-table-row "
                         + ".oxd-table-cell:nth-child(2) .oxd-text");

    /** "Evaluate" / eye icon button in each row */
    private static final By EVALUATE_BTN =
            By.cssSelector(".oxd-table-body .oxd-icon-button");

    /** Table column headers */
    private static final By TABLE_HEADERS =
            By.cssSelector(".oxd-table-head .oxd-table-header-cell");

    // ════════════════════════════════════════════════════════════════════════
    // SELF-ASSESSMENT FORM LOCATORS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Review Summary section labels (Employee Name, Job Title, Status,
     * Review Period, Due Date shown as read-only text).
     */
    private static final By REVIEW_SUMMARY_SECTION =
            By.cssSelector(".orangehrm-paper-container");

    /** Review Status text inside the summary section */
    private static final By REVIEW_STATUS_TEXT =
            By.xpath("//p[contains(@class,'oxd-text') and "
                   + "(contains(text(),'Activated') or "
                   + " contains(text(),'In Progress') or "
                   + " contains(text(),'Completed'))]");

    /**
     * KPI rating input fields — one per KPI row in the Self Evaluation section.
     * OrangeHRM renders these as number inputs.
     */
    private static final By KPI_RATING_INPUTS =
            By.cssSelector(".oxd-form .oxd-input[type='number'],"
                         + ".oxd-form input[type='number']");

    /**
     * KPI comment textareas — one per KPI row.
     */
    private static final By KPI_COMMENT_AREAS =
            By.cssSelector(".oxd-form textarea");

    /**
     * General comment textarea (last textarea on the form, below KPI rows).
     * OrangeHRM typically renders it after all KPI textareas.
     */
    private static final By GENERAL_COMMENT_AREA =
            By.cssSelector(".orangehrm-self-review-evaluation textarea:last-of-type,"
                         + ".oxd-form-row:last-of-type textarea");

    /** "Save" button inside the self-assessment form */
    private static final By FORM_SAVE_BTN =
            By.cssSelector("button[type='submit']");

    /** "Complete" button inside the self-assessment form */
    private static final By FORM_COMPLETE_BTN =
            By.xpath("//button[normalize-space()='Complete']");

    /** Inline validation error messages on the form */
    private static final By FORM_ERROR_MSGS =
            By.cssSelector(".oxd-input-field-error-message");

    /** "Final Rating" label/value — visible only on Completed reviews */
    private static final By FINAL_RATING_VALUE =
            By.xpath("//p[contains(@class,'oxd-text') and "
                   + "preceding-sibling::*[contains(text(),'Final Rating') "
                   + "or contains(text(),'final')]]");

    /**
     * Supervisor Evaluation section — visible only after supervisor completes.
     * OrangeHRM shows/hides this based on disclosure rule.
     */
    private static final By SUPERVISOR_EVAL_SECTION =
            By.xpath("//h6[contains(text(),'Supervisor') or "
                   + "contains(text(),'Evaluator')]"
                   + "/ancestor::div[contains(@class,'oxd-sheet') "
                   + "   or contains(@class,'orangehrm-paper')]");

    /** "Pending" or hidden indicator for supervisor evaluation */
    private static final By SUPERVISOR_PENDING_INDICATOR =
            By.xpath("//*[contains(text(),'Pending') or "
                   + "    contains(text(),'Not yet') or "
                   + "    contains(text(),'pending')]");

    // ════════════════════════════════════════════════════════════════════════
    // CONFIRMATION DIALOG LOCATORS
    // ════════════════════════════════════════════════════════════════════════

    private static final By DIALOG_OK_BTN =
            By.cssSelector(".orangehrm-modal-footer button.oxd-button--label-danger,"
                         + ".oxd-dialog-container button.oxd-button--label-danger");

    private static final By DIALOG_CANCEL_BTN =
            By.cssSelector(".orangehrm-modal-footer button.oxd-button--ghost,"
                         + ".oxd-dialog-container button.oxd-button--ghost");

    // ════════════════════════════════════════════════════════════════════════
    // TOAST LOCATORS
    // ════════════════════════════════════════════════════════════════════════

    private static final By SUCCESS_TOAST =
            By.cssSelector(".oxd-toast--success .oxd-toast-content-text");

    // ════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════════════

    public MyReviewPage(WebDriver driver) {
        super(driver);
    }

    // ════════════════════════════════════════════════════════════════════════
    // LIST PAGE — PERF-MYREV-1, 2, 3, 4
    // ════════════════════════════════════════════════════════════════════════

    /** Returns the breadcrumb page title text */
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    /** Returns all table column header labels */
    public List<String> getTableColumnHeaders() {
        List<String> labels = new ArrayList<>();
        for (WebElement h : driver.findElements(TABLE_HEADERS)) {
            String t = h.getText().trim();
            if (!t.isEmpty()) labels.add(t);
        }
        return labels;
    }

    /** Returns the number of review rows in the list */
    public int getTableRowCount() {
        return driver.findElements(TABLE_ROWS).size();
    }

    /**
     * PERF-MYREV-3
     * Returns the Employee Name text from every row in the list.
     * Used to verify data isolation — all rows should belong to logged-in user.
     */
    public List<String> getAllRowEmployeeNames() {
        List<String> names = new ArrayList<>();
        for (WebElement cell : driver.findElements(ROW_EMPLOYEE_CELLS)) {
            names.add(cell.getText().trim());
        }
        return names;
    }

    /**
     * PERF-MYREV-3
     * Returns the status badge text for each row.
     */
    public List<String> getAllRowStatuses() {
        List<String> statuses = new ArrayList<>();
        for (WebElement badge : driver.findElements(STATUS_BADGES)) {
            statuses.add(badge.getText().trim());
        }
        return statuses;
    }

    /**
     * PERF-MYREV-4
     * Attempts to navigate to Manage Reviews and returns the final URL.
     * Used to verify employee cannot access the Admin-only screen.
     */
    public String tryNavigateToManageReviews(String baseUrl) {
        driver.get(baseUrl + "/web/index.php/performance/searchPerformanceReview");
        pause(800);
        return driver.getCurrentUrl();
    }

    // ════════════════════════════════════════════════════════════════════════
    // OPEN EVALUATE FORM — PERF-MYREV-5
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the Evaluate (eye) icon on the row at the given index (0-based).
     * Waits for the self-assessment form to load.
     */
    public void openReviewAtIndex(int rowIndex) {
        List<WebElement> btns = driver.findElements(EVALUATE_BTN);
        if (rowIndex >= btns.size())
            throw new IndexOutOfBoundsException(
                    "Evaluate button not found at index: " + rowIndex
                  + ". Available: " + btns.size());
        scrollIntoView(btns.get(rowIndex));
        btns.get(rowIndex).click();
        // wait for the form page to load
        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(FORM_SAVE_BTN),
            ExpectedConditions.visibilityOfElementLocated(FORM_COMPLETE_BTN),
            ExpectedConditions.visibilityOfElementLocated(REVIEW_SUMMARY_SECTION)
        ));
        pause(400);
    }

    /**
     * Finds the first row with the given status and clicks its Evaluate icon.
     * Returns the row index found, or -1 if none.
     */
    public int openFirstReviewWithStatus(String status) {
        List<WebElement> badges = driver.findElements(STATUS_BADGES);
        List<WebElement> btns   = driver.findElements(EVALUATE_BTN);
        for (int i = 0; i < badges.size(); i++) {
            if (badges.get(i).getText().trim().equalsIgnoreCase(status)
                    && i < btns.size()) {
                scrollIntoView(btns.get(i));
                btns.get(i).click();
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(FORM_SAVE_BTN),
                    ExpectedConditions.visibilityOfElementLocated(REVIEW_SUMMARY_SECTION)
                ));
                pause(400);
                return i;
            }
        }
        return -1;
    }

    // ════════════════════════════════════════════════════════════════════════
    // SELF-ASSESSMENT FORM — PERF-MYREV-5 … 19
    // ════════════════════════════════════════════════════════════════════════

    /**
     * PERF-MYREV-5
     * True if the self-assessment form Save button is visible and clickable.
     */
    public boolean isSelfEvalFormOpen() {
        return isDisplayed(FORM_SAVE_BTN) || isDisplayed(FORM_COMPLETE_BTN);
    }

    /**
     * PERF-MYREV-5
     * True if the Complete button is visible (form is editable / not read-only).
     */
    public boolean isCompleteButtonVisible() {
        return isDisplayed(FORM_COMPLETE_BTN);
    }

    /**
     * PERF-MYREV-5
     * True if the Save button is visible on the form.
     */
    public boolean isSaveButtonVisible() {
        return isDisplayed(FORM_SAVE_BTN);
    }

    /**
     * PERF-MYREV-5 / PERF-MYREV-16 / PERF-MYREV-17
     * Returns the Review Status text shown in the Review Summary section.
     */
    public String getReviewStatusOnForm() {
        try {
            return getText(REVIEW_STATUS_TEXT);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns the number of KPI rating inputs in the Self Evaluation section.
     * Used to know how many KPIs are in this review.
     */
    public int getKpiCount() {
        return driver.findElements(KPI_RATING_INPUTS).size();
    }

    /**
     * PERF-MYREV-6 / PERF-MYREV-7 / PERF-MYREV-8 / PERF-MYREV-9 / PERF-MYREV-10
     * Enters a rating value into the KPI input at the given index (0-based).
     * Clears the field first, then types the value.
     */
    public void enterKpiRating(int kpiIndex, String ratingValue) {
        List<WebElement> inputs = waitForKpiInputs();
        if (kpiIndex >= inputs.size())
            throw new IndexOutOfBoundsException(
                    "KPI rating input not found at index: " + kpiIndex
                  + ". KPI count: " + inputs.size());
        WebElement input = inputs.get(kpiIndex);
        scrollIntoView(input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.clear();
        input.sendKeys(ratingValue);
        input.sendKeys(Keys.TAB);
        pause(200);
    }

    /**
     * PERF-MYREV-6 / PERF-MYREV-13
     * Fills ALL KPI rating inputs with the same value.
     * Convenient for happy-path tests where all KPIs need a valid rating.
     */
    public void fillAllKpiRatings(String ratingValue) {
        List<WebElement> inputs = waitForKpiInputs();
        for (int i = 0; i < inputs.size(); i++) {
            WebElement input = inputs.get(i);
            scrollIntoView(input);
            input.click();
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            input.clear();
            input.sendKeys(ratingValue);
            input.sendKeys(Keys.TAB);
            pause(150);
        }
    }

    /**
     * PERF-MYREV-11
     * Enters a comment in the KPI comment textarea at the given index (0-based).
     */
    public void enterKpiComment(int kpiIndex, String comment) {
        List<WebElement> textareas = driver.findElements(KPI_COMMENT_AREAS);
        if (kpiIndex >= textareas.size())
            throw new IndexOutOfBoundsException(
                    "KPI comment textarea not found at index: " + kpiIndex);
        WebElement ta = textareas.get(kpiIndex);
        scrollIntoView(ta);
        ta.clear();
        ta.sendKeys(comment);
    }

    /**
     * Returns the value currently in the KPI comment textarea at the given index.
     * Used to verify comment persists after Save.
     */
    public String getKpiCommentValue(int kpiIndex) {
        List<WebElement> textareas = driver.findElements(KPI_COMMENT_AREAS);
        if (kpiIndex >= textareas.size()) return "";
        return textareas.get(kpiIndex).getAttribute("value");
    }

    /**
     * PERF-MYREV-12
     * Enters text in the General Comment textarea.
     */
    public void enterGeneralComment(String comment) {
        // General comment is the last textarea on the form
        List<WebElement> all = driver.findElements(KPI_COMMENT_AREAS);
        if (all.isEmpty()) return;
        WebElement last = all.get(all.size() - 1);
        scrollIntoView(last);
        last.clear();
        last.sendKeys(comment);
    }

    /**
     * Returns the current value in the General Comment textarea.
     */
    public String getGeneralCommentValue() {
        List<WebElement> all = driver.findElements(KPI_COMMENT_AREAS);
        if (all.isEmpty()) return "";
        return all.get(all.size() - 1).getAttribute("value");
    }

    /**
     * Returns the current value of the KPI rating input at the given index.
     * Used to verify rating persists after Save (re-open form).
     */
    public String getKpiRatingValue(int kpiIndex) {
        List<WebElement> inputs = waitForKpiInputs();
        if (kpiIndex >= inputs.size()) return "";
        return inputs.get(kpiIndex).getAttribute("value");
    }

    /**
     * PERF-MYREV-6 / PERF-MYREV-13
     * Clicks the Save button on the self-assessment form.
     */
    public void clickSave() {
        click(FORM_SAVE_BTN);
        pause(600);
    }

    /**
     * PERF-MYREV-13 / PERF-MYREV-14 / PERF-MYREV-15
     * Clicks the Complete button → triggers confirmation dialog.
     */
    public void clickComplete() {
        click(FORM_COMPLETE_BTN);
        pause(400);
    }

    /**
     * PERF-MYREV-13
     * Confirms the Complete action in the dialog (clicks OK).
     */
    public void confirmComplete() {
        click(DIALOG_OK_BTN);
        pause(700);
    }

    /**
     * PERF-MYREV-15
     * Cancels the Complete dialog (clicks Cancel).
     */
    public void cancelComplete() {
        click(DIALOG_CANCEL_BTN);
        pause(400);
    }

    /**
     * True if the Complete confirmation dialog is visible.
     */
    public boolean isCompleteDialogVisible() {
        return isDisplayed(DIALOG_OK_BTN);
    }

    // ════════════════════════════════════════════════════════════════════════
    // VALIDATION — PERF-MYREV-9, 10, 14
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Returns all inline validation error messages currently visible on the form.
     */
    public List<String> getFormErrors() {
        List<String> errors = new ArrayList<>();
        for (WebElement e : driver.findElements(FORM_ERROR_MSGS)) {
            String t = e.getText().trim();
            if (!t.isEmpty()) errors.add(t);
        }
        return errors;
    }

    /** True if any validation error is visible */
    public boolean hasFormErrors() {
        return !getFormErrors().isEmpty();
    }

    // ════════════════════════════════════════════════════════════════════════
    // READ-ONLY STATE — PERF-MYREV-16, 17
    // ════════════════════════════════════════════════════════════════════════

    /**
     * PERF-MYREV-16 / PERF-MYREV-17
     * True if the form is read-only:
     *   - No Save button visible  AND
     *   - No Complete button visible
     */
    public boolean isFormReadOnly() {
        return !isDisplayed(FORM_SAVE_BTN) && !isDisplayed(FORM_COMPLETE_BTN);
    }

    /**
     * PERF-MYREV-16
     * True if all KPI rating inputs are disabled / read-only (not editable).
     */
    public boolean areKpiInputsReadOnly() {
        List<WebElement> inputs = driver.findElements(KPI_RATING_INPUTS);
        if (inputs.isEmpty()) return true; // no inputs = read-only view
        for (WebElement input : inputs) {
            String readonly  = input.getAttribute("readonly");
            String disabled  = input.getAttribute("disabled");
            if (readonly == null && disabled == null) return false;
        }
        return true;
    }

    // ════════════════════════════════════════════════════════════════════════
    // FINAL RATING & DISCLOSURE — PERF-MYREV-18, 19
    // ════════════════════════════════════════════════════════════════════════

    /**
     * PERF-MYREV-18
     * True if a Final Rating value is visible in the Review Summary section.
     * This appears only after the Supervisor completes the review finalization.
     */
    public boolean isFinalRatingVisible() {
        return isDisplayed(FINAL_RATING_VALUE);
    }

    /**
     * PERF-MYREV-18
     * Returns the Final Rating text value.
     */
    public String getFinalRatingText() {
        try {
            return getText(FINAL_RATING_VALUE);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * PERF-MYREV-19
     * True if the Supervisor Evaluation section is visible (disclosed).
     * Per OrangeHRM mutual disclosure rule: supervisor evaluation is only
     * shown to the employee AFTER the supervisor has completed their evaluation.
     */
    public boolean isSupervisorEvaluationVisible() {
        return isDisplayed(SUPERVISOR_EVAL_SECTION);
    }

    /**
     * PERF-MYREV-19
     * True if a "Pending" / "Not yet submitted" indicator is shown
     * where the supervisor evaluation would be.
     */
    public boolean isSupervisorEvaluationPending() {
        return isDisplayed(SUPERVISOR_PENDING_INDICATOR);
    }

    // ════════════════════════════════════════════════════════════════════════
    // TOAST
    // ════════════════════════════════════════════════════════════════════════

    /** True if success toast is displayed */
    public boolean isSuccessToastDisplayed() {
        try {
            return waitVisible(SUCCESS_TOAST).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns toast message text */
    public String getToastMessage() {
        try {
            return getText(SUCCESS_TOAST);
        } catch (Exception e) {
            return "";
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Waits for KPI rating inputs to be visible and returns them.
     * Throws TimeoutException if no inputs appear within the wait timeout.
     */
    private List<WebElement> waitForKpiInputs() {
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(KPI_RATING_INPUTS));
        return driver.findElements(KPI_RATING_INPUTS);
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
