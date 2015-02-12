/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchAnalyticDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchExecution;
import de.fraunhofer.sciencedataamanager.domain.SearchTerm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Moritz Mars
 */
public class SearchAnalyticDefinitionDataManager {

    private ApplicationConfiguration applicationConfiguration;

    public SearchAnalyticDefinitionDataManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public LinkedList<SearchAnalyticDefinition> getAnalyticDefinitions() throws Exception {
        LinkedList<SearchAnalyticDefinition> searchAnalyticDefinitionList = new LinkedList();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM search_analytics_definition";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            SearchAnalyticDefinition searchAnalyticDefinition = new SearchAnalyticDefinition();
            searchAnalyticDefinition.setID(rs.getInt("ID"));
            searchAnalyticDefinition.setName(rs.getString("Name"));
            searchAnalyticDefinition.setQuery(rs.getString("Query"));
            searchAnalyticDefinitionList.add(searchAnalyticDefinition);
        }

        st.close();
        rs.close();
        conn.close();

        return searchAnalyticDefinitionList;

    }

    public void addSearchAnalyticDefinition(SearchAnalyticDefinition searchAnalyticDefinition) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String sqlInsertStatement = "INSERT INTO  search_analytics_definition (Name, Query)"
                + " VALUES (?,?)";

        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, searchAnalyticDefinition.getName());
        preparedStatement.setString(2, searchAnalyticDefinition.getQuery());

        preparedStatement.execute();
        preparedStatement.close();

        conn.close();

    }

    public SearchAnalyticDefinition getSearchAnalyticDefinitionByID(int id) throws Exception {
        SearchAnalyticDefinition searchAnalyticDefinition = new SearchAnalyticDefinition();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM search_analytics_definition where ID=" + id;

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            searchAnalyticDefinition.setID(rs.getInt("ID"));
            searchAnalyticDefinition.setName(rs.getString("Name"));
            searchAnalyticDefinition.setQuery(rs.getString("Query"));
        }
        st.close();
        rs.close();
        conn.close();

        return searchAnalyticDefinition;

    }

    public void updateSearchAnalyticDefinition(SearchAnalyticDefinition searchAnalyticDefinition) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String sqlInsertStatement = "Update search_analytics_definition set Name=?, Query =? where ID =?";

        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, searchAnalyticDefinition.getName());
         preparedStatement.setString(2, searchAnalyticDefinition.getQuery());
        preparedStatement.setInt(3, searchAnalyticDefinition.getID());

        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
    }
        public void delete(int id) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "delete from search_analytics_definition where id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        preparedStatement.execute();

        preparedStatement.close();
        conn.close();

    }
}
