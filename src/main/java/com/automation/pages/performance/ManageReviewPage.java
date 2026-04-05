package com.automation.pages.performance;

import com.automation.pages.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.ArrayList;

/**
 * ManageReviewPage – Page Object for Performance > Manage Reviews screen.
 *
 * Covers ALL test cases:
 *   PERF-REV-1  : Access to Manage Reviews screen
 *   PERF-REV-2  : UI layout verification
 *   PERF-REV-3  : Filter by Status – Activated
 *   PERF-REV-4  : Filter by Status – In Progress
 *   PERF-REV-5  : Filter by Status – Completed
 *   PERF-REV-6  : Filter by Employee Name
 *   PERF-REV-7  : Filter by date range (From–To)
 *   PERF-REV-8  : Reset after filter
 *   PERF-REV-9  : Verify Supervisor auto-populated when Employee selected
 *   PERF-REV-10 : Add Review – save as Draft (Inactive)
 *   PERF-REV-11 : Add Review – Activate directly
 *   PERF-REV-12 : Activate from Edit (Draft) form
 *   PERF-REV-13 : Add Review – Employee Name empty
 *   PERF-REV-14 : Add Review – Supervisor Reviewer not filled
 *   PERF-REV-15 : Add Review – Review Period Start Date blank
 *   PERF-REV-16 : Add Review – Review Period End Date blank
 *   PERF-REV-17 : Add Review – Due Date blank
 *   PERF-REV-18 : Add Review – End Date before Start Date
 *   PERF-REV-19 : Activate with no KPIs loaded
 *   PERF-REV-20 : Add Review – Cancel
 *   PERF-REV-21 : Edit Inactive review – modify Due Date
 *   PERF-REV-22 : Verify Activated review cannot be edited
 *   PERF-REV-23 : Delete Inactive review
 *   PERF-REV-24 : Verify Activated/Completed review cannot be deleted
 *   PERF-REV-25 : Delete Inactive review – cancel dialog
 *   PERF-REV-26 : Status changes Activated → In Progress
 *   PERF-REV-27 : Status changes In Progress → Completed
 */
public class ManageReviewPage extends BasePage {

    // ════════════════════════════════════════════════════════════════════════
    // LIST PAGE LOCATORS
    // ════════════════════════════════════════════════════════════════════════

    /** Page breadcrumb title */
    private static final By PAGE_TITLE =
            By.cssSelector(".oxd-topbar-header-breadcrumb .oxd-text");

    /** Employee Name autocomplete filter */
    private static final By FILTER_EMPLOYEE_INPUT =
            By.cssSelector(".oxd-form .oxd-autocomplete-text-input input");

    /** Review Status dropdown filter */
    private static final By FILTER_STATUS_DROPDOWN =
            By.cssSelector(".oxd-form .oxd-select-text");

    /** From Date input */
    private static final By FILTER_FROM_DATE =
            By.xpath("(//div[contains(@class,'oxd-date-input')]//input)[1]");

    /** To Date input */
    private static final By FILTER_TO_DATE =
            By.xpath("(//div[contains(@class,'oxd-date-input')]//input)[2]");

    /** Search button */
    private static final By SEARCH_BTN =
            By.cssSelector("button[type='submit']");

    /** Reset button */
    private static final By RESET_BTN =
            By.cssSelector("button[type='reset']");

    /** Add button */
    private static final By ADD_BTN =
            By.xpath("//button[normalize-space()='Add']");

    /** Table rows in body */
    private static final By TABLE_ROWS =
            By.cssSelector(".oxd-table-body .oxd-table-row");

    /** Status badge cells (column 6 in the reviews table) */
    private static final By STATUS_CELLS =
            By.cssSelector(".oxd-table-body .oxd-table-row "
                         + ".oxd-table-cell:nth-child(6) .oxd-chip");

    /** Edit icons in table rows */
    private static final By ROW_EDIT_ICONS =
            By.cssSelector(".oxd-table-body .oxd-icon-button i.bi-pencil-fill");

    /** Delete icons in table rows */
    private static final By ROW_DELETE_ICONS =
            By.cssSelector(".oxd-table-body .oxd-icon-button i.bi-trash");

    /** Evaluate / view icon buttons */
    private static final By ROW_EVALUATE_ICONS =
            By.cssSelector(".oxd-table-body .oxd-icon-button i.bi-eye-fill,"
                         + ".oxd-table-body .oxd-icon-button i.bi-check-circle");

    /** Table column header cells */
    private static final By TABLE_HEADERS =
            By.cssSelector(".oxd-table-head .oxd-table-header-cell");

    /** No records found message */
    private static final By NO_RECORDS_MSG =
            By.cssSelector(".oxd-table-card p");

    /** Dropdown options (Status filter or any open dropdown) */
    private static final By DROPDOWN_OPTIONS =
            By.cssSelector(".oxd-select-dropdown .oxd-select-option,"
                         + ".oxd-select-option");

    /** Autocomplete suggestion list */
    private static final By AUTOCOMPLETE_OPTIONS =
            By.cssSelector(".oxd-autocomplete-dropdown .oxd-autocomplete-option");

    // ════════════════════════════════════════════════════════════════════════
    // ADD / EDIT FORM LOCATORS
    // ════════════════════════════════════════════════════════════════════════

    /** Employee Name autocomplete in Add/Edit form */
    private static final By FORM_EMPLOYEE_INPUT =
            By.cssSelector(".oxd-form .oxd-autocomplete-text-input input");

    /** Supervisor Reviewer field (read-only text or autocomplete) */
    private static final By FORM_SUPERVISOR_INPUT =
            By.xpath("//label[contains(text(),'Supervisor')]"
                   + "/ancestor::div[contains(@class,'oxd-input-group')]"
                   + "//input");

    /** Review Period Start Date */
    private static final By FORM_START_DATE =
            By.xpath("(//label[contains(text(),'Review Period')]"
                   + "/ancestor::div[contains(@class,'oxd-form-row')]"
                   + "//input)[1]");

    /** Review Period End Date */
    private static final By FORM_END_DATE =
            By.xpath("(//label[contains(text(),'Review Period')]"
                   + "/ancestor::div[contains(@class,'oxd-form-row')]"
                   + "//input)[2]");

    /** Due Date input */
    private static final By FORM_DUE_DATE =
            By.xpath("//label[contains(text(),'Due Date')]"
                   + "/ancestor::div[contains(@class,'oxd-input-group')]"
                   + "//input");

    /** Save (Draft) button */
    private static final By FORM_SAVE_BTN =
            By.cssSelector(".oxd-form button[type='submit']");

    /** Activate button */
    private static final By FORM_ACTIVATE_BTN =
            By.xpath("//button[normalize-space()='Activate']");

    /** Cancel button */
    private static final By FORM_CANCEL_BTN =
            By.xpath("//button[normalize-space()='Cancel']");

    /** All inline validation error messages */
    private static final By FORM_ERRORS =
            By.cssSelector(".oxd-input-field-error-message,"
                         + ".oxd-form-row .oxd-text--span.oxd-input-field-error-message");

    // ════════════════════════════════════════════════════════════════════════
    // CONFIRMATION DIALOG LOCATORS
    // ════════════════════════════════════════════════════════════════════════

    private static final By DIALOG_CONFIRM_BTN =
            By.cssSelector(".orangehrm-modal-footer button.oxd-button--label-danger");

    private static final By DIALOG_CANCEL_BTN =
            By.cssSelector(".orangehrm-modal-footer button.oxd-button--ghost");

    private static final By DIALOG_CONTAINER =
            By.cssSelector(".orangehrm-dialog-popup, .oxd-dialog-container");

    // ════════════════════════════════════════════════════════════════════════
    // TOAST LOCATORS
    // ════════════════════════════════════════════════════════════════════════

    private static final By SUCCESS_TOAST =
            By.cssSelector(".oxd-toast--success .oxd-toast-content-text");

    private static final By ANY_TOAST =
            By.cssSelector(".oxd-toast");

    // ════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════════════

    public ManageReviewPage(WebDriver driver) {
        super(driver);
    }

    // ════════════════════════════════════════════════════════════════════════
    // LIST PAGE – VERIFICATION METHODS (PERF-REV-1, 2)
    // ════════════════════════════════════════════════════════════════════════

    /** Returns breadcrumb title text */
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    /** True if "Add" button is visible */
    public boolean isAddButtonVisible() {
        return isDisplayed(ADD_BTN);
    }

    /** True if "Search" button is visible */
    public boolean isSearchButtonVisible() {
        return isDisplayed(SEARCH_BTN);
    }

    /** True if "Reset" button is visible */
    public boolean isResetButtonVisible() {
        return isDisplayed(RESET_BTN);
    }

    /** True if Employee Name filter input is visible */
    public boolean isEmployeeFilterVisible() {
        return isDisplayed(FILTER_EMPLOYEE_INPUT);
    }

    /** True if Review Status dropdown filter is visible */
    public boolean isStatusFilterVisible() {
        return isDisplayed(FILTER_STATUS_DROPDOWN);
    }

    /** True if From Date filter is visible */
    public boolean isFromDateFilterVisible() {
        return isDisplayed(FILTER_FROM_DATE);
    }

    /** True if To Date filter is visible */
    public boolean isToDateFilterVisible() {
        return isDisplayed(FILTER_TO_DATE);
    }

    /** Returns all column header labels from the review table */
    public List<String> getTableColumnHeaders() {
        List<String> labels = new ArrayList<>();
        for (WebElement h : driver.findElements(TABLE_HEADERS)) {
            String t = h.getText().trim();
            if (!t.isEmpty()) labels.add(t);
        }
        return labels;
    }

    // ════════════════════════════════════════════════════════════════════════
    // LIST PAGE – FILTER ACTIONS (PERF-REV-3 … 8)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Types an employee name into the filter autocomplete and
     * selects the first matching suggestion.
     */
    public void filterByEmployeeName(String employeeName) {
        WebElement input = waitVisible(FILTER_EMPLOYEE_INPUT);
        input.clear();
        input.sendKeys(employeeName);
        waitVisible(AUTOCOMPLETE_OPTIONS);
        List<WebElement> opts = driver.findElements(AUTOCOMPLETE_OPTIONS);
        if (!opts.isEmpty()) opts.get(0).click();
    }

    /**
     * Selects a Review Status from the filter dropdown.
     * Valid values: "Activated", "In Progress", "Completed"
     */
    public void filterByStatus(String status) {
        click(FILTER_STATUS_DROPDOWN);
        wait.until(ExpectedConditions.visibilityOfElementLocated(DROPDOWN_OPTIONS));
        for (WebElement opt : driver.findElements(DROPDOWN_OPTIONS)) {
            if (opt.getText().trim().equalsIgnoreCase(status)) {
                opt.click();
                return;
            }
        }
        throw new NoSuchElementException("Status option not found: " + status);
    }

    /**
     * Sets the From Date filter. Format: yyyy-mm-dd
     */
    public void setFromDate(String date) {
        WebElement input = waitVisible(FILTER_FROM_DATE);
        input.clear();
        input.sendKeys(date);
        input.sendKeys(Keys.TAB);
    }

    /**
     * Sets the To Date filter. Format: yyyy-mm-dd
     */
    public void setToDate(String date) {
        WebElement input = waitVisible(FILTER_TO_DATE);
        input.clear();
        input.sendKeys(date);
        input.sendKeys(Keys.TAB);
    }

    /** Clicks Search button */
    public void clickSearch() {
        click(SEARCH_BTN);
        waitForTableLoad();
    }

    /** Clicks Reset button */
    public void clickReset() {
        click(RESET_BTN);
        waitForTableLoad();
    }

    /** Returns number of rows in the table */
    public int getTableRowCount() {
        return driver.findElements(TABLE_ROWS).size();
    }

    /** True if "No Records Found" empty state is shown */
    public boolean isNoRecordsFound() {
        List<WebElement> rows = driver.findElements(TABLE_ROWS);
        if (rows.isEmpty()) return true;
        try {
            WebElement msg = driver.findElement(NO_RECORDS_MSG);
            return msg.getText().contains("No Records Found");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the status text of each row currently in the table.
     * Used to verify filter results show only the expected status.
     */
    public List<String> getAllRowStatuses() {
        List<String> statuses = new ArrayList<>();
        for (WebElement badge : driver.findElements(STATUS_CELLS)) {
            statuses.add(badge.getText().trim());
        }
        return statuses;
    }

    /**
     * Returns the current value shown in the Status filter dropdown.
     */
    public String getStatusFilterValue() {
        return getText(FILTER_STATUS_DROPDOWN);
    }

    /**
     * Returns current value in Employee filter input.
     */
    public String getEmployeeFilterValue() {
        return waitVisible(FILTER_EMPLOYEE_INPUT).getAttribute("value");
    }

    // ════════════════════════════════════════════════════════════════════════
    // ADD FORM – NAVIGATION (PERF-REV-10 … 20)
    // ════════════════════════════════════════════════════════════════════════

    /** Clicks Add button → waits for form to appear */
    public void clickAdd() {
        click(ADD_BTN);
        waitVisible(FORM_EMPLOYEE_INPUT);
    }

    // ════════════════════════════════════════════════════════════════════════
    // ADD FORM – FIELD ACTIONS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Types employee name in the autocomplete and selects first suggestion.
     * Pass null to leave blank (simulate empty input).
     */
    public void enterEmployeeName(String name) {
        WebElement input = waitVisible(FORM_EMPLOYEE_INPUT);
        input.clear();
        if (name == null || name.isEmpty()) return;
        input.sendKeys(name);
        try {
            waitVisible(AUTOCOMPLETE_OPTIONS);
            List<WebElement> opts = driver.findElements(AUTOCOMPLETE_OPTIONS);
            if (!opts.isEmpty()) opts.get(0).click();
        } catch (Exception e) {
            // No autocomplete suggestion – leave as typed
        }
    }

    /**
     * Returns the auto-populated Supervisor Reviewer field value.
     * Used by PERF-REV-9 to verify auto-population.
     */
    public String getSupervisorFieldValue() {
        try {
            WebElement el = waitVisible(FORM_SUPERVISOR_INPUT);
            String val = el.getAttribute("value");
            return val != null ? val.trim() : "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * True if Supervisor field is NOT empty (auto-populated).
     */
    public boolean isSupervisorAutoPopulated() {
        return !getSupervisorFieldValue().isEmpty();
    }

    /**
     * Manually fills the Supervisor Reviewer field.
     * Pass null to leave blank.
     */
    public void enterSupervisor(String supervisorName) {
        if (supervisorName == null || supervisorName.isEmpty()) return;
        WebElement input = waitVisible(FORM_SUPERVISOR_INPUT);
        input.clear();
        input.sendKeys(supervisorName);
        try {
            waitVisible(AUTOCOMPLETE_OPTIONS);
            List<WebElement> opts = driver.findElements(AUTOCOMPLETE_OPTIONS);
            if (!opts.isEmpty()) opts.get(0).click();
        } catch (Exception ignored) {}
    }

    /**
     * Sets Review Period Start Date. Format: yyyy-mm-dd
     */
    public void enterStartDate(String date) {
        setDateField(FORM_START_DATE, date);
    }

    /**
     * Sets Review Period End Date. Format: yyyy-mm-dd
     */
    public void enterEndDate(String date) {
        setDateField(FORM_END_DATE, date);
    }

    /**
     * Sets Due Date. Format: yyyy-mm-dd
     */
    public void enterDueDate(String date) {
        setDateField(FORM_DUE_DATE, date);
    }

    /** Clicks Save (saves as Draft → Inactive status) */
    public void clickSave() {
        click(FORM_SAVE_BTN);
        pause(600);
    }

    /** Clicks Activate button */
    public void clickActivate() {
        click(FORM_ACTIVATE_BTN);
        pause(800);
    }

    /** Clicks Cancel – discards form */
    public void clickCancel() {
        click(FORM_CANCEL_BTN);
        waitForTableLoad();
    }

    // ════════════════════════════════════════════════════════════════════════
    // ADD FORM – VALIDATION (PERF-REV-13 … 19)
    // ════════════════════════════════════════════════════════════════════════

    /** Returns all inline validation error messages */
    public List<String> getFormErrors() {
        List<String> errors = new ArrayList<>();
        for (WebElement e : driver.findElements(FORM_ERRORS)) {
            String t = e.getText().trim();
            if (!t.isEmpty()) errors.add(t);
        }
        return errors;
    }

    /** True if any form validation error is visible */
    public boolean hasFormErrors() {
        return !getFormErrors().isEmpty();
    }

    /** True if the Add/Edit form is still open (Save/Activate was blocked) */
    public boolean isFormStillOpen() {
        return isDisplayed(FORM_SAVE_BTN) || isDisplayed(FORM_ACTIVATE_BTN);
    }

    // ════════════════════════════════════════════════════════════════════════
    // COMPOSITE HELPERS – Add Review flows
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Fills all Add Review fields.
     * Pass null for any field to leave it blank (validation test scenarios).
     */
    public void fillReviewForm(String employeeName,
                                String startDate,
                                String endDate,
                                String dueDate) {
        enterEmployeeName(employeeName);
        pause(500); // allow supervisor to auto-populate
        enterStartDate(startDate);
        enterEndDate(endDate);
        enterDueDate(dueDate);
    }

    /**
     * PERF-REV-10: Full flow – Add review and Save as Draft.
     */
    public void addReviewAsDraft(String employeeName,
                                  String startDate,
                                  String endDate,
                                  String dueDate) {
        clickAdd();
        fillReviewForm(employeeName, startDate, endDate, dueDate);
        clickSave();
    }

    /**
     * PERF-REV-11: Full flow – Add review and Activate directly.
     */
    public void addReviewAndActivate(String employeeName,
                                      String startDate,
                                      String endDate,
                                      String dueDate) {
        clickAdd();
        fillReviewForm(employeeName, startDate, endDate, dueDate);
        clickActivate();
    }

    // ════════════════════════════════════════════════════════════════════════
    // EDIT ACTIONS (PERF-REV-21, 22)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the Edit (pencil) icon on the row at the given index (0-based).
     * Waits for form to load.
     */
    public void clickEditOnRow(int rowIndex) {
        List<WebElement> editBtns = getActionButtons("edit");
        if (rowIndex >= editBtns.size())
            throw new IndexOutOfBoundsException(
                    "Edit button not found at index: " + rowIndex
                  + ". Available: " + editBtns.size());
        scrollIntoView(editBtns.get(rowIndex));
        editBtns.get(rowIndex).click();
        waitVisible(FORM_SAVE_BTN);
    }

    /**
     * Returns true if the Edit icon exists for the row at the given index.
     * Used by PERF-REV-22 to verify Activated review cannot be edited.
     */
    public boolean isEditIconPresentOnRow(int rowIndex) {
        List<WebElement> editBtns = getActionButtons("edit");
        return rowIndex < editBtns.size();
    }

    // ════════════════════════════════════════════════════════════════════════
    // DELETE ACTIONS (PERF-REV-23, 24, 25)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the Delete (trash) icon on the row at the given index (0-based).
     * Waits for confirmation dialog.
     */
    public void clickDeleteOnRow(int rowIndex) {
        List<WebElement> delBtns = getActionButtons("delete");
        if (rowIndex >= delBtns.size())
            throw new IndexOutOfBoundsException(
                    "Delete button not found at index: " + rowIndex
                  + ". Available: " + delBtns.size());
        scrollIntoView(delBtns.get(rowIndex));
        delBtns.get(rowIndex).click();
        waitVisible(DIALOG_CONFIRM_BTN);
    }

    /**
     * Returns true if the Delete icon exists for the row at the given index.
     * Used by PERF-REV-24 to verify Activated/Completed reviews cannot be deleted.
     */
    public boolean isDeleteIconPresentOnRow(int rowIndex) {
        List<WebElement> delBtns = getActionButtons("delete");
        return rowIndex < delBtns.size();
    }

    /** Confirms delete in dialog (clicks Yes/OK) */
    public void confirmDelete() {
        click(DIALOG_CONFIRM_BTN);
        pause(700);
    }

    /** Cancels the delete dialog */
    public void cancelDelete() {
        click(DIALOG_CANCEL_BTN);
        pause(400);
    }

    /** True if delete confirmation dialog is visible */
    public boolean isDeleteDialogVisible() {
        return isDisplayed(DIALOG_CONFIRM_BTN);
    }

    // ════════════════════════════════════════════════════════════════════════
    // STATUS VERIFICATION (PERF-REV-26, 27)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Returns the status text of the row at the given index (0-based).
     * Used by PERF-REV-26 and PERF-REV-27 to verify status transitions.
     */
    public String getStatusOfRow(int rowIndex) {
        List<WebElement> badges = driver.findElements(STATUS_CELLS);
        if (rowIndex >= badges.size())
            throw new IndexOutOfBoundsException(
                    "Status badge not found at row index: " + rowIndex);
        return badges.get(rowIndex).getText().trim();
    }

    /**
     * Returns the index of the first row matching the given status.
     * Returns -1 if not found.
     */
    public int findRowIndexByStatus(String status) {
        List<WebElement> badges = driver.findElements(STATUS_CELLS);
        for (int i = 0; i < badges.size(); i++) {
            if (badges.get(i).getText().trim().equalsIgnoreCase(status)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Clicks the Evaluate icon on the row at the given index.
     * Used by PERF-REV-27 to open the review finalization form.
     */
    public void clickEvaluateOnRow(int rowIndex) {
        List<WebElement> evBtns = getActionButtons("evaluate");
        if (rowIndex >= evBtns.size())
            throw new IndexOutOfBoundsException(
                    "Evaluate button not found at index: " + rowIndex);
        scrollIntoView(evBtns.get(rowIndex));
        evBtns.get(rowIndex).click();
        pause(800);
    }

    // ════════════════════════════════════════════════════════════════════════
    // TOAST VERIFICATION
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
     * Returns action button elements by type.
     * OrangeHRM renders icon buttons; we identify by icon class.
     * type: "edit" | "delete" | "evaluate"
     */
    private List<WebElement> getActionButtons(String type) {
        String iconClass = switch (type) {
            case "edit"     -> "bi-pencil-fill";
            case "delete"   -> "bi-trash";
            case "evaluate" -> "bi-eye-fill";
            default         -> throw new IllegalArgumentException(
                    "Unknown button type: " + type);
        };
        // Each icon is inside an oxd-icon-button; click the button, not the icon
        List<WebElement> buttons = new ArrayList<>();
        String btnSelector =
                ".oxd-table-body .oxd-icon-button i." + iconClass;
        for (WebElement icon : driver.findElements(By.cssSelector(btnSelector))) {
            try {
                WebElement btn = icon.findElement(
                        By.xpath("./ancestor::button"));
                buttons.add(btn);
            } catch (Exception e) {
                buttons.add(icon); // fallback: click icon directly
            }
        }
        return buttons;
    }

    /** Sets a date input field using clear + sendKeys + TAB */
    private void setDateField(By locator, String date) {
        if (date == null) return;
        WebElement input = waitVisible(locator);
        input.clear();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(date);
        input.sendKeys(Keys.TAB);
        pause(200);
    }

    /** Waits for the table to finish loading after Search/Reset */
    private void waitForTableLoad() {
        pause(600);
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
