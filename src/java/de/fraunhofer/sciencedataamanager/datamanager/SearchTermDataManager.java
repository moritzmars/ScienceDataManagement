/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchTerm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 *
 * @author Moritz Mars
 */
public class SearchTermDataManager {
         private ApplicationConfiguration applicationConfiguration; 

    /**
     *
     * @param applicationConfiguration
     */
    public SearchTermDataManager(ApplicationConfiguration applicationConfiguration)
    {
       this.applicationConfiguration = applicationConfiguration; 
    }

    /**
     *
     * @param searchDefinition
     * @return
     * @throws Exception
     */
    public LinkedList<SearchTerm> getSearchTermsBySearchDefinition(SearchDefinition searchDefinition) throws Exception {
        LinkedList<SearchTerm> searchTerms = new LinkedList<SearchTerm>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        String query = "SELECT * FROM search_term where Search_Definiton_ID=" + searchDefinition.getID();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            SearchTerm searchTerm = new SearchTerm();
            searchTerm.setID(rs.getInt("ID"));
            searchTerm.setTerm(rs.getString("Term"));
            searchTerm.setOperation(rs.getString("Operation"));

            searchTerms.add(searchTerm);
        }

        st.close();
        rs.close();
        conn.close();
        return searchTerms;
    }

    /**
     *
     * @param searchDefinition
     * @throws Exception
     */
    public void insertSearchTerms(SearchDefinition searchDefinition) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        for (SearchTerm searchTerm : searchDefinition.getSearchTerms()) {

            String sqlInsertStatement = "INSERT INTO search_term (Term, Operation, Search_Definiton_ID)"
                    + " VALUES (?,?,?)";
            java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
            preparedStatement.setString(1, searchTerm.getTerm());
            preparedStatement.setString(2, searchTerm.getOperation());
            preparedStatement.setInt(3, searchDefinition.getID());

            preparedStatement.execute();
            preparedStatement.close();
        }

        conn.close();

    }

    /**
     *
     * @param searchTerm
     * @throws Exception
     */
    public void deleteSearchTerm(SearchTerm searchTerm) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "delete from search_term where id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, searchTerm.getID());

        preparedStatement.execute();
        preparedStatement.close();
        conn.close();

    }
}
