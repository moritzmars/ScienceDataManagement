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
 * This class provides logic and data for the search definition web site. 
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

    /**
     * Returns the loaded system instances.
     * @return the loaded system instances.
     */
    public Collection getLoadedSystemInstances() {
        return loadedSystemInstances;
    }

    /**
     * Returns the loaded search definitions.
     * @return the loaded search definitions. 
     */
    public Collection getLoadedSearchDefinitions() {
        return loadedSearchDefinitions;
    }

    /**
     * Returns the application configuration.
     * @return the application configuration. 
     */
    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    /**
     * Returns the loaded search terms. 
     * @return the loaded search terms. 
     */
    public Collection getLoadedSearchTerms() {
        return loadedSearchTerms;
    }

    /**
     * Returns the selected search definition. 
     * @param the selected search definition. 
     */
    public void setSelectedSearchDefinition(String selectedSearchDefinition) {
        this.selectedSearchDefinition = selectedSearchDefinition;
    }

    /**
     * Returns the selected search definition.
     * @return the selected search definition. 
     */
    public String getSelectedSearchDefinition() {
        return selectedSearchDefinition;
    }

    /**
     * Sets the selected system instances
     * @param the selected system instances. 
     */
    public void setSelectedSystemInstances(Collection<String> selectedSystemInstances) {
        this.selectedSystemInstances = selectedSystemInstances;
    }

    /**
     * Returns the selected system instances. 
     * @return the selected system instances. 
     */
    public Collection<String> getSelectedSystemInstances() {
        return selectedSystemInstances;
    }

    /**
     * Loads the tables with the data. 
     */
    public void loadTable() {
        this.loadedSearchTerms = getSearchTermsBySearchDefinition();
        this.loadedSelectedSearchDefinitionExecutionList = getSearchExecutionDefinitionListBySearchDefinition();
    }

    /**
     * This method is executed after page call. 
     * @param the object about the onload call. 
     */
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

    /**
     * Returns the selected search definition execution list.
     * @return the selected search definition execution list. 
     */
    public LinkedList<SearchDefinitonExecution> getLoadedSelectedSearchDefinitionExecutionList() {
        return loadedSelectedSearchDefinitionExecutionList;
    }

    /**
     * Returns the execution definition list. 
     * @return the execution definition list.
     */
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

    /**
     * Returns the search terms by search definition. 
     * @return the search terms by search definition.
     */
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

    /**
     * Returns the system instances. 
     * @return the system instances. 
     */
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

    /**
     * Returns the search definitions. 
     * @return the search definitions. 
     */
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

    /**
     * Redirects to the edit page. 
     */
    public void redirectToEditPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("SearchDefinitionEdit.xhtml?SelectedSearchDefinition=" + selectedSearchDefinition);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * Deletes the search definition by id. 
     */
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
