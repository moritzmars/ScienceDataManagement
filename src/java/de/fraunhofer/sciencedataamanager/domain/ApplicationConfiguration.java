/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

import de.fraunhofer.sciencedataamanager.interfaces.ILoggingManager;

/**
 *
 * @author Moritz Mars
 */
public class ApplicationConfiguration {
 
    private String sqlConnection; 
    private ILoggingManager loggingManager; 

    public ILoggingManager getLoggingManager() {
        return loggingManager;
    }

    public void setLoggingManager(ILoggingManager loggingManager) {
        this.loggingManager = loggingManager;
    }
    
    public String getSqlConnection() {
        return sqlConnection;
    }

    public void setSqlConnection(String sqlConnection) {
        this.sqlConnection = sqlConnection;
    }
    
}
