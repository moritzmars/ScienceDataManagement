/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitionExecutionRun;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 *
 * @author Moritz Mars
 */
public class SearchDefinitonExecutionRunDataManager {

    private ApplicationConfiguration applicationConfiguration;

    /**
     *
     * @param applicationConfiguration
     */
    public SearchDefinitonExecutionRunDataManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    /**
     *
     * @param searchDefinitionExecutionRun
     * @return
     * @throws Exception
     */
    public SearchDefinitionExecutionRun insertSearchDefinitionExecutionRunLastID(SearchDefinitionExecutionRun searchDefinitionExecutionRun) throws Exception {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        this.insertSearchDefinitionExecutionRun(searchDefinitionExecutionRun);

        String query = "SELECT MAX(ID) lastID FROM search_definition_execution_run";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        rs.next();
        searchDefinitionExecutionRun.setID(rs.getInt("lastID"));

        st.close();
        rs.close();

        return searchDefinitionExecutionRun;

    }

    /**
     *
     * @param searchDefinitionExecutionRun
     * @throws Exception
     */
    public void insertSearchDefinitionExecutionRun(SearchDefinitionExecutionRun searchDefinitionExecutionRun) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String sqlInsertStatement = "INSERT INTO search_definition_execution_run (Description, StartExecutionTimestamp, FinishedExecutionTimestamp, search_definiton_ID)"
                + " VALUES (?,?,?,?)";
        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, searchDefinitionExecutionRun.getDescription());
        preparedStatement.setTimestamp(2, searchDefinitionExecutionRun.getStartExecutionTimestamp());
        preparedStatement.setTimestamp(3, searchDefinitionExecutionRun.getFinishedExecutionTimestamp());
        preparedStatement.setInt(4, searchDefinitionExecutionRun.getSearchExecution().getSearchDefiniton().getID());

        preparedStatement.execute();
        preparedStatement.close();

        conn.close();

    }

    /**
     *
     * @param searchDefinitionExecutionRun
     * @throws Exception
     */
    public void updateSearchDefinitionExecutionRun(SearchDefinitionExecutionRun searchDefinitionExecutionRun) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String sqlInsertStatement = "Update search_definition_execution_run set Description=?, StartExecutionTimestamp =?, FinishedExecutionTimestamp =? where ID =?";

        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, searchDefinitionExecutionRun.getDescription());
        preparedStatement.setTimestamp(2, searchDefinitionExecutionRun.getStartExecutionTimestamp());
        preparedStatement.setTimestamp(3, searchDefinitionExecutionRun.getFinishedExecutionTimestamp());
        preparedStatement.setInt(4, searchDefinitionExecutionRun.getID());

        preparedStatement.execute();
        preparedStatement.close();

        conn.close();
        SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);

        for (SearchDefinitonExecution searchDefinitonExecution : searchDefinitionExecutionRun.getSearchDefinitionExecutionList()) {
            searchDefinitonExecution.setSearch_Definition_Execution_Run_ID(searchDefinitionExecutionRun.getID());
            searchDefinitonExecutionDataProvider.saveCloudPapers(searchDefinitonExecution);
        }
    }

    /**
     *
     * @param searchDefinition
     * @return
     * @throws Exception
     */
    public LinkedList<SearchDefinitionExecutionRun> getAllSearchDefinitionExecutionRunForSearchDefinition(SearchDefinition searchDefinition) throws Exception {
        LinkedList<SearchDefinitionExecutionRun> searchDefinitionExecutionRunList = new LinkedList<SearchDefinitionExecutionRun>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn2 = null;
        conn2 = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String query2 = "SELECT * FROM search_definition_execution_run where search_definiton_ID=" + searchDefinition.getID() + " order by ID DESC";
        Statement st2 = conn2.createStatement();
        ResultSet rs2 = st2.executeQuery(query2);

        while (rs2.next()) {
            SearchDefinitionExecutionRun searchDefinitionExecutionRun = new SearchDefinitionExecutionRun();

            searchDefinitionExecutionRun.setID(rs2.getInt("ID"));
            searchDefinitionExecutionRun.setDescription(rs2.getString("Description"));
            searchDefinitionExecutionRun.setStartExecutionTimestamp(rs2.getTimestamp("StartExecutionTimestamp"));
            searchDefinitionExecutionRun.setFinishedExecutionTimestamp(rs2.getTimestamp("FinishedExecutionTimestamp"));
        
            searchDefinitionExecutionRunList.add(searchDefinitionExecutionRun);
        }
        st2.close();
        rs2.close();

        conn2.close();
        return searchDefinitionExecutionRunList;

    }

}
