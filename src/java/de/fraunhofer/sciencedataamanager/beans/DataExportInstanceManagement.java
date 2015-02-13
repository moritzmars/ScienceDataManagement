/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.DataExportInstanceDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDatabaseManager;
import de.fraunhofer.sciencedataamanager.domain.DataExportInstance;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import java.io.IOException;
import java.util.Collection;
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
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "dataExportInstanceManagement")
@SessionScoped
public class DataExportInstanceManagement {

    private DataExportInstance selectedDataExportInstance;
    private LinkedList<DataExportInstance> loadedDataExportInstances;
    private String selectedItem;
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    public LinkedList<DataExportInstance> getLoadedDataExportInstances() {
        return loadedDataExportInstances;
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public DataExportInstance getSelectedDataExportInstance() {
        return selectedDataExportInstance;
    }

    public void setSelectedDataExportInstance(DataExportInstance selectedDataExportInstance) {
        this.selectedDataExportInstance = selectedDataExportInstance;
    }

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

    public void redirectToEditPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("DataExportInstanceEdit.xhtml?DataExportInstanceID=" + selectedItem);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    public void deleteDataExportInstanceByID() throws IOException {
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
