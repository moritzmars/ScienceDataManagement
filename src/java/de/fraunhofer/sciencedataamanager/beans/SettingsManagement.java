/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationPersistanceDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDatabaseManager;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "settings")
@ViewScoped
public class SettingsManagement implements java.io.Serializable {

    private String sqlConnectionString;

    public String getSqlConnectionString() {
        try {
            ApplicationConfigurationPersistanceDataManager applicationConfigurationPersistanceDataProvider = new ApplicationConfigurationPersistanceDataManager();
            ApplicationConfiguration applicationConfiguration = applicationConfigurationPersistanceDataProvider.getApplicationConfiguration();
            sqlConnectionString = applicationConfiguration.getSqlConnection();
            return sqlConnectionString;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public void setSqlConnectionString(String sqlConnectionString) {
        this.sqlConnectionString = sqlConnectionString;
    }

    public void saveSettings() {
        try {
            ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
            applicationConfiguration.setSqlConnection(sqlConnectionString);
            ApplicationConfigurationPersistanceDataManager applicationConfigurationPersistanceDataProvider = new ApplicationConfigurationPersistanceDataManager();
            applicationConfigurationPersistanceDataProvider.setApplicationConfiguration(applicationConfiguration);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
