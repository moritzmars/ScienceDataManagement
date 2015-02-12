/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformation;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformationAuthors;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 *
 * @author Moritz Mars
 */
public class ScientificPaperMetaInformationAuthorDataManager {

    private ApplicationConfiguration applicationConfiguration;

    public ScientificPaperMetaInformationAuthorDataManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public void insertAuthors(ScientificPaperMetaInformation scientificPaperMetaInformation) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        for (ScientificPaperMetaInformationAuthors scientificPaperMetaInformationAuthor : scientificPaperMetaInformation.getAuthors()) {

            String sqlInsertStatement = "INSERT INTO scientific_paper_meta_information_authors (SurName, GivenName, Scientific_Paper_Meta_Information_ID)"
                    + " VALUES (?,?,?)";

            java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
            preparedStatement.setString(1, scientificPaperMetaInformationAuthor.getSurName());
            preparedStatement.setString(2, scientificPaperMetaInformationAuthor.getGivenName());
            preparedStatement.setInt(3, scientificPaperMetaInformation.getID());

            preparedStatement.execute();
            preparedStatement.close();
        }
        conn.close();
    }
    
      public LinkedList<ScientificPaperMetaInformationAuthors> getAuthorsByScientificMetaInformation(ScientificPaperMetaInformation scientificPaperMetaInformation) throws Exception {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM scientific_paper_meta_information_authors where Scientific_Paper_Meta_Information_ID=" + scientificPaperMetaInformation.getID();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        LinkedList<ScientificPaperMetaInformationAuthors> scientificPaperMetaInformationAuthorsList = new LinkedList<ScientificPaperMetaInformationAuthors>(); 
        
        while (rs.next()) {
            ScientificPaperMetaInformationAuthors scientificPaperMetaInformationAuthors = new ScientificPaperMetaInformationAuthors(); 
            
            scientificPaperMetaInformationAuthors.setID(rs.getInt("ID"));
            scientificPaperMetaInformationAuthors.setGivenName(rs.getString("SurName"));
            scientificPaperMetaInformationAuthors.setSurName(rs.getString("GivenName"));
            scientificPaperMetaInformationAuthors.setScientific_Paper_Meta_Information_ID(rs.getInt("Scientific_Paper_Meta_Information_ID"));
            scientificPaperMetaInformationAuthorsList.add(scientificPaperMetaInformationAuthors);
        }

        st.close();
        rs.close();
        conn.close();

        return scientificPaperMetaInformationAuthorsList;

    }
  
}
