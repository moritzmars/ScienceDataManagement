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
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.charts.*;

/**
 *
 * @author Moritz Mars
 */
public class ExcelDataExportIncChart implements IExportScientificPaperMetaInformation {

    /**
     *
     * @param dataToExport The objects gets all the values, which should
     * exported.
     * @param outputStream
     * @throws Exception
     */
    @Override
    public void export(Map<String, Map<String, List<Object>>> allConnectorsToExport, OutputStream outputStream) throws Exception {
        XSSFWorkbook currentWorkBook = new XSSFWorkbook();
        int currenSheetCount = 0;

        for (String currentKey : allConnectorsToExport.keySet())
        {
            Map<String, List<Object>> dataToExport = allConnectorsToExport.get(currentKey);

            List<String> columns = new ArrayList<String>(dataToExport.keySet());
            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
            int size = dataToExport.values().iterator().next().size();

            for (int i = 0; i < size; i++)
            {
                Map<String, Object> row = new HashMap<String, Object>();

                for (String column : columns)
                {
                    row.put(column, dataToExport.get(column).get(i));
                }

                rows.add(row);
            }

            //for (SearchDefinitonExecution searchDefinitonExecution : searchDefinitonExecutionList) {
            XSSFSheet currentSheet = currentWorkBook.createSheet();
            currentSheet.setFitToPage(true);
            currentSheet.setHorizontallyCenter(true);
            currentSheet.createFreezePane(0, 1);
            currentWorkBook.setSheetName(currenSheetCount, currentKey);

            XSSFRow headerRow = currentSheet.createRow(0);
            headerRow.setHeightInPoints(12.75f);
            int headerColumnIndex = 0;
            for (String currentColumn : columns)
            {
                headerRow.createCell(headerColumnIndex).setCellValue(currentColumn);
                headerColumnIndex++;
            }
            XSSFCellStyle style = currentWorkBook.createCellStyle();
            XSSFFont headerFont = currentWorkBook.createFont();
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

           XSSFRow currentRow = null;
            int rowNum = 1;
            int currentColum = 0;

            for (Map<String, Object> currentRow2 : rows)
            {
                currentRow = currentSheet.createRow(rowNum);
                for (String column : columns)
                {
                    if (currentRow2.get(column) != null)
                    {
                        currentRow.createCell(currentColum).setCellValue(currentRow2.get(column).toString());
                    }
                    currentColum++;

                }
                currentColum = 0;
                rowNum++;
            }

            /* At the end of this step, we have a worksheet with test data, that we want to write into a chart */
            /* Create a drawing canvas on the worksheet */
           
            XSSFDrawing xlsx_drawing = currentSheet.createDrawingPatriarch();
            /* Define anchor points in the worksheet to position the chart */
            XSSFClientAnchor anchor = xlsx_drawing.createAnchor(0, 0, 0, 0, 0, 5, 10, 15);
            /* Create the chart object based on the anchor point */
            XSSFChart my_line_chart = xlsx_drawing.createChart(anchor);
            /* Define legends for the line chart and set the position of the legend */
            XSSFChartLegend legend = my_line_chart.getOrCreateLegend();
            legend.setPosition(LegendPosition.BOTTOM);
            /* Create data for the chart */
            LineChartData data = my_line_chart.getChartDataFactory().createLineChartData();
            /* Define chart AXIS */
            ChartAxis bottomAxis = my_line_chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
            ValueAxis leftAxis = my_line_chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            /* Define Data sources for the chart */
            /* Set the right cell range that contain values for the chart */
            /* Pass the worksheet and cell range address as inputs */
            /* Cell Range Address is defined as First row, last row, first column, last column */
            ChartDataSource<Number> xs = DataSources.fromNumericCellRange(currentSheet, new CellRangeAddress(0, 0, 0, 4));
            ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(currentSheet, new CellRangeAddress(1, 1, 0, 4));
            ChartDataSource<Number> ys2 = DataSources.fromNumericCellRange(currentSheet, new CellRangeAddress(2, 2, 0, 4));
            ChartDataSource<Number> ys3 = DataSources.fromNumericCellRange(currentSheet, new CellRangeAddress(3, 3, 0, 4));
            /* Add chart data sources as data to the chart */
            
            data.addSeries(xs, ys1);
            data.addSeries(xs, ys2);
            data.addSeries(xs, ys3);
            /* Plot the chart with the inputs from data and chart axis */
            my_line_chart.plot(data, new ChartAxis[]
            {
                bottomAxis, leftAxis
            });

            currenSheetCount++;
        }

        currentWorkBook.write(outputStream);

        outputStream.close();

    }

}
