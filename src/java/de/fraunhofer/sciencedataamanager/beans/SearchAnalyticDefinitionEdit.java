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
 * Provides a the bean for search analytic definition web site. 
 * @author Moritz Mars
 */
@ManagedBean(name = "searchAnalyticDefinitionEdit")
@SessionScoped
public class SearchAnalyticDefinitionEdit {

    private int searchAnalyticDefinitionID;

    private String searchAnalyticsDefinitionName;
    private String searchAnalyticsDefinitionQuery;

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Returns the search analytic definition name. 
     * @return the search analytic definition name. 
     */
    public String getSearchAnalyticsDefinitionName() {
        return searchAnalyticsDefinitionName;
    }

    /**
     * Sets the search analytic definition name
     * @param searchAnalyticsDefinitionName the search analytic definition name
     */
    public void setSearchAnalyticsDefinitionName(String searchAnalyticsDefinitionName) {
        this.searchAnalyticsDefinitionName = searchAnalyticsDefinitionName;
    }

    /**
     * Returns the search analytic definition query.
     * @return the search analytic definition query. 
     */
    public String getSearchAnalyticsDefinitionQuery() {
        return searchAnalyticsDefinitionQuery;
    }

    /**
     * Sets the search analytics definition query. 
     * @param searchAnalyticsDefinitionQuery The search analytics definition query. 
     */
    public void setSearchAnalyticsDefinitionQuery(String searchAnalyticsDefinitionQuery) {
        this.searchAnalyticsDefinitionQuery = searchAnalyticsDefinitionQuery;
    }

    /**
     * The event is executed after the page is loaded. 
     * @param event Page load event object.
     */
    public void onLoad(ComponentSystemEvent event) {

        try {
            if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SelectedSearchAnalyticDefinition") == null) {
                return;
            }
            searchAnalyticDefinitionID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SelectedSearchAnalyticDefinition"));
            SearchAnalyticDefinitionDataManager searchAnalyticDefinitionDataManager = new SearchAnalyticDefinitionDataManager(applicationConfiguration);
            SearchAnalyticDefinition searchAnalyticDefinition = searchAnalyticDefinitionDataManager.getSearchAnalyticDefinitionByID(searchAnalyticDefinitionID);
            this.searchAnalyticsDefinitionName = searchAnalyticDefinition.getName();
            this.searchAnalyticsDefinitionQuery = searchAnalyticDefinition.getQuery();
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * The method updates the search definition. 
     */
    public void updateSearchDefinition() {
        try {

            SearchAnalyticDefinition searchAnalyticDefinition = new SearchAnalyticDefinition();
            searchAnalyticDefinition.setID(searchAnalyticDefinitionID);
            searchAnalyticDefinition.setName(searchAnalyticsDefinitionName);
            searchAnalyticDefinition.setQuery(searchAnalyticsDefinitionQuery);
            
            
            SearchAnalyticDefinitionDataManager searchAnalyticDefinitionDataManager = new SearchAnalyticDefinitionDataManager(applicationConfiguration);
            searchAnalyticDefinitionDataManager.updateSearchAnalyticDefinition(searchAnalyticDefinition);

            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);

            this.applicationConfiguration.getLoggingManager().logException(ex);
        }

    }

    /**
     * The method executed a redirect to the index page. 
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
