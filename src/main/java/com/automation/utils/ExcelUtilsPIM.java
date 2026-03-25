package com.automation.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ExcelUtilsPIM {
    private static Workbook workbook;
    private static final DataFormatter FORMATTER = new DataFormatter();

    static {
        try {
            FileInputStream fis =
                    new FileInputStream("src/test/resources/testdata/PIMData.xlsx");
            workbook = new XSSFWorkbook(fis);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load Excel file", e);
        }
    }

    public static Object[][] getDataProvider(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);

        int rowCount = sheet.getPhysicalNumberOfRows();
        int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

        Object[][] data = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    data[i - 1][j] = "";
                } else {
                    switch (cell.getCellType()) {

                        case STRING:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;

                        case NUMERIC:
                            data[i - 1][j] = (int) cell.getNumericCellValue();
                            break;

                        case BOOLEAN:
                            data[i - 1][j] = cell.getBooleanCellValue();
                            break;

                        default:
                            data[i - 1][j] = FORMATTER.formatCellValue(cell);
                    }
                }
            }
        }

        return data;
    }
}
