/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchExecution;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Moritz Mars
 */
public class SearchExecutionDataManager {
           private ApplicationConfiguration applicationConfiguration; 

    /**
     *
     * @param applicationConfiguration
     */
    public SearchExecutionDataManager(ApplicationConfiguration applicationConfiguration)
    {
       this.applicationConfiguration = applicationConfiguration; 
    }

    /**
     *
     * @param searchDefinition
     * @return
     * @throws Exception
     */
    public SearchExecution getSystemInstanceBySearchDefinition(SearchDefinition searchDefinition) throws Exception {
        Collection systemInstanceList = new LinkedList();
        SearchExecution searchExecution = new SearchExecution();
        searchExecution.setSearchDefiniton(searchDefinition);

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM search_definiton_system_instance where SearchDefiniton_ID=" + searchDefinition.getID();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            searchExecution.getSystemInstances().add(systemInstanceDataProvider.getSystemInstanceByID(rs.getInt("SystemInstance_ID")));
        }

        st.close();
        rs.close();
        conn.close();
        return searchExecution;
    }

    /**
     *
     * @param searchExecution
     * @throws Exception
     */
    public void saveSearchExecution(SearchExecution searchExecution) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        for (SystemInstance currentSystemInstance : searchExecution.getSystemInstances()) {

            String sqlInsertStatement = "INSERT INTO  search_definiton_system_instance (SearchDefiniton_ID, SystemInstance_ID)"
                    + " VALUES (?,?)";

            java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
            preparedStatement.setInt(1, searchExecution.getSearchDefiniton().getID());
            preparedStatement.setInt(2, currentSystemInstance.getID());

            preparedStatement.execute();
            preparedStatement.close();
        }
        conn.close();
    }

    /**
     *
     * @param searchExecution
     * @throws Exception
     */
    public void deleteSystemInstancesBySearchExecution(SearchExecution searchExecution) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn2 = null;
        conn2 = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query2 = "DELETE FROM search_definiton_system_instance where SearchDefiniton_ID =" + searchExecution.getSearchDefiniton().getID();
        java.sql.PreparedStatement preparedStatement = conn2.prepareStatement(query2);
        preparedStatement.execute();
        preparedStatement.close();
        conn2.close();
    }
}
