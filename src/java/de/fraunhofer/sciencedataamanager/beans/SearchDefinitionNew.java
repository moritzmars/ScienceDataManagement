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
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDatabaseManager;
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
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchDefinitionNew")
@SessionScoped
public class SearchDefinitionNew {

    private String searchDefinitionName;

    private String tempSearhTerm;
    private String tempOperation;

    public LinkedList<SystemInstance> getSelectedSystemInstanceList() {
        return selectedSystemInstanceList;
    }

    public void setSelectedSystemInstanceList(LinkedList<SystemInstance> selectedSystemInstanceList) {
        this.selectedSystemInstanceList = selectedSystemInstanceList;
    }

    public LinkedList<SystemInstance> getSystemInstanceList() {
        return systemInstanceList;
    }

    public void setSystemInstanceList(LinkedList<SystemInstance> systemInstanceList) {
        this.systemInstanceList = systemInstanceList;
    }
    private LinkedList<SystemInstance> selectedSystemInstanceList;
    private LinkedList<SystemInstance> systemInstanceList;

    private LinkedList<SearchTerm> tempSeachTerms = new LinkedList<SearchTerm>();
    private Collection<String> selectedSystemInstances = new LinkedList();
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    public Collection getTempSeachTerms() {
        return tempSeachTerms;
    }

    public void setTempSeachTerms(LinkedList<SearchTerm> tempSeachTerms) {
        this.tempSeachTerms = tempSeachTerms;
    }

    public String getSearchDefinitionName() {
        return searchDefinitionName;
    }

    public void setSearchDefinitionName(String searchDefinitionName) {
        this.searchDefinitionName = searchDefinitionName;
    }

    public String getTempSearhTerm() {
        return tempSearhTerm;
    }

    public void setTempSearhTerm(String tempSearhTerm) {
        this.tempSearhTerm = tempSearhTerm;
    }

    public String getTempOperation() {
        return tempOperation;
    }

    public void setTempOperation(String tempOperation) {
        this.tempOperation = tempOperation;
    }

    public Collection<String> getSelectedSystemInstances() {
        return selectedSystemInstances;
    }

    public void setSelectedSystemInstances(Collection<String> selectedSystemInstances) {
        this.selectedSystemInstances = selectedSystemInstances;
    }

    public void addTempSearchTerm() {
        SearchTerm searchTerm = new SearchTerm();
        searchTerm.setOperation(tempOperation);
        searchTerm.setTerm(tempSearhTerm);
        tempSeachTerms.add(searchTerm);
    }

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

    public void redirectBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

}
