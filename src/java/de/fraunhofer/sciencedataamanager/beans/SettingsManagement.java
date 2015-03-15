/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationPersistanceDataManager;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.ApplicationLogMonitoringLevel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 * The class provides logic and data for the settings management page. 
 * @author Moritz Mars
 */
@ManagedBean(name = "settings")
@ViewScoped
public class SettingsManagement implements java.io.Serializable {

    private String sqlConnectionString;
    private ApplicationLogMonitoringLevel selectedApplicationLogMonitoringLevel;

    /**
     * Returns the application monitoring level. 
     * @return the application monitoring level. 
     */
    public ApplicationLogMonitoringLevel[] getApplicationLogMonitoringLevel() {
        return ApplicationLogMonitoringLevel.values();
    }

    /**
     * Return the SQL Connection string. 
     * @return the sql connection string. 
     */
    public String getSqlConnectionString() {
        return sqlConnectionString;
    }

    /**
     * Sets the sql connection string. 
     * @param the sql connection string. 
     */
    public void setSqlConnectionString(String sqlConnectionString) {
        this.sqlConnectionString = sqlConnectionString;
    }

    /**
     * Sets the application log monitoring level. 
     * @return the application log monitoring level. 
     */
    public ApplicationLogMonitoringLevel getSelectedApplicationLogMonitoringLevel() {
        return selectedApplicationLogMonitoringLevel;
    }

    /**
     * Sets the selected application log monitoring level.
     * @param the application log monitoring level. 
     */
    public void setSelectedApplicationLogMonitoringLevel(ApplicationLogMonitoringLevel selectedApplicationLogMonitoringLevel) {
        this.selectedApplicationLogMonitoringLevel = selectedApplicationLogMonitoringLevel;
    }

    /**
     * The method is executed after the page is loaded. 
     * @param object holds information about the page load. 
     */
    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) 
                return;
                ApplicationConfigurationPersistanceDataManager applicationConfigurationPersistanceDataProvider = new ApplicationConfigurationPersistanceDataManager();
                ApplicationConfiguration applicationConfiguration = applicationConfigurationPersistanceDataProvider.getApplicationConfiguration();
                sqlConnectionString = applicationConfiguration.getSqlConnection();
                this.selectedApplicationLogMonitoringLevel = applicationConfiguration.getApplicationLogMonitoringLevel();
            
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
        }
    }

    /**
     *This method save the settings. 
     */
    public void saveSettings() {
        try {
            ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
            applicationConfiguration.setSqlConnection(sqlConnectionString);
            applicationConfiguration.setApplicationLogMonitoringLevel(this.selectedApplicationLogMonitoringLevel);
            ApplicationConfigurationPersistanceDataManager applicationConfigurationPersistanceDataProvider = new ApplicationConfigurationPersistanceDataManager();
            applicationConfigurationPersistanceDataProvider.setApplicationConfiguration(applicationConfiguration);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
