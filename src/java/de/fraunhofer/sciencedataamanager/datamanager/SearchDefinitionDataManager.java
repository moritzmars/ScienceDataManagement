/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchDefinitionDataProvider")
@SessionScoped
public class SearchDefinitionDataManager {
            private ApplicationConfiguration applicationConfiguration; 
    public SearchDefinitionDataManager(ApplicationConfiguration applicationConfiguration)
    {
       this.applicationConfiguration = applicationConfiguration; 
    }
    public LinkedList<SearchDefinition> getSearchDefinitions() throws Exception {
        LinkedList<SearchDefinition> searchDefinitionList = new LinkedList();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM search_definiton";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(rs.getInt("ID"));
            searchDefinition.setName(rs.getString("Name"));

            searchDefinitionList.add(searchDefinition);
        }

        st.close();
        rs.close();
        conn.close();

        return searchDefinitionList;

    }

    public SearchDefinition getSearchDefinitionByID(int id) throws Exception {
        SearchDefinition searchDefinition = new SearchDefinition();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM search_definiton where ID=" + id;

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            searchDefinition.setID(rs.getInt("ID"));
            searchDefinition.setName(rs.getString("Name"));
            LinkedList<SearchTerm> searchTerms = new LinkedList<SearchTerm>();
            SearchTermDataManager searchTermDataProvider = new SearchTermDataManager(applicationConfiguration);
            searchDefinition.setSearchTerms(searchTermDataProvider.getSearchTermsBySearchDefinition(searchDefinition));

        }
        st.close();
        rs.close();
        conn.close();

        return searchDefinition;

    }

    public void updateSearchDefinition(SearchDefinition searchDefinition) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String sqlInsertStatement = "Update search_definiton set Name=? where ID =?";

        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, searchDefinition.getName());
        preparedStatement.setInt(2, searchDefinition.getID());

        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
    }

    public void addSearchDefinition(SearchExecution searchExecution) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String sqlInsertStatement = "INSERT INTO  search_definiton (Name)"
                + " VALUES (?)";

        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, searchExecution.getSearchDefiniton().getName());

        preparedStatement.execute();
        preparedStatement.close();

        String query2 = "SELECT MAX(ID) lastID FROM search_definiton";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query2);
        rs.next();
        searchExecution.getSearchDefiniton().setID(rs.getInt("lastID"));
        st.close();
        rs.close();

        SearchTermDataManager searchTermDataProvider = new SearchTermDataManager(applicationConfiguration); 
        searchTermDataProvider.insertSearchTerms(searchExecution.getSearchDefiniton());
        
        SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
        searchExecutionDataProvider.saveSearchExecution(searchExecution);
        
    }
    
        public void delete(int id) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "delete from search_definiton where ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        preparedStatement.execute();

        preparedStatement.close();
        conn.close();

    }

}
