/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDataManager;
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
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "logging")
@ViewScoped
public class LoggingManagement {

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();
    private LinkedList<LoggingEntry> loadedLoggingEntries;

    public LinkedList<LoggingEntry> getLoadedLoggingEntries() {
        return loadedLoggingEntries;
    }

    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadedLoggingEntries = getLoggingEntries();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            LoggingDataManager.logException(ex, applicationConfiguration);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedList<LoggingEntry> getLoggingEntries() throws Exception {
        LoggingDataManager loggingDataProvider = new LoggingDataManager(applicationConfiguration);
        return loggingDataProvider.getLoggingEntries();
    }
}
