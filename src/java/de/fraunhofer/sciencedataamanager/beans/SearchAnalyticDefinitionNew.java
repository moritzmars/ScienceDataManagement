/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.SearchAnalyticDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.domain.SearchAnalyticDefinition;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 * Provides logic and data for the site search analytic new. 
 * @author Moritz Mars
 */
@ManagedBean(name = "searchAnalyticDefinitionNew")
@SessionScoped
public class SearchAnalyticDefinitionNew {

    private String searchAnalyticsDefinitionName;
    private String searchAnalyticsDefinitionQuery;

    /**
     * Returns the search analytic query.
     * @return the search analytic query. 
     */
    public String getSearchAnalyticsDefinitionQuery() {
        return searchAnalyticsDefinitionQuery;
    }

    /**
     * Sets the search analytic definition query. 
     * @param the search analytic definition query.
     */
    public void setSearchAnalyticsDefinitionQuery(String searchAnalyticsDefinitionQuery) {
        this.searchAnalyticsDefinitionQuery = searchAnalyticsDefinitionQuery;
    }
    
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Returns the search analytic definition name,
     * @return the searach analytic definition name. 
     */
    public String getSearchAnalyticsDefinitionName() {
        return searchAnalyticsDefinitionName;
    }

    /**
     * Sets the search analytic definition name.
     * @param the search analytic definition name.
     */
    public void setSearchAnalyticsDefinitionName(String searchAnalyticsDefinitionName) {
        this.searchAnalyticsDefinitionName = searchAnalyticsDefinitionName;
    }

    /**
     * The method save the search definition to the database. 
     */
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

    /**
     * The method is called after the page is loaded. 
     * @param the event of the page loaded. 
     */
    public void onLoad(ComponentSystemEvent event) {

        try {

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * Executed a redirect to the index page. 
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
