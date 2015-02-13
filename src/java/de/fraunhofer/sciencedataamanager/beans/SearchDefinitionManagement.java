/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDatabaseManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import java.util.Collection;
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
@ManagedBean(name = "searchDefinitionManagement")
@SessionScoped
public class SearchDefinitionManagement {

    private Collection<String> selectedSystemInstances = new LinkedList();
    private String selectedSearchDefinition;
    private LinkedList<SearchDefinitonExecution> loadedSelectedSearchDefinitionExecutionList;
    private Collection loadedSearchTerms;
    private Collection loadedSearchDefinitions;
    private Collection loadedSystemInstances;

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    public Collection getLoadedSystemInstances() {
        return loadedSystemInstances;
    }

    public Collection getLoadedSearchDefinitions() {
        return loadedSearchDefinitions;
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public Collection getLoadedSearchTerms() {
        return loadedSearchTerms;
    }

    public void setSelectedSearchDefinition(String selectedSearchDefinition) {
        this.selectedSearchDefinition = selectedSearchDefinition;
    }

    public String getSelectedSearchDefinition() {
        return selectedSearchDefinition;
    }

    public void setSelectedSystemInstances(Collection<String> selectedSystemInstances) {
        this.selectedSystemInstances = selectedSystemInstances;
    }

    public Collection<String> getSelectedSystemInstances() {
        return selectedSystemInstances;
    }

    public void loadTable() {
        this.loadedSearchTerms = getSearchTermsBySearchDefinition();
        this.loadedSelectedSearchDefinitionExecutionList = getSearchExecutionDefinitionListBySearchDefinition();
    }

    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadedSearchDefinitions = getSearchDefinitions();
            this.loadedSystemInstances = getSystemInstances();

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedList<SearchDefinitonExecution> getLoadedSelectedSearchDefinitionExecutionList() {
        return loadedSelectedSearchDefinitionExecutionList;
    }

    public LinkedList<SearchDefinitonExecution> getSearchExecutionDefinitionListBySearchDefinition() {
        if (selectedSearchDefinition == null || "".equals(selectedSearchDefinition)) {
            return null;
        }
        int searchDefinitionID = Integer.parseInt(selectedSearchDefinition);
        LinkedList<SearchDefinitonExecution> searchExecutionList = null;
        try {
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);
            SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
            searchExecutionList = searchDefinitonExecutionDataProvider.getAllSearchDefinitionExecutionForSearchDefinition(searchDefinition);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchExecutionList;
    }

    public Collection getSearchTermsBySearchDefinition() {
        if (selectedSearchDefinition == null || "".equals(selectedSearchDefinition)) {
            return null;
        }
        int searchDefinitionID = Integer.parseInt(selectedSearchDefinition);
        Collection searchTerms = null;

        try {
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            searchTerms = searchDefinitionDataProvider.getSearchDefinitionByID(searchDefinitionID).getSearchTerms();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchTerms;
    }

    public Collection getSystemInstances() {

        Collection systemInstances = null;
        try {
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstances = systemInstanceDataProvider.getSystemInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return systemInstances;
    }

    public Collection getSearchDefinitions() {
        Collection searchDefinitions = null;
        try {
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            searchDefinitions = searchDefinitionDataProvider.getSearchDefinitions();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchDefinitions;
    }

    public void redirectToEditPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("SearchDefinitionEdit.xhtml?SelectedSearchDefinition=" + selectedSearchDefinition);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    public void deleteSearchDefinitionByID() {
        if (selectedSearchDefinition == null || "".equals(selectedSearchDefinition)) {
            return;
        }
        int id = Integer.parseInt(selectedSearchDefinition);

        try {
            SearchDefinitionDataManager searchDefinitionDataManager = new SearchDefinitionDataManager(applicationConfiguration);
            searchDefinitionDataManager.delete(id);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }
}
