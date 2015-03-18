/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchExecution;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformation;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

/**
 *
 * @author Moritz Mars
 */
public class SearchDefinitonExecutionDataManager {

    private ApplicationConfiguration applicationConfiguration;

    /**
     *
     * @param applicationConfiguration
     */
    public SearchDefinitonExecutionDataManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    /**
     *
     * @param searchDefinition
     * @param systemInstance
     * @return
     * @throws Exception
     */
    public int getLastSearchDefinitonExecutionIDForSystemInstance(SearchDefinition searchDefinition, SystemInstance systemInstance) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String query = "SELECT MAX(ID) lastID FROM search_definiton_execution where (Search_Definiton_ID=" + searchDefinition.getID() + " and System_Instance_ID=" + systemInstance.getID() + ")";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        rs.next();
        int lastSearchDefinitionID = rs.getInt("lastID");

        st.close();
        rs.close();
        conn.close();
        return lastSearchDefinitionID;
    }

    /**
     *
     * @param searchDefinition
     * @return
     * @throws Exception
     */
    public SearchDefinitonExecution getLastSearchDefinitionExecutionForSearchDefinition(SearchDefinition searchDefinition) throws Exception {
        SearchDefinitonExecution searchDefinitionExecution = new SearchDefinitonExecution();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String query = "SELECT MAX(ID) lastID FROM search_definiton_execution where Search_Definiton_ID=" + searchDefinition.getID();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        rs.next();
        searchDefinitionExecution.setID(rs.getInt("lastID"));

        st.close();
        rs.close();

        String query2 = "SELECT * FROM search_definiton_execution where ID=" + searchDefinitionExecution.getID();

        Statement st2 = conn.createStatement();
        ResultSet rs2 = st2.executeQuery(query2);
        while (rs2.next()) {

            searchDefinitionExecution.setTotalItems(rs2.getInt("TotalItems"));
            searchDefinitionExecution.setUpdatedItems(rs2.getInt("UpdatedItems"));
            searchDefinitionExecution.setSearch_Definiton_ID(rs2.getInt("Search_Definiton_ID"));
            searchDefinitionExecution.setRequestUrl(rs2.getString("RequestUrl"));
            searchDefinitionExecution.setQuery(rs2.getString("Query"));
            searchDefinitionExecution.setCrawledItems(rs2.getInt("CrawledItems"));
            searchDefinitionExecution.setStartExecutionTimestamp(rs2.getTimestamp("StartExecutionTimestamp"));
            searchDefinitionExecution.setFinishedExecutionTimestamp(rs2.getTimestamp("FinishedExecutionTimestamp"));
            searchDefinitionExecution.setSearchState(rs2.getString("SearchExecutionState"));
            searchDefinitionExecution.setMessage(rs2.getString("Message"));
            searchDefinitionExecution.setLocalItemsFound(rs2.getInt("LocalItemsFound"));

            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            searchDefinitionExecution.setSystemInstance(systemInstanceDataProvider.getSystemInstanceByID(rs2.getInt("System_Instance_ID")));
        }

        st2.close();
        rs2.close();
        conn.close();
        ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataProvider = new ScientificPaperMetaInformationDataManager(applicationConfiguration);
        searchDefinitionExecution.setScientificPaperMetaInformation(scientificPaperMetaInformationDataProvider.getScientificMetaInformationBySearchDefinition(searchDefinitionExecution));

        return searchDefinitionExecution;

    }

    /**
     *
     * @param searchDefinitonExecution
     * @throws Exception
     */
    public void saveCloudPapers(SearchDefinitonExecution searchDefinitonExecution) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String sqlInsertStatement = "INSERT INTO  search_definiton_execution (TotalItems, UpdatedItems, NewItems, Search_Definiton_ID, RequestUrl, Query, CrawledItems, System_Instance_ID, StartExecutionTimestamp, FinishedExecutionTimestamp, SearchExecutionState, Message, LocalItemsFound, search_definition_execution_run_ID)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setInt(1, searchDefinitonExecution.getTotalItems());
        preparedStatement.setInt(2, searchDefinitonExecution.getUpdatedItems());
        preparedStatement.setInt(3, searchDefinitonExecution.getNewItems());
        preparedStatement.setInt(4, searchDefinitonExecution.getSearch_Definiton_ID());
        preparedStatement.setString(5, searchDefinitonExecution.getRequestUrl());
        preparedStatement.setString(6, searchDefinitonExecution.getQuery());
        preparedStatement.setInt(7, searchDefinitonExecution.getCrawledItems());
        preparedStatement.setInt(8, searchDefinitonExecution.getSystemInstance().getID());
        preparedStatement.setTimestamp(9, searchDefinitonExecution.getStartExecutionTimestamp());
        preparedStatement.setTimestamp(10, searchDefinitonExecution.getFinishedExecutionTimestamp());
        preparedStatement.setString(11, searchDefinitonExecution.getSearchState());
        preparedStatement.setString(12, searchDefinitonExecution.getMessage());
        preparedStatement.setInt(13, searchDefinitonExecution.getLocalItemsFound());
        preparedStatement.setInt(14, searchDefinitonExecution.getSearch_Definition_Execution_Run_ID());
        preparedStatement.execute();
        preparedStatement.close();

        String query = "SELECT MAX(ID) lastID FROM search_definiton_execution";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        rs.next();
        searchDefinitonExecution.setID(rs.getInt("lastID"));
        st.close();
        rs.close();
        conn.close();
        ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataProvider = new ScientificPaperMetaInformationDataManager(applicationConfiguration);
        scientificPaperMetaInformationDataProvider.insertScientificPaperMetaInformation(searchDefinitonExecution);

    }

    /**
     *
     * @param searchDefinition
     * @return
     * @throws Exception
     */
    public LinkedList<SearchDefinitonExecution> getSearchDefinitionExecutionForSearchDefinition(SearchDefinition searchDefinition) throws Exception {
        LinkedList<SearchDefinitonExecution> searchDefinitionExecutionList = new LinkedList<SearchDefinitonExecution>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn2 = null;
        conn2 = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
        SearchExecution searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);
        for (SystemInstance currentSystemInstance : searchExecution.getSystemInstances()) {

            int lastID = getLastSearchDefinitonExecutionIDForSystemInstance(searchDefinition, currentSystemInstance);

            String query2 = "SELECT * FROM search_definiton_execution where ID=" + lastID;
            Statement st2 = conn2.createStatement();
            ResultSet rs2 = st2.executeQuery(query2);

            while (rs2.next()) {
                SearchDefinitonExecution searchDefinitonExecution = new SearchDefinitonExecution();

                searchDefinitonExecution.setID(rs2.getInt("ID"));
                searchDefinitonExecution.setTotalItems(rs2.getInt("TotalItems"));
                searchDefinitonExecution.setUpdatedItems(rs2.getInt("UpdatedItems"));
                searchDefinitonExecution.setNewItems(rs2.getInt("NewItems"));
                searchDefinitonExecution.setSearch_Definiton_ID(rs2.getInt("Search_Definiton_ID"));
                searchDefinitonExecution.setRequestUrl(rs2.getString("RequestUrl"));
                searchDefinitonExecution.setQuery(rs2.getString("Query"));
                searchDefinitonExecution.setCrawledItems(rs2.getInt("CrawledItems"));
                searchDefinitonExecution.setStartExecutionTimestamp(rs2.getTimestamp("StartExecutionTimestamp"));
                searchDefinitonExecution.setFinishedExecutionTimestamp(rs2.getTimestamp("FinishedExecutionTimestamp"));
                searchDefinitonExecution.setSearchState(rs2.getString("SearchExecutionState"));
                searchDefinitonExecution.setMessage(rs2.getString("Message"));
                searchDefinitonExecution.setLocalItemsFound(rs2.getInt("LocalItemsFound"));

                SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
                searchDefinitonExecution.setSystemInstance(systemInstanceDataProvider.getSystemInstanceByID(rs2.getInt("System_Instance_ID")));

                searchDefinitionExecutionList.add(searchDefinitonExecution);
            }
            st2.close();
            rs2.close();

            for (SearchDefinitonExecution currentSearchDefinitonExecution : searchDefinitionExecutionList) {
                ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataProvider = new ScientificPaperMetaInformationDataManager(applicationConfiguration);
                currentSearchDefinitonExecution.setScientificPaperMetaInformation(scientificPaperMetaInformationDataProvider.getScientificMetaInformationBySearchDefinition(currentSearchDefinitonExecution));

            }

        }
        conn2.close();
        return searchDefinitionExecutionList;

    }

    /**
     *
     * @param searchDefinition
     * @return
     * @throws Exception
     */
    public LinkedList<SearchDefinitonExecution> getSearchDefinitionExecutionForSearchDefinitionWithoutScientificMetaData(SearchDefinition searchDefinition) throws Exception {
        LinkedList<SearchDefinitonExecution> searchDefinitionExecutionList = new LinkedList<SearchDefinitonExecution>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn2 = null;
        conn2 = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
        SearchExecution searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);
        for (SystemInstance currentSystemInstance : searchExecution.getSystemInstances()) {

            int lastID = getLastSearchDefinitonExecutionIDForSystemInstance(searchDefinition, currentSystemInstance);

            String query2 = "SELECT * FROM search_definiton_execution where ID=" + lastID;
            Statement st2 = conn2.createStatement();
            ResultSet rs2 = st2.executeQuery(query2);

            while (rs2.next()) {
                SearchDefinitonExecution searchDefinitonExecution = new SearchDefinitonExecution();

                searchDefinitonExecution.setID(rs2.getInt("ID"));
                searchDefinitonExecution.setTotalItems(rs2.getInt("TotalItems"));
                searchDefinitonExecution.setUpdatedItems(rs2.getInt("UpdatedItems"));
                searchDefinitonExecution.setNewItems(rs2.getInt("NewItems"));
                searchDefinitonExecution.setSearch_Definiton_ID(rs2.getInt("Search_Definiton_ID"));
                searchDefinitonExecution.setRequestUrl(rs2.getString("RequestUrl"));
                searchDefinitonExecution.setQuery(rs2.getString("Query"));
                searchDefinitonExecution.setCrawledItems(rs2.getInt("CrawledItems"));
                searchDefinitonExecution.setStartExecutionTimestamp(rs2.getTimestamp("StartExecutionTimestamp"));
                searchDefinitonExecution.setFinishedExecutionTimestamp(rs2.getTimestamp("FinishedExecutionTimestamp"));
                searchDefinitonExecution.setSearchState(rs2.getString("SearchExecutionState"));
                searchDefinitonExecution.setMessage(rs2.getString("Message"));
                searchDefinitonExecution.setLocalItemsFound(rs2.getInt("LocalItemsFound"));

                SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
                searchDefinitonExecution.setSystemInstance(systemInstanceDataProvider.getSystemInstanceByID(rs2.getInt("System_Instance_ID")));

                searchDefinitionExecutionList.add(searchDefinitonExecution);
            }
            st2.close();
            rs2.close();
        }
        conn2.close();
        return searchDefinitionExecutionList;

    }

    /**
     *
     * @param searchDefinition
     * @return
     * @throws Exception
     */
    public LinkedList<SearchDefinitonExecution> getAllSearchDefinitionExecutionForSearchDefinition(SearchDefinition searchDefinition) throws Exception {
        LinkedList<SearchDefinitonExecution> searchDefinitionExecutionList = new LinkedList<SearchDefinitonExecution>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn2 = null;
        conn2 = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
        SearchExecution searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);
        String query2 = "SELECT * FROM search_definiton_execution where Search_Definiton_ID=" + searchDefinition.getID() + " order by ID DESC";
        Statement st2 = conn2.createStatement();
        ResultSet rs2 = st2.executeQuery(query2);

        while (rs2.next()) {
            SearchDefinitonExecution searchDefinitonExecution = new SearchDefinitonExecution();

            searchDefinitonExecution.setID(rs2.getInt("ID"));
            searchDefinitonExecution.setTotalItems(rs2.getInt("TotalItems"));
            searchDefinitonExecution.setUpdatedItems(rs2.getInt("UpdatedItems"));
            searchDefinitonExecution.setNewItems(rs2.getInt("NewItems"));
            searchDefinitonExecution.setSearch_Definiton_ID(rs2.getInt("Search_Definiton_ID"));
            searchDefinitonExecution.setRequestUrl(rs2.getString("RequestUrl"));
            searchDefinitonExecution.setQuery(rs2.getString("Query"));
            searchDefinitonExecution.setCrawledItems(rs2.getInt("CrawledItems"));
            searchDefinitonExecution.setStartExecutionTimestamp(rs2.getTimestamp("StartExecutionTimestamp"));
            searchDefinitonExecution.setFinishedExecutionTimestamp(rs2.getTimestamp("FinishedExecutionTimestamp"));
            searchDefinitonExecution.setSearchState(rs2.getString("SearchExecutionState"));
            searchDefinitonExecution.setMessage(rs2.getString("Message"));
            searchDefinitonExecution.setLocalItemsFound(rs2.getInt("LocalItemsFound"));

            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            searchDefinitonExecution.setSystemInstance(systemInstanceDataProvider.getSystemInstanceByID(rs2.getInt("System_Instance_ID")));

            searchDefinitionExecutionList.add(searchDefinitonExecution);
        }
        st2.close();
        rs2.close();

        conn2.close();
        return searchDefinitionExecutionList;

    }

    /**
     *
     * @return
     * @throws Exception
     */
    public LinkedList<SearchDefinitonExecution> getAllSearchDefinitionExecution() throws Exception {
        LinkedList<SearchDefinitonExecution> searchDefinitionExecutionList = new LinkedList<SearchDefinitonExecution>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn2 = null;
        conn2 = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String query2 = "SELECT * FROM search_definiton_execution order by ID DESC";
        Statement st2 = conn2.createStatement();
        ResultSet rs2 = st2.executeQuery(query2);

        while (rs2.next()) {
            SearchDefinitonExecution searchDefinitonExecution = new SearchDefinitonExecution();

            searchDefinitonExecution.setID(rs2.getInt("ID"));
            searchDefinitonExecution.setTotalItems(rs2.getInt("TotalItems"));
            searchDefinitonExecution.setUpdatedItems(rs2.getInt("UpdatedItems"));
            searchDefinitonExecution.setNewItems(rs2.getInt("NewItems"));
            searchDefinitonExecution.setSearch_Definiton_ID(rs2.getInt("Search_Definiton_ID"));
            searchDefinitonExecution.setRequestUrl(rs2.getString("RequestUrl"));
            searchDefinitonExecution.setQuery(rs2.getString("Query"));
            searchDefinitonExecution.setCrawledItems(rs2.getInt("CrawledItems"));
            searchDefinitonExecution.setStartExecutionTimestamp(rs2.getTimestamp("StartExecutionTimestamp"));
            searchDefinitonExecution.setFinishedExecutionTimestamp(rs2.getTimestamp("FinishedExecutionTimestamp"));
            searchDefinitonExecution.setSearchState(rs2.getString("SearchExecutionState"));
            searchDefinitonExecution.setMessage(rs2.getString("Message"));
            searchDefinitonExecution.setLocalItemsFound(rs2.getInt("LocalItemsFound"));

            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            searchDefinitonExecution.setSystemInstance(systemInstanceDataProvider.getSystemInstanceByID(rs2.getInt("System_Instance_ID")));

            searchDefinitionExecutionList.add(searchDefinitonExecution);
        }
        st2.close();
        rs2.close();

        conn2.close();
        return searchDefinitionExecutionList;

    }

    /**
     *
     * @param searchDefinitonExecutionList
     * @param outputStream
     * @throws Exception
     */
    public void exportToExcel(LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList, OutputStream outputStream) throws Exception {

        Workbook currentWorkBook = new HSSFWorkbook();
        int currenSheetCount = 0;
        for (SearchDefinitonExecution searchDefinitonExecution : searchDefinitonExecutionList) {

            Sheet currentSheet = currentWorkBook.createSheet();
            currentSheet.setFitToPage(true);
            currentSheet.setHorizontallyCenter(true);
            currentSheet.createFreezePane(0, 1);
            currentWorkBook.setSheetName(currenSheetCount, searchDefinitonExecution.getSystemInstance().getName());

            Row headerRow = currentSheet.createRow(0);
            headerRow.setHeightInPoints(12.75f);

            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Title");

            headerRow.createCell(2).setCellValue("Identifier 1");
            headerRow.createCell(3).setCellValue("Identifier 2");
            headerRow.createCell(4).setCellValue("Identifier 3");
            headerRow.createCell(5).setCellValue("Identifier 4");
            headerRow.createCell(6).setCellValue("Url 1");
            headerRow.createCell(7).setCellValue("Url 2");
            headerRow.createCell(8).setCellValue("Text 1");
            headerRow.createCell(9).setCellValue("Publication Name");
            headerRow.createCell(10).setCellValue("Issue Name");
            headerRow.createCell(11).setCellValue("Publish Date");
            headerRow.createCell(12).setCellValue("Volume");
            headerRow.createCell(13).setCellValue("Start Page");
            headerRow.createCell(14).setCellValue("Issue Identifier");

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
            for (ScientificPaperMetaInformation scientificPaperMetaInformation : searchDefinitonExecution.getScientificPaperMetaInformation()) {
                currentRow = currentSheet.createRow(rowNum);
                currentRow.createCell(0).setCellValue(scientificPaperMetaInformation.getID());
                currentRow.createCell(1).setCellValue(scientificPaperMetaInformation.getTitle());
                currentRow.createCell(2).setCellValue(scientificPaperMetaInformation.getIdentifier_1());
                currentRow.createCell(3).setCellValue(scientificPaperMetaInformation.getIdentifier_2());
                currentRow.createCell(4).setCellValue(scientificPaperMetaInformation.getIdentifier_3());
                currentRow.createCell(5).setCellValue(scientificPaperMetaInformation.getIdentifier_4());
                currentRow.createCell(6).setCellValue(scientificPaperMetaInformation.getUrl_1());
                currentRow.createCell(7).setCellValue(scientificPaperMetaInformation.getUrl_2());
                currentRow.createCell(8).setCellValue(scientificPaperMetaInformation.getText_1());

                currentRow.createCell(9).setCellValue(scientificPaperMetaInformation.getSrcTitle());
                currentRow.createCell(10).setCellValue(scientificPaperMetaInformation.getScrPublisherName());
                currentRow.createCell(11).setCellValue(scientificPaperMetaInformation.getSrcPublicationDate());
                currentRow.createCell(12).setCellValue(scientificPaperMetaInformation.getSrcVolume());
                currentRow.createCell(13).setCellValue(scientificPaperMetaInformation.getSrcStartPage());
                currentRow.createCell(14).setCellValue(scientificPaperMetaInformation.getScrIdentifier_1());

                rowNum++;

            }
            currenSheetCount++;
        }
        currentWorkBook.write(outputStream);

        outputStream.close();

    }

}
