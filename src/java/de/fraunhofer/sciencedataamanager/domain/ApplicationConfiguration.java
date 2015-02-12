/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

/**
 *
 * @author Moritz Mars
 */
public class ApplicationConfiguration {
 
    private String sqlConnection; 
    
    public String getSqlConnection() {
        return sqlConnection;
    }

    public void setSqlConnection(String sqlConnection) {
        this.sqlConnection = sqlConnection;
    }
    
}
