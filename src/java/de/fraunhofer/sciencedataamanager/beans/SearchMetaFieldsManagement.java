/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.SearchFieldMappingManager;
import de.fraunhofer.sciencedataamanager.domain.SearchFieldMapping;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 * This class provides logic and data for the system instance management.
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchMetaFieldsManagement")
@SessionScoped
public class SearchMetaFieldsManagement {

    private LinkedList<SearchFieldMapping> searchMetaFields;
    private String selectedField;

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * This method is executed after the page is loaded.
     *
     * @param event objects has information about the page load.
     */
    public void onLoad(ComponentSystemEvent event) {
        try
        {
            if (FacesContext.getCurrentInstance().isPostback())
            {
                return;
            }
            SearchFieldMappingManager searchFieldMappingManager = new SearchFieldMappingManager(this.applicationConfiguration);
            this.setSearchMetaFields(searchFieldMappingManager.getFieldMappings());
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addMetaSearchField() {
        try
        {
            SearchFieldMapping searchFieldMapping = new SearchFieldMapping();
            searchFieldMapping.setMetaFieldName(selectedField);
            SearchFieldMappingManager searchFieldMappingManager = new SearchFieldMappingManager(this.applicationConfiguration);
            searchFieldMappingManager.insertMetaSearchField(searchFieldMapping);
             searchMetaFields.add(searchFieldMapping);
  
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    public LinkedList<SearchFieldMapping> getSearchMetaFields() {
        return searchMetaFields;
    }

    public void setSearchMetaFields(LinkedList<SearchFieldMapping> searchMetaFields) {
        this.searchMetaFields = searchMetaFields;
    }

    public String getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(String selectedField) {
        this.selectedField = selectedField;
    }

    public void deleteMetaField(SearchFieldMapping searchFieldMapping) {
        try
        {
            SearchFieldMappingManager searchFieldMappingManager = new SearchFieldMappingManager(this.applicationConfiguration);
            searchFieldMappingManager.deleteSearchFieldMappings(searchFieldMapping);
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

    /**
     * Execute a redirect to the edit page.
     */
    public void redirectToEditPage() {
        try
        {
            //FacesContext.getCurrentInstance().getExternalContext().redirect("SystemInstanceEdit.xhtml?SystemInstanceID=" + selectedItem);
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

}
