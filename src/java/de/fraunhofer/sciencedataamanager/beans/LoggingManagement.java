/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDatabaseManager;
import de.fraunhofer.sciencedataamanager.domain.LoggingEntry;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 * This class provides a overview about current log entries. 
 * @author Moritz Mars
 */
@ManagedBean(name = "logging")
@ViewScoped
public class LoggingManagement {

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();
    private LinkedList<LoggingEntry> loadedLoggingEntries;

    /**
     * Returns the Logging Entries
     * @return loaded Linked List with LoggingEntries.
     */
    public LinkedList<LoggingEntry> getLoadedLoggingEntries() {
        return loadedLoggingEntries;
    }

    /**
     * The method is executed during first execution of the page and loads the initial data from database. 
     * @param event from the JSF Framework
     */
    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadedLoggingEntries = getLoggingEntries();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return the Logging Entries
     * @return Linked List with Logging Entries
     * @throws Exception
     */
    public LinkedList<LoggingEntry> getLoggingEntries() throws Exception {
        LoggingDatabaseManager loggingDataProvider = new LoggingDatabaseManager(applicationConfiguration);
        return loggingDataProvider.getLoggingEntries();
    }
}
