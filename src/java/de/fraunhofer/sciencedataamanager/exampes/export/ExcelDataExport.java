/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.exampes.export;

import de.fraunhofer.sciencedataamanager.interfaces.IExportScientificPaperMetaInformation;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Moritz Mars
 */
public class ExcelDataExport implements IExportScientificPaperMetaInformation {

    @Override
    public void export(Map<String, List<Object>> dataToExport, OutputStream outputStream) throws Exception {
        Workbook currentWorkBook = new HSSFWorkbook();
        int currenSheetCount = 0;

        List<String> columns = new ArrayList<String>(dataToExport.keySet());
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int size = dataToExport.values().iterator().next().size();

        for (int i = 0; i < size; i++) {
            Map<String, Object> row = new HashMap<String, Object>();

            for (String column : columns) {
                row.put(column, dataToExport.get(column).get(i));
            }

            rows.add(row);
        }

        //for (SearchDefinitonExecution searchDefinitonExecution : searchDefinitonExecutionList) {
        Sheet currentSheet = currentWorkBook.createSheet();
        currentSheet.setFitToPage(true);
        currentSheet.setHorizontallyCenter(true);
        currentSheet.createFreezePane(0, 1);
        currentWorkBook.setSheetName(currenSheetCount, "Test");

        Row headerRow = currentSheet.createRow(0);
        headerRow.setHeightInPoints(12.75f);
        int headerColumnIndex = 0;
        for (String currentColumn : columns) {
            headerRow.createCell(headerColumnIndex).setCellValue(currentColumn);
            headerColumnIndex++;
        }
        CellStyle style = currentWorkBook.createCellStyle();
        Font headerFont = currentWorkBook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);

        headerRow.setRowStyle(style);

        Row currentRow = null;
        int rowNum = 1;
        int currentColum = 0;
        
        for (Map<String, Object> currentRow2 : rows) {
            currentRow = currentSheet.createRow(rowNum);
            for (String column : columns) {
                if(currentRow2.get(column)!=null)
                currentRow.createCell(currentColum).setCellValue(currentRow2.get(column).toString());
                currentColum++;
                
            }
            currentColum=0;
            rowNum++;
        }
        currenSheetCount++;
        //}
        currentWorkBook.write(outputStream);

        outputStream.close();

    }

}
