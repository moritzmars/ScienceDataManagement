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
import de.fraunhofer.sciencedataamanager.datamanager.SearchExecutionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchTermDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Provides logic and data for the search definition edit page.
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchDefinitionEdit")
@SessionScoped
public class SearchDefinitionEdit {

    private int searchDefinitionID;
    private String searchDefinitionName;
    private String searchDefinitionItemTreshhold;

    private String searchDefinitionExpertQuery;
    private String searchDefinitionQueryMode;
    private LinkedList<SystemInstance> selectedSystemInstanceList;
    private LinkedList<SystemInstance> systemInstanceList;
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();



    /**
     * Returns the selected system instance list.
     *
     * @return the selected system instance list.
     */
    public LinkedList<SystemInstance> getSelectedSystemInstanceList() {
        return selectedSystemInstanceList;
    }

    /**
     * Sets the selected system instance list.
     *
     * @param selectedSystemInstanceList the selected system instance list.
     */
    public void setSelectedSystemInstanceList(LinkedList<SystemInstance> selectedSystemInstanceList) {
        this.selectedSystemInstanceList = selectedSystemInstanceList;
    }

    /**
     * Returns the system instance list.
     *
     * @return the system instance list.
     */
    public LinkedList<SystemInstance> getSystemInstanceList() {
        return systemInstanceList;
    }

    /**
     * Sets the system instance list.
     *
     * @param systemInstanceList the system instance list.
     */
    public void setSystemInstanceList(LinkedList<SystemInstance> systemInstanceList) {
        this.systemInstanceList = systemInstanceList;
    }

    /**
     * Sets the current term.
     *
     * @param currentTerm the current term.
     */
    public void setCurrentTerm(String currentTerm) {
        this.currentTerm = currentTerm;
    }

    /**
     * Sets the current operation.
     *
     * @param currentOperation the current operation
     */
    public void setCurrentOperation(String currentOperation) {
        this.currentOperation = currentOperation;
    }
    private String currentTerm;

    /**
     * Returns the current term.
     *
     * @return the current term.
     */
    public String getCurrentTerm() {
        return currentTerm;
    }

    /**
     * Returns the current operation.
     *
     * @return the current operation.
     */
    public String getCurrentOperation() {
        return currentOperation;
    }
    private String currentOperation;

    /**
     * Sets the search definition name.
     *
     * @param searchDefinitionName the search definition name.
     */
    public void setSearchDefinitionName(String searchDefinitionName) {
        this.searchDefinitionName = searchDefinitionName;
    }

    /**
     * Returns the search definition name.
     *
     * @return the search definition name.
     */
    public String getSearchDefinitionName() {
        return searchDefinitionName;
    }

    /**
     * The method is executed after page load.
     *
     * @param event the information of the page load.
     */
    public void onLoad(ComponentSystemEvent event) {

        try
        {
            if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SelectedSearchDefinition") == null)
            {
                return;
            }
            searchDefinitionID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SelectedSearchDefinition"));
            this.setSelectedSystemInstanceList(new LinkedList());
            this.setSystemInstanceList(new LinkedList());
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            SearchDefinition searchDefinition = searchDefinitionDataProvider.getSearchDefinitionByID(searchDefinitionID);
            this.setSearchDefinitionName(searchDefinition.getName());

            this.setSearchDefinitionItemTreshhold(searchDefinition.getItemTreshhold() + "");
            this.setSearchDefinitionExpertQuery(StringEscapeUtils.unescapeJava(searchDefinition.getExpertQuery()));
            this.setSearchDefinitionQueryMode(searchDefinition.getSearchQueryMode());

            this.getSystemInstanceList().addAll(this.getSystemInstances());
            this.getSelectedSystemInstanceList().addAll(getSearchExecutionBySearchDefinition().getSystemInstances());
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * The methods updates the search definition.
     */
    public void updateSearchDefinition() {
        try
        {

            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);
            searchDefinition.setName(this.getSearchDefinitionName());
            
            searchDefinition.setExpertQuery(StringEscapeUtils.escapeJava(this.searchDefinitionExpertQuery));
            if (this.searchDefinitionItemTreshhold.matches("\\d+"))
            {
                searchDefinition.setItemTreshhold(Integer.parseInt(this.searchDefinitionItemTreshhold));
            }
            else
            {
                searchDefinition.setItemTreshhold(500);
            }

            searchDefinition.setSearchQueryMode(this.searchDefinitionQueryMode);
            
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            searchDefinitionDataProvider.updateSearchDefinition(searchDefinition);

            SearchExecution searchExecution = new SearchExecution();
            searchExecution.setSearchDefiniton(searchDefinition);
            searchExecution.setSystemInstances(this.getSelectedSystemInstanceList());
            SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
            searchExecutionDataProvider.deleteSystemInstancesBySearchExecution(searchExecution);
            searchExecutionDataProvider.saveSearchExecution(searchExecution);

            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);

            this.applicationConfiguration.getLoggingManager().logException(ex);
        }

    }

    /**
     * Returns the search execution.
     *
     * @return the search execution.
     */
    public SearchExecution getSearchExecutionBySearchDefinition() {

        SearchExecution searchExecution = null;
        SearchDefinition searchDefinition = new SearchDefinition();
        searchDefinition.setID(searchDefinitionID);
        try
        {
            SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
            searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);

        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchExecution;
    }

    /**
     * Returns the search terms.
     *
     * @return the search terms.
     */
    public Collection getSearchTermsBySearchExecutionDefinition() {
        Collection searchTerms = null;

        try
        {
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            searchTerms = searchDefinitionDataProvider.getSearchDefinitionByID(searchDefinitionID).getSearchTerms();
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchTerms;
    }

    /**
     * The method adds a new search term.
     */
    public void addSearchTerm() {
        try
        {

            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);

            SearchTerm searchTerm = new SearchTerm();
            searchTerm.setOperation(currentOperation);
            searchTerm.setTerm(currentTerm);

            searchDefinition.getSearchTerms().add(searchTerm);

            SearchTermDataManager searchTermDataProvider = new SearchTermDataManager(applicationConfiguration);
            searchTermDataProvider.insertSearchTerms(searchDefinition);

            FacesContext.getCurrentInstance().getExternalContext().redirect("SearchDefinitionEdit.xhtml?SelectedSearchDefinition=" + searchDefinitionID);
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

    /**
     * The method deletes a search term.
     *
     * @param the search term to delete.
     */
    public void deleteSearchTerm(SearchTerm searchTerm) {
        try
        {
            SearchTermDataManager searchTermDataProvider = new SearchTermDataManager(applicationConfiguration);
            searchTermDataProvider.deleteSearchTerm(searchTerm);
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * Returns the system instances.
     *
     * @return the system instances.
     */
    public Collection<SystemInstance> getSystemInstances() {
        Collection systemInstances = null;
        try
        {
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstances = systemInstanceDataProvider.getSystemInstances();
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return systemInstances;
    }

    /**
     * Executes a redirect to the index page.
     */
    public void redirectBack() {
        try
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }
        public String getSearchDefinitionItemTreshhold() {
        return searchDefinitionItemTreshhold;
    }

    public void setSearchDefinitionItemTreshhold(String searchDefinitionItemTreshhold) {
        this.searchDefinitionItemTreshhold = searchDefinitionItemTreshhold;
    }

    public String getSearchDefinitionExpertQuery() {
        return searchDefinitionExpertQuery;
    }

    public void setSearchDefinitionExpertQuery(String searchDefinitionExpertQuery) {
        this.searchDefinitionExpertQuery = searchDefinitionExpertQuery;
    }

    public String getSearchDefinitionQueryMode() {
        return searchDefinitionQueryMode;
    }

    public void setSearchDefinitionQueryMode(String searchDefinitionQueryMode) {
        this.searchDefinitionQueryMode = searchDefinitionQueryMode;
    }

}
