/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionDataManager;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchHistory")
@SessionScoped
public class SearchHistoryManagement {

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();
    private LinkedList<SearchDefinitonExecution> loadedSearchDefinitionExecutions;

    /**
     * This method provides logic and data for the search history web site. 
     * @return
     */
    public LinkedList<SearchDefinitonExecution> getLoadedSearchDefinitionExecutions() {
        return loadedSearchDefinitionExecutions;
    }

    /**
     * This method is called after the page is loaded. 
     * @param has information about the page load. 
     */
    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadedSearchDefinitionExecutions = getAllSearchDefinitionExecutions();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *Returns all search definition executions. 
     * @return the search definitions execution. 
     * @throws Exception during load from database. 
     */
    public LinkedList<SearchDefinitonExecution> getAllSearchDefinitionExecutions() throws Exception {
        SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
        return searchDefinitonExecutionDataProvider.getAllSearchDefinitionExecution();
    }

}
