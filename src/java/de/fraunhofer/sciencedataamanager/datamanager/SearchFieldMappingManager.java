/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchFieldMapping;
import de.fraunhofer.sciencedataamanager.domain.SearchTerm;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 *
 * @author Moritz Mars
 */
public class SearchFieldMappingManager {

    private ApplicationConfiguration applicationConfiguration;

    /**
     *
     * @param applicationConfiguration
     */
    public SearchFieldMappingManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public LinkedList<SearchFieldMapping> getFieldMappingBySystemInstance(SystemInstance systemInstance) throws Exception {

        LinkedList<SearchFieldMapping> searchFieldMappingList = new LinkedList<SearchFieldMapping>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM search_field_mapping sfm INNER JOIN search_field_mapping_system_instance sfmsi ON sfm.id = sfmsi.search_field_mapping_ID where system_instance_ID = " + systemInstance.getID();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next())
        {
            SearchFieldMapping searchFieldMapping = new SearchFieldMapping();
            searchFieldMapping.setID(rs.getInt("ID"));
            searchFieldMapping.setFieldName(rs.getString("FieldName"));
            searchFieldMapping.setMetaFieldName(rs.getString("MetaFieldName"));

            searchFieldMappingList.add(searchFieldMapping);
        }
        st.close();
        rs.close();
        conn.close();

        return searchFieldMappingList;
    }

    public LinkedList<SearchFieldMapping> getFieldMappings() throws Exception {

        LinkedList<SearchFieldMapping> searchFieldMappingList = new LinkedList<SearchFieldMapping>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM search_field_mapping";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next())
        {
            SearchFieldMapping searchFieldMapping = new SearchFieldMapping();
            searchFieldMapping.setID(rs.getInt("ID"));
            searchFieldMapping.setMetaFieldName(rs.getString("MetaFieldName"));

            searchFieldMappingList.add(searchFieldMapping);
        }
        st.close();
        rs.close();
        conn.close();

        return searchFieldMappingList;
    }

    public void insertMetaSearchField(SearchFieldMapping searchFieldMapping) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String sqlInsertStatement = "INSERT INTO search_field_mapping (MetaFieldName)"
                + " VALUES (?)";
        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, searchFieldMapping.getMetaFieldName());
        preparedStatement.execute();
        preparedStatement.close();

        int lastID;
        String query2 = "SELECT MAX(ID) lastID FROM search_field_mapping";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query2);
        rs.next();
        searchFieldMapping.setID(rs.getInt("lastID"));
        st.close();
        rs.close();
        conn.close();
        SystemInstanceDataManager systemInstanceDataManager = new SystemInstanceDataManager(this.applicationConfiguration);
        LinkedList<SystemInstance> allSystemInstances = systemInstanceDataManager.getSystemInstances();
        for (SystemInstance currentSystemInstance : allSystemInstances)
        {
            this.insertSearchFieldMapping(currentSystemInstance, searchFieldMapping);
        }

    }

    public void insertSearchFieldMapping(SystemInstance systemInstance, SearchFieldMapping searchFieldMapping) throws Exception {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String sqlInsertStatement = "INSERT INTO search_field_mapping_system_instance (system_instance_ID, search_field_mapping_ID, FieldName)"
                + " VALUES (?,?,?)";
        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setInt(1, systemInstance.getID());
        preparedStatement.setInt(2, searchFieldMapping.getID());
        preparedStatement.setString(3, "");
               
        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
    }

    public void updateSearchFieldMappings(SystemInstance systemInstance) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        for (SearchFieldMapping searchFieldMapping : systemInstance.getSearchFieldMappings())
        {
            String sqlInsertStatement = "Update search_field_mapping_system_instance set FieldName=? where search_field_mapping_ID =? AND system_instance_ID=?";
            java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
            preparedStatement.setString(1, searchFieldMapping.getFieldName());
            preparedStatement.setInt(2, searchFieldMapping.getID());
            preparedStatement.setInt(3, systemInstance.getID());

            preparedStatement.execute();
            preparedStatement.close();
        }
        conn.close();
    }

    public void deleteSearchFieldMappings(SearchFieldMapping searchFieldMapping) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String sqlInsertStatement = "Delete from search_field_mapping_system_instance where search_field_mapping_ID =?";
        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setInt(1, searchFieldMapping.getID());
        preparedStatement.execute();
        preparedStatement.close();

        String sqlDeleteStatement = "Delete from search_field_mapping where ID =?";
        java.sql.PreparedStatement preparedDeleteStatement = conn.prepareStatement(sqlDeleteStatement);
        preparedDeleteStatement.setInt(1, searchFieldMapping.getID());
        preparedDeleteStatement.execute();
        preparedDeleteStatement.close();

        conn.close();
    }

}
