/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.SearchFieldMappingManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import de.fraunhofer.sciencedataamanager.domain.SearchFieldMapping;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * The class provides logic and data for the system instance edit page.
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "systemInstanceEdit")
@SessionScoped
public class SystemInstanceEdit {

    private String systemInstanceName;
    private String systemInstanceGroovyCode;
    private String selectedMetaField;
    private String selectedField;

    private int systemInstanceID;
    private LinkedList<SearchFieldMapping> searchFieldMappings;
    private LinkedList<SearchFieldMapping> searchMetaFields;

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Returns the system instance name.
     *
     * @return the system instance name.
     */
    public String getSystemInstanceName() {
        return systemInstanceName;
    }

    /**
     * Returns the system instance groovy code.
     *
     * @return the system instance groocy code.
     */
    public String getSystemInstanceGroovyCode() {
        return systemInstanceGroovyCode;
    }

    /**
     * Sets the system instance name.
     *
     * @param systemInstanceName the system instance name.
     */
    public void setSystemInstanceName(String systemInstanceName) {
        this.systemInstanceName = systemInstanceName;
    }

    /**
     * Sets the system instance groovy code.
     *
     * @param systemInstanceGroovyCode the system instance groovy code.
     */
    public void setSystemInstanceGroovyCode(String systemInstanceGroovyCode) {
        this.systemInstanceGroovyCode = systemInstanceGroovyCode;
    }

    public LinkedList<SearchFieldMapping> getSearchFieldMappings() {
        return searchFieldMappings;
    }

    public void setSearchFieldMappings(LinkedList<SearchFieldMapping> searchFieldMappings) {
        this.searchFieldMappings = searchFieldMappings;
    }

    public LinkedList<SearchFieldMapping> getSearchMetaFields() {
        return searchMetaFields;
    }

    public void setSearchMetaFields(LinkedList<SearchFieldMapping> searchMetaField) {
        this.searchMetaFields = searchMetaField;
    }

    public String getSelectedMetaField() {
        return selectedMetaField;
    }

    public void setSelectedMetaField(String selectedMetaField) {
        this.selectedMetaField = selectedMetaField;
    }

    public String getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(String selectedField) {
        this.selectedField = selectedField;
    }

    /**
     * This method is executed after the page is loaded.
     *
     * @param event informations about the page load.
     */
    public void onLoad(ComponentSystemEvent event) {
        try
        {
               if (!FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SystemInstanceID").matches("\\d+"))
            {
                return;
            }
            systemInstanceID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SystemInstanceID"));

            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            SystemInstance systemInstance = systemInstanceDataProvider.getSystemInstanceByID(systemInstanceID);
            SearchFieldMappingManager searchFieldMappingManager = new SearchFieldMappingManager(applicationConfiguration);
            this.setSearchMetaFields(searchFieldMappingManager.getFieldMappings());
            this.setSystemInstanceName(systemInstance.getName());
            this.setSystemInstanceGroovyCode(StringEscapeUtils.unescapeJava(systemInstance.getGroovyCode()));
            this.setSearchFieldMappings(systemInstance.getSearchFieldMappings());
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    public void metaFieldNameSelectionChanged() {
        if (!this.selectedMetaField.matches("\\d+"))
        {
            return;
        }
        for (SearchFieldMapping currentSearchFieldMapping : searchFieldMappings)
        {
            if (Integer.parseInt(this.selectedMetaField) == currentSearchFieldMapping.getID())
            {
                this.selectedField = currentSearchFieldMapping.getFieldName();
                return;
            }
        }
    }

    public void updateFieldNameByMetaFieldName() {
        if (!this.selectedMetaField.matches("\\d+"))
        {
            return;
        }
        for (SearchFieldMapping currentSearchFieldMapping : searchFieldMappings)
        {
            if (Integer.parseInt(this.selectedMetaField) == currentSearchFieldMapping.getID())
            {
                currentSearchFieldMapping.setFieldName(this.selectedField);
                return;
            }
        }
    } 

    /**
     * This method update the system instance.
     */
    public void updateSystemInstance() {
        try
        {
            SystemInstance systemInstance = new SystemInstance();

            systemInstance.setID(systemInstanceID);
            systemInstance.setName(systemInstanceName);
            systemInstance.setGroovyCode(StringEscapeUtils.escapeJava(systemInstanceGroovyCode));
            systemInstance.setSearchFieldMappings(searchFieldMappings);
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstanceDataProvider.updateSystemInstance(systemInstance);
            this.selectedField = "";
            this.systemInstanceID = -1;
            this.systemInstanceGroovyCode = ""; 
            this.systemInstanceName ="";
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

    public void UpdateMapping() {

    }

    /**
     * This method executes a redirect to the index page.
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

}
