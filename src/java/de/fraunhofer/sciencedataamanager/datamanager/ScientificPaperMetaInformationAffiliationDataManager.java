/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformation;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformationAffiliation;
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
public class ScientificPaperMetaInformationAffiliationDataManager {

    private ApplicationConfiguration applicationConfiguration;

    /**
     *
     * @param applicationConfiguration
     */
    public ScientificPaperMetaInformationAffiliationDataManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    /**
     *
     * @param scientificPaperMetaInformation
     * @throws Exception
     */
    public void insertAffiliation(ScientificPaperMetaInformation scientificPaperMetaInformation) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());

        for (ScientificPaperMetaInformationAffiliation scientificPaperMetaInformationAffiliation : scientificPaperMetaInformation.getAffiliation()) {

            String sqlInsertStatement = "INSERT INTO scientific_paper_meta_information_affiliation (Identifier_1, Url, Name, Country, City, Author_Count, Document_Count, scientific_paper_meta_information_ID)"
                    + " VALUES (?,?,?,?,?,?,?,?)";

            java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
            preparedStatement.setString(1, scientificPaperMetaInformationAffiliation.getIdentifier_1());
            preparedStatement.setString(2, scientificPaperMetaInformationAffiliation.getUrl());
            preparedStatement.setString(3, scientificPaperMetaInformationAffiliation.getName());
            preparedStatement.setString(4, scientificPaperMetaInformationAffiliation.getCountry());
            preparedStatement.setString(5, scientificPaperMetaInformationAffiliation.getCity());
            preparedStatement.setInt(6, scientificPaperMetaInformationAffiliation.getAuthor_Count());
            preparedStatement.setInt(7, scientificPaperMetaInformationAffiliation.getDocument_Count());
            preparedStatement.setInt(8, scientificPaperMetaInformation.getID());

            preparedStatement.execute();
            preparedStatement.close();
        }
        conn.close();
    }

    /**
     *
     * @param scientificPaperMetaInformation
     * @return
     * @throws Exception
     */
    public LinkedList<ScientificPaperMetaInformationAffiliation> getAffiliationByScientificMetaInformation(ScientificPaperMetaInformation scientificPaperMetaInformation) throws Exception {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM scientific_paper_meta_information_affiliation where Scientific_Paper_Meta_Information_ID=" + scientificPaperMetaInformation.getID();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        LinkedList<ScientificPaperMetaInformationAffiliation> scientificPaperMetaInformationAffiliationList = new LinkedList<ScientificPaperMetaInformationAffiliation>();

        while (rs.next()) {
            ScientificPaperMetaInformationAffiliation scientificPaperMetaInformationAffiliation = new ScientificPaperMetaInformationAffiliation();

            scientificPaperMetaInformation.setID(rs.getInt("ID"));
            scientificPaperMetaInformationAffiliation.setIdentifier_1(rs.getString("Identifier_1"));
            scientificPaperMetaInformationAffiliation.setUrl(rs.getString("Url"));
            scientificPaperMetaInformationAffiliation.setName(rs.getString("Name"));
            scientificPaperMetaInformationAffiliation.setCountry(rs.getString("Country"));
            scientificPaperMetaInformationAffiliation.setCity(rs.getString("City"));
            scientificPaperMetaInformationAffiliation.setAuthor_Count(rs.getInt("Author_Count"));
            scientificPaperMetaInformationAffiliation.setDocument_Count(rs.getInt("Document_Count"));
            scientificPaperMetaInformationAffiliation.setScientific_Paper_Meta_Information_ID(rs.getInt("scientific_paper_meta_information_ID"));
            scientificPaperMetaInformationAffiliationList.add(scientificPaperMetaInformationAffiliation);
        }

        st.close();
        rs.close();
        conn.close();

        return scientificPaperMetaInformationAffiliationList;

    }

}
