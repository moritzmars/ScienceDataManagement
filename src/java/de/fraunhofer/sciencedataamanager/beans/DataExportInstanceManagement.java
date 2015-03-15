/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.DataExportInstanceDataManager;
import de.fraunhofer.sciencedataamanager.domain.DataExportInstance;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * The class is provides bean functions to the JSF data export instance management. 
 * @author Moritz Mars
 */
@ManagedBean(name = "dataExportInstanceManagement")
@SessionScoped
public class DataExportInstanceManagement {

    private DataExportInstance selectedDataExportInstance;
    private LinkedList<DataExportInstance> loadedDataExportInstances;
    private String selectedItem;
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Return the loaded data export instances. 
     * @return the loaded data export instance. 
     */
    public LinkedList<DataExportInstance> getLoadedDataExportInstances() {
        return loadedDataExportInstances;
    }

    /**
     * Return the application configuration.
     * @return The application configuration. 
     */
    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    /**
     * Sets the application configuration
     * @param applicationConfiguration application configuration to set. 
     */
    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    /**
     * Set the selected data export instance id. 
     * @param selectedItem The selected data export instance id.
     */
    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * Return the selected data export instance id. 
     * @return the selected data export instance id.
     */
    public String getSelectedItem() {
        return selectedItem;
    }

    /**
     * The selected data export instance.
     * @return the selected data export instance. 
     */
    public DataExportInstance getSelectedDataExportInstance() {
        return selectedDataExportInstance;
    }

    /**
     * Sets the selected data export instance id. 
     * @param selectedDataExportInstance the selected data export instance id. 
     */
    public void setSelectedDataExportInstance(DataExportInstance selectedDataExportInstance) {
        this.selectedDataExportInstance = selectedDataExportInstance;
    }

    /**
     * The method is executed during first execution of the page and loads the initial data from database. 
     * @param event from the JSF Framework
     */
    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadedDataExportInstances = getDataExportInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the data export instances from database. 
     * @return the data export instances from database. 
     */
    public LinkedList<DataExportInstance> getDataExportInstances() {
        LinkedList<DataExportInstance> dataExportInstances = null;
        try {
            DataExportInstanceDataManager systemInstanceDataProvider = new DataExportInstanceDataManager(applicationConfiguration);
            dataExportInstances = systemInstanceDataProvider.getDataExportInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return dataExportInstances;
    }

    /**
     * Returns the data export instance. 
     * @return the data export instance. 
     */
    public DataExportInstance getDataExportInstanceByID() {
        DataExportInstance dataExportInstance = null;
        try {
            if (selectedItem == null || "".equals(selectedItem)) {
                return null;
            }
            int id = Integer.parseInt(selectedItem);
            for (DataExportInstance currentDataExportInstance : loadedDataExportInstances) {
                if (currentDataExportInstance.getID() == id) {
                    currentDataExportInstance.setGroovyCode(StringEscapeUtils.unescapeJava(currentDataExportInstance.getGroovyCode()));
                    return currentDataExportInstance;
                }
            }

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return null;
    }

    /**
     * Executes a redirect to the edit page of the data export instance. 
     */
    public void redirectToEditPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("DataExportInstanceEdit.xhtml?DataExportInstanceID=" + selectedItem);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * Deletes the selected data export instance .
     */
    public void deleteDataExportInstanceByID()  {
        try {
            if (selectedItem == null || "".equals(selectedItem)) {
                return;
            }
            int id = Integer.parseInt(selectedItem);

            DataExportInstanceDataManager dataExportInstanceDataProvider = new DataExportInstanceDataManager(applicationConfiguration);
            dataExportInstanceDataProvider.delete(id);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

}
