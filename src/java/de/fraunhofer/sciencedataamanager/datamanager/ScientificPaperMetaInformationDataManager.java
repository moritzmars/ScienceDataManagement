/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformation;
import de.fraunhofer.sciencedataamanager.domain.SearchAnalyticDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Moritz Mars
 */
public class ScientificPaperMetaInformationDataManager {

    private ApplicationConfiguration applicationConfiguration;

    public ScientificPaperMetaInformationDataManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public LinkedList<ScientificPaperMetaInformation> getScientificMetaInformationBySearchDefinition(SearchDefinitonExecution searchDefinitonExecution) throws Exception {
        LinkedList<ScientificPaperMetaInformation> scientificPaperMetaInformationList = new LinkedList<ScientificPaperMetaInformation>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM scientific_paper_meta_information where Search_Definiton_Execution_ID=" + searchDefinitonExecution.getID();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            ScientificPaperMetaInformation scientificPaperMetaInformation = new ScientificPaperMetaInformation();

            scientificPaperMetaInformation.setID(rs.getInt("ID"));
            scientificPaperMetaInformation.setTitle(rs.getString("Title"));

            scientificPaperMetaInformation.setIdentifier_1(rs.getString("Identifier_1"));
            scientificPaperMetaInformation.setIdentifier_2(rs.getString("Identifier_2"));
            scientificPaperMetaInformation.setIdentifier_3(rs.getString("Identifier_3"));
            scientificPaperMetaInformation.setIdentifier_4(rs.getString("Identifier_4"));
            scientificPaperMetaInformation.setIdentifier_5(rs.getString("Identifier_5"));
            scientificPaperMetaInformation.setIdentifier_6(rs.getString("Identifier_6"));
            scientificPaperMetaInformation.setIdentifier_7(rs.getString("Identifier_7"));
            scientificPaperMetaInformation.setIdentifier_8(rs.getString("Identifier_8"));
            scientificPaperMetaInformation.setIdentifier_9(rs.getString("Identifier_9"));
            scientificPaperMetaInformation.setIdentifier_10(rs.getString("Identifier_10"));

            scientificPaperMetaInformation.setScrPublisherName(rs.getString("ScrPublisherName"));
            scientificPaperMetaInformation.setSrcPublicationDate(rs.getDate("SrcPublicationDate"));
            scientificPaperMetaInformation.setSrcDOI(rs.getString("SrcDOI"));
            scientificPaperMetaInformation.setSrcIISN(rs.getString("SrcIISN"));
            scientificPaperMetaInformation.setSrcVolume(rs.getInt("SrcVolume"));
            scientificPaperMetaInformation.setScrIdentifier_1(rs.getString("ScrIdentifier_1"));
            scientificPaperMetaInformation.setSrcIdentifier_2(rs.getString("SrcIdentifier_2"));
            scientificPaperMetaInformation.setSrcIdentifier_3(rs.getString("SrcIdentifier_3"));
            scientificPaperMetaInformation.setSrcIdentifier_4(rs.getString("SrcIdentifier_4"));
            scientificPaperMetaInformation.setSrcISBN(rs.getString("SrcISBN"));
            scientificPaperMetaInformation.setUrl_1(rs.getString("Url_1"));
            scientificPaperMetaInformation.setUrl_2(rs.getString("Url_2"));
            scientificPaperMetaInformation.setSrcAdditionalField_1(rs.getString("SrcAdditionalField_1"));
            scientificPaperMetaInformation.setSrcAdditionalField_2(rs.getString("SrcAdditionalField_2"));
            scientificPaperMetaInformation.setSrcTitle(rs.getString("srcTitle"));
            scientificPaperMetaInformation.setSrcStartPage(rs.getString("srcStartPage"));
            scientificPaperMetaInformation.setSrcEndPage(rs.getString("srcEndPage"));
            scientificPaperMetaInformation.setSrcDate_1(rs.getTimestamp("srcDate_1"));
            scientificPaperMetaInformation.setSrcDate_2(rs.getTimestamp("srcDate_2"));
            scientificPaperMetaInformation.setText_1(rs.getString("Text_1"));
            scientificPaperMetaInformation.setText_2(rs.getString("Text_2"));
            scientificPaperMetaInformation.setLocalizedTime(rs.getTimestamp("LocalizedTime"));
            ScientificPaperMetaInformationAuthorDataManager scientificPaperMetaInformationAuthorDataManager = new ScientificPaperMetaInformationAuthorDataManager(applicationConfiguration);
            scientificPaperMetaInformation.setAuthors(scientificPaperMetaInformationAuthorDataManager.getAuthorsByScientificMetaInformation(scientificPaperMetaInformation));
            scientificPaperMetaInformationList.add(scientificPaperMetaInformation);
        }

        st.close();
        rs.close();
        conn.close();

        return scientificPaperMetaInformationList;

    }

    public void insertScientificPaperMetaInformation(SearchDefinitonExecution searchDefinitonExecution) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        for (ScientificPaperMetaInformation cloudPaperResult : searchDefinitonExecution.getScientificPaperMetaInformation()) {
            String sqlInsertStatement = "INSERT INTO  scientific_paper_meta_information (Title, Search_Definiton_Execution_Search_Definiton_ID, "
                    + "Search_Definiton_Execution_ID, search_definition_execution_run_ID, search_definition_execution_run_search_definiton_ID, Identifier_1, Identifier_2, Identifier_3, Identifier_4, Identifier_5, "
                    + "Identifier_6, Identifier_7, Identifier_8, Identifier_9, Identifier_10, ScrPublisherName,SrcPublicationDate,"
                    + "SrcDOI, SrcIISN, SrcVolume, ScrIdentifier_1, SrcIdentifier_2, SrcIdentifier_3, SrcIdentifier_4, SrcISBN,"
                    + "Url_1, Url_2, SrcAdditionalField_1, SrcAdditionalField_2, srcTitle, srcStartPage, srcEndPage,srcDate_1, srcDate_2,"
                    + "Text_1, Text_2, LocalizedTime)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
            preparedStatement.setString(1, cloudPaperResult.getTitle());
            preparedStatement.setInt(2, searchDefinitonExecution.getSearch_Definiton_ID());
            preparedStatement.setInt(3, searchDefinitonExecution.getID());
            preparedStatement.setInt(4, searchDefinitonExecution.getSearch_Definition_Execution_Run_ID());
            preparedStatement.setInt(5, searchDefinitonExecution.getSearch_Definiton_ID());
            
            preparedStatement.setString(6, cloudPaperResult.getIdentifier_1());
            preparedStatement.setString(7, cloudPaperResult.getIdentifier_2());
            preparedStatement.setString(8, cloudPaperResult.getIdentifier_3());
            preparedStatement.setString(9, cloudPaperResult.getIdentifier_4());
            preparedStatement.setString(10, cloudPaperResult.getIdentifier_5());
            preparedStatement.setString(11, cloudPaperResult.getIdentifier_6());
            preparedStatement.setString(12, cloudPaperResult.getIdentifier_7());
            preparedStatement.setString(13, cloudPaperResult.getIdentifier_8());
            preparedStatement.setString(14, cloudPaperResult.getIdentifier_9());
            preparedStatement.setString(15, cloudPaperResult.getIdentifier_10());

            preparedStatement.setString(16, cloudPaperResult.getScrPublisherName());
            preparedStatement.setDate(17, cloudPaperResult.getSrcPublicationDate());
            preparedStatement.setString(18, cloudPaperResult.getSrcDOI());
            preparedStatement.setString(19, cloudPaperResult.getSrcIISN());
            preparedStatement.setInt(20, cloudPaperResult.getSrcVolume());
            preparedStatement.setString(21, cloudPaperResult.getScrIdentifier_1());
            preparedStatement.setString(22, cloudPaperResult.getSrcIdentifier_2());
            preparedStatement.setString(23, cloudPaperResult.getSrcIdentifier_3());
            preparedStatement.setString(24, cloudPaperResult.getSrcIdentifier_4());
            preparedStatement.setString(25, cloudPaperResult.getSrcISBN());
            preparedStatement.setString(26, cloudPaperResult.getUrl_1());
            preparedStatement.setString(27, cloudPaperResult.getUrl_2());
            preparedStatement.setString(28, cloudPaperResult.getSrcAdditionalField_1());
            preparedStatement.setString(29, cloudPaperResult.getSrcAdditionalField_2());
            preparedStatement.setString(30, cloudPaperResult.getSrcTitle());
            preparedStatement.setString(31, cloudPaperResult.getSrcStartPage());
            preparedStatement.setString(32, cloudPaperResult.getSrcEndPage());
            preparedStatement.setTimestamp(33, cloudPaperResult.getSrcDate_1());
            preparedStatement.setTimestamp(34, cloudPaperResult.getSrcDate_2());
            preparedStatement.setString(35, cloudPaperResult.getText_1());
            preparedStatement.setString(36, cloudPaperResult.getText_2());
            preparedStatement.setTimestamp(37, cloudPaperResult.getLocalizedTime());

            preparedStatement.execute();
            preparedStatement.close();
            cloudPaperResult.setSearch_Definiton_Execution_Search_Definiton_ID(searchDefinitonExecution.getID());
            cloudPaperResult.setID(getLastScientificPaperMetaInformationBySearchDefinitionID(cloudPaperResult));

            ScientificPaperMetaInformationAuthorDataManager scientificPaperMetaInformationAuthorDataManager = new ScientificPaperMetaInformationAuthorDataManager(applicationConfiguration);
            scientificPaperMetaInformationAuthorDataManager.insertAuthors(cloudPaperResult);
        }
        conn.close();
    }

    public ScientificPaperMetaInformation getScientificMetaInformationByID(ScientificPaperMetaInformation scientificPaperMetaInformation) throws Exception {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM scientific_paper_meta_information where Identifier_1='" + scientificPaperMetaInformation.getIdentifier_1() + "'";
        scientificPaperMetaInformation = null;
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            scientificPaperMetaInformation = new ScientificPaperMetaInformation();

            scientificPaperMetaInformation.setID(rs.getInt("ID"));
            scientificPaperMetaInformation.setTitle(rs.getString("Title"));

            scientificPaperMetaInformation.setIdentifier_1(rs.getString("Identifier_1"));
            scientificPaperMetaInformation.setIdentifier_2(rs.getString("Identifier_2"));
            scientificPaperMetaInformation.setIdentifier_3(rs.getString("Identifier_3"));
            scientificPaperMetaInformation.setIdentifier_4(rs.getString("Identifier_4"));
            scientificPaperMetaInformation.setIdentifier_5(rs.getString("Identifier_5"));
            scientificPaperMetaInformation.setIdentifier_6(rs.getString("Identifier_6"));
            scientificPaperMetaInformation.setIdentifier_7(rs.getString("Identifier_7"));
            scientificPaperMetaInformation.setIdentifier_8(rs.getString("Identifier_8"));
            scientificPaperMetaInformation.setIdentifier_9(rs.getString("Identifier_9"));
            scientificPaperMetaInformation.setIdentifier_10(rs.getString("Identifier_10"));

            scientificPaperMetaInformation.setScrPublisherName(rs.getString("ScrPublisherName"));
            scientificPaperMetaInformation.setSrcPublicationDate(rs.getDate("SrcPublicationDate"));
            scientificPaperMetaInformation.setSrcDOI(rs.getString("SrcDOI"));
            scientificPaperMetaInformation.setSrcIISN(rs.getString("SrcIISN"));
            scientificPaperMetaInformation.setSrcVolume(rs.getInt("SrcVolume"));
            scientificPaperMetaInformation.setScrIdentifier_1(rs.getString("ScrIdentifier_1"));
            scientificPaperMetaInformation.setSrcIdentifier_2(rs.getString("SrcIdentifier_2"));
            scientificPaperMetaInformation.setSrcIdentifier_3(rs.getString("SrcIdentifier_3"));
            scientificPaperMetaInformation.setSrcIdentifier_4(rs.getString("SrcIdentifier_4"));
            scientificPaperMetaInformation.setSrcISBN(rs.getString("SrcISBN"));
            scientificPaperMetaInformation.setUrl_1(rs.getString("Url_1"));
            scientificPaperMetaInformation.setUrl_2(rs.getString("Url_2"));
            scientificPaperMetaInformation.setSrcAdditionalField_1(rs.getString("SrcAdditionalField_1"));
            scientificPaperMetaInformation.setSrcAdditionalField_2(rs.getString("SrcAdditionalField_2"));
            scientificPaperMetaInformation.setSrcTitle(rs.getString("srcTitle"));
            scientificPaperMetaInformation.setSrcStartPage(rs.getString("srcStartPage"));
            scientificPaperMetaInformation.setSrcEndPage(rs.getString("srcEndPage"));
            scientificPaperMetaInformation.setSrcDate_1(rs.getTimestamp("srcDate_1"));
            scientificPaperMetaInformation.setSrcDate_2(rs.getTimestamp("srcDate_2"));
            scientificPaperMetaInformation.setText_1(rs.getString("Text_1"));
            scientificPaperMetaInformation.setText_2(rs.getString("Text_2"));
            scientificPaperMetaInformation.setLocalizedTime(rs.getTimestamp("LocalizedTime"));
            ScientificPaperMetaInformationAuthorDataManager scientificPaperMetaInformationAuthorDataManager = new ScientificPaperMetaInformationAuthorDataManager(applicationConfiguration);
            scientificPaperMetaInformation.setAuthors(scientificPaperMetaInformationAuthorDataManager.getAuthorsByScientificMetaInformation(scientificPaperMetaInformation));

        }

        st.close();
        rs.close();
        conn.close();

        return scientificPaperMetaInformation;

    }

    public int getLastScientificPaperMetaInformationBySearchDefinitionID(ScientificPaperMetaInformation scientificPaperMetaInformation) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String query = "SELECT MAX(ID) lastID FROM scientific_paper_meta_information where Search_Definiton_Execution_ID=" + scientificPaperMetaInformation.getSearch_Definiton_Execution_Search_Definiton_ID();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        rs.next();
        int lastSearchDefinitionID = rs.getInt("lastID");

        st.close();
        rs.close();
        conn.close();
        return lastSearchDefinitionID;
    }

    public Map<String, List<Object>> getScientificPaperMetaInformationResultSetBySearchAnalyticDefinition(SearchAnalyticDefinition searchAnalyticDefinition, SearchDefinition searchDefinition) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(searchAnalyticDefinition.getQuery());
        Map<String, List<Object>> transformedResultSet = this.resultSetToArrayList(rs);
        st.close();
        rs.close();
        conn.close();
        return transformedResultSet;
    }
    
    public Map<String, List<Object>> getScientificMetaInformationBySearchDefinitionAsMap(SearchDefinitonExecution searchDefinitonExecution) throws Exception {
        LinkedList<ScientificPaperMetaInformation> scientificPaperMetaInformationList = new LinkedList<ScientificPaperMetaInformation>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT ID,Title,Identifier_1,Identifier_2,Identifier_3,Identifier_4,Identifier_5,Identifier_6,Identifier_7,"
                + "Identifier_8, Identifier_9, Identifier_10, ScrPublisherName, SrcPublicationDate, SrcDOI, SrcIISN, SrcVolume, ScrIdentifier_1,"
                + "SrcIdentifier_2,SrcIdentifier_3, SrcIdentifier_4, SrcISBN, Url_1, Url_2, SrcAdditionalField_1, SrcAdditionalField_2,"
                + "srcTitle,srcStartPage,srcEndPage,srcDate_1,srcDate_2, Text_1, Text_2, LocalizedTime FROM scientific_paper_meta_information where Search_Definiton_Execution_ID=" + searchDefinitonExecution.getID();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        Map<String, List<Object>> resultAsMap = this.resultSetToArrayList(rs);
        st.close();
        rs.close();
        conn.close();

        return resultAsMap;

    }

    
    private Map<String, List<Object>> resultSetToArrayList(ResultSet rs) throws Exception {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<String, List<Object>> map = new HashMap<>(columns);
        for (int i = 1; i <= columns; ++i) {
            map.put(md.getColumnName(i), new ArrayList<>());
        }
        while (rs.next()) {
            for (int i = 1; i <= columns; ++i) {
                map.get(md.getColumnName(i)).add(rs.getObject(i));
            }
        }
        return map;
    }
}
