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

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchDefinitionEdit")
@SessionScoped
public class SearchDefinitionEdit {

    private int searchDefinitionID;
    private String searchDefinitionName;
    private LinkedList<SystemInstance> selectedSystemInstanceList; 
    private LinkedList<SystemInstance> systemInstanceList; 
       private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration(); 
 
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
    public void setCurrentTerm(String currentTerm) {
        this.currentTerm = currentTerm;
    }

    public void setCurrentOperation(String currentOperation) {
        this.currentOperation = currentOperation;
    }
    private String currentTerm;

    public String getCurrentTerm() {
        return currentTerm;
    }

    public String getCurrentOperation() {
        return currentOperation;
    }
    private String currentOperation;

    public void setSearchDefinitionName(String searchDefinitionName) {
        this.searchDefinitionName = searchDefinitionName;
    }

    public String getSearchDefinitionName() {
        return searchDefinitionName;
    }

    public void onLoad(ComponentSystemEvent event) {

        try {
            if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SelectedSearchDefinition") == null) {
                return;
            }
            searchDefinitionID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SelectedSearchDefinition"));
            this.setSelectedSystemInstanceList(new LinkedList<>());
            this.setSystemInstanceList(new LinkedList<>());
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            SearchDefinition searchDefinition = searchDefinitionDataProvider.getSearchDefinitionByID(searchDefinitionID);
            this.setSearchDefinitionName(searchDefinition.getName());
            this.getSystemInstanceList().addAll(this.getSystemInstances());
            this.getSelectedSystemInstanceList().addAll(getSearchExecutionBySearchDefinition().getSystemInstances());
        } catch (Exception ex) {
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);
          
        }
    }

    public void updateSearchDefinition() {
        try {

            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);
            searchDefinition.setName(this.getSearchDefinitionName());

            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            searchDefinitionDataProvider.updateSearchDefinition(searchDefinition);
              
            
            SearchExecution searchExecution = new SearchExecution(); 
            searchExecution.setSearchDefiniton(searchDefinition);
            searchExecution.setSystemInstances(this.getSelectedSystemInstanceList());
            SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration); 
            searchExecutionDataProvider.deleteSystemInstancesBySearchExecution(searchExecution);
            searchExecutionDataProvider.saveSearchExecution(searchExecution);
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
     
            this.applicationConfiguration.getLoggingManager().logException(ex);
        }

    }
    
      public SearchExecution getSearchExecutionBySearchDefinition() {
      
          SearchExecution searchExecution = null;
       SearchDefinition searchDefinition = new SearchDefinition(); 
       searchDefinition.setID(searchDefinitionID);
        try {
            SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration); 
            searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);
      
        } catch (Exception ex) {
             Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);
           
        }
        return searchExecution;
    }
    
    public Collection getSearchTermsBySearchExecutionDefinition() {
        Collection searchTerms = null;

        try {
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            searchTerms = searchDefinitionDataProvider.getSearchDefinitionByID(searchDefinitionID).getSearchTerms();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);
            
        }
        return searchTerms;
    }

    public void addSearchTerm() {
        try {

            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);

            SearchTerm searchTerm = new SearchTerm();
            searchTerm.setOperation(currentOperation);
            searchTerm.setTerm(currentTerm);

            searchDefinition.getSearchTerms().add(searchTerm);

            SearchTermDataManager searchTermDataProvider = new SearchTermDataManager(applicationConfiguration);
            searchTermDataProvider.insertSearchTerms(searchDefinition);

            FacesContext.getCurrentInstance().getExternalContext().redirect("SearchDefinitionEdit.xhtml?SelectedSearchDefinition=" + searchDefinitionID);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);
            
        }

    }

    public void deleteSearchTerm(SearchTerm searchTerm) {
        try {
            SearchTermDataManager searchTermDataProvider = new SearchTermDataManager(applicationConfiguration);
            searchTermDataProvider.deleteSearchTerm(searchTerm);
        } catch (Exception ex) {
             Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);
           
        }
    }
    
        public Collection<SystemInstance> getSystemInstances() {
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
        
            public void redirectBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

}
