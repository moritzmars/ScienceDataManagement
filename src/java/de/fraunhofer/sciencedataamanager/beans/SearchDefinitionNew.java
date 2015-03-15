/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchExecution;
import de.fraunhofer.sciencedataamanager.domain.SearchTerm;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 * The class provides logic and data for the search definition new web site. 
 * @author Moritz Mars
 */
@ManagedBean(name = "searchDefinitionNew")
@SessionScoped
public class SearchDefinitionNew {

    private String searchDefinitionName;

    private String tempSearhTerm;
    private String tempOperation;

    /**
     * Returns the selected system instance list. 
     * @return the selected system instance list. 
     */
    public LinkedList<SystemInstance> getSelectedSystemInstanceList() {
        return selectedSystemInstanceList;
    }

    /**
     * Returns the selected system instance list. 
     * @param selectedSystemInstanceList the selected system instance list. 
     */
    public void setSelectedSystemInstanceList(LinkedList<SystemInstance> selectedSystemInstanceList) {
        this.selectedSystemInstanceList = selectedSystemInstanceList;
    }

    /**
     * Returns the system instance list. 
     * @return the system instance list. 
     */
    public LinkedList<SystemInstance> getSystemInstanceList() {
        return systemInstanceList;
    }

    /**
     * Sets the system instance list. 
     * @param systemInstanceList the system instance list. 
     */
    public void setSystemInstanceList(LinkedList<SystemInstance> systemInstanceList) {
        this.systemInstanceList = systemInstanceList;
    }
    private LinkedList<SystemInstance> selectedSystemInstanceList;
    private LinkedList<SystemInstance> systemInstanceList;

    private LinkedList<SearchTerm> tempSeachTerms = new LinkedList<SearchTerm>();
    private Collection<String> selectedSystemInstances = new LinkedList();
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Returns the temp search terms. 
     * @return the temp search terms.
     */
    public Collection getTempSeachTerms() {
        return tempSeachTerms;
    }

    /**
     * Sets the temp search terms. 
     * @param tempSeachTerms the temp search terms. 
     */
    public void setTempSeachTerms(LinkedList<SearchTerm> tempSeachTerms) {
        this.tempSeachTerms = tempSeachTerms;
    }

    /**
     * Returns the search definition name. 
     * @return the search definition name. 
     */
    public String getSearchDefinitionName() {
        return searchDefinitionName;
    }

    /**
     * Sets the search definition name.
     * @param searchDefinitionName the search definition name. 
     */
    public void setSearchDefinitionName(String searchDefinitionName) {
        this.searchDefinitionName = searchDefinitionName;
    }

    /**
     * Returns the temp search term. 
     * @return the temp search term. 
     */
    public String getTempSearhTerm() {
        return tempSearhTerm;
    }

    /**
     * Sets the temp search term.
     * @param tempSearhTerm the temp search term. 
     */
    public void setTempSearhTerm(String tempSearhTerm) {
        this.tempSearhTerm = tempSearhTerm;
    }

    /**
     * Returns the temp operation. 
     * @return the temp operation. 
     */
    public String getTempOperation() {
        return tempOperation;
    }

    /**
     * Sets the temp operation. 
     * @param tempOperation the temp operation. 
     */
    public void setTempOperation(String tempOperation) {
        this.tempOperation = tempOperation;
    }

    /**
     * Returns the selected system instances. 
     * @return the selected system instances.  
     */
    public Collection<String> getSelectedSystemInstances() {
        return selectedSystemInstances;
    }

    /**
     * Sets the selected system instances. 
     * @param selectedSystemInstances the selected system instances. 
     */
    public void setSelectedSystemInstances(Collection<String> selectedSystemInstances) {
        this.selectedSystemInstances = selectedSystemInstances;
    }

    /**
     * This method adds a temp search term.
     */
    public void addTempSearchTerm() {
        SearchTerm searchTerm = new SearchTerm();
        searchTerm.setOperation(tempOperation);
        searchTerm.setTerm(tempSearhTerm);
        tempSeachTerms.add(searchTerm);
    }

    /**
     * This method save the search definition. 
     */
    public void saveSearchDefinition() {
        try {
            SearchExecution searchExecution = new SearchExecution();
            searchExecution.getSystemInstances().addAll(this.getSelectedSystemInstanceList());

            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setName(searchDefinitionName);
            searchDefinition.setSearchTerms(tempSeachTerms);
            searchExecution.setSearchDefiniton(searchDefinition);

            searchDefinitionDataProvider.addSearchDefinition(searchExecution);
            searchDefinitionName = "";
            tempSearhTerm = "";
            tempOperation = "";

            tempSeachTerms = new LinkedList<SearchTerm>();
            selectedSystemInstances = new LinkedList();

            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return systemInstances;
    }

    /**
     * This event is executed after the page is loaded. 
     * @param event Informations about the page load event. 
     */
    public void onLoad(ComponentSystemEvent event) {

        try {

            this.setSelectedSystemInstanceList(new LinkedList<>());
            this.setSystemInstanceList(new LinkedList<>());
            this.getSystemInstanceList().addAll(this.getSystemInstances());
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * This method performs a redirect to the index page. 
     */
    public void redirectBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

}
