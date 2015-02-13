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
import de.fraunhofer.sciencedataamanager.datamanager.SearchAnalyticDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import de.fraunhofer.sciencedataamanager.domain.SearchAnalyticDefinition;
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
@ManagedBean(name = "searchAnalyticDefinitionNew")
@SessionScoped
public class SearchAnalyticDefinitionNew {

    private String searchAnalyticsDefinitionName;
    private String searchAnalyticsDefinitionQuery;

    public String getSearchAnalyticsDefinitionQuery() {
        return searchAnalyticsDefinitionQuery;
    }

    public void setSearchAnalyticsDefinitionQuery(String searchAnalyticsDefinitionQuery) {
        this.searchAnalyticsDefinitionQuery = searchAnalyticsDefinitionQuery;
    }
    
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    public String getSearchAnalyticsDefinitionName() {
        return searchAnalyticsDefinitionName;
    }

    public void setSearchAnalyticsDefinitionName(String searchAnalyticsDefinitionName) {
        this.searchAnalyticsDefinitionName = searchAnalyticsDefinitionName;
    }

    public void saveSearchDefinition() {
        try {
            SearchAnalyticDefinition searchAnalyticDefinition = new SearchAnalyticDefinition(); 
            searchAnalyticDefinition.setName(searchAnalyticsDefinitionName);
            searchAnalyticDefinition.setQuery(searchAnalyticsDefinitionQuery);
            SearchAnalyticDefinitionDataManager searchAnalyticDefinitionDataManager = new SearchAnalyticDefinitionDataManager(applicationConfiguration); 
            searchAnalyticDefinitionDataManager.addSearchAnalyticDefinition(searchAnalyticDefinition);
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

    public void onLoad(ComponentSystemEvent event) {

        try {

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
