/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import java.io.IOException;
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
 * This class provides logic and data for the system instance management. 
 * @author Moritz Mars
 */
@ManagedBean(name = "systemInstanceManagement")
@SessionScoped
public class SystemInstanceManagement {

    private SystemInstance selectedSystemInstance;
    private String selectedItem;
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    private LinkedList<SystemInstance> loadedSystemInstances;

    /**
     * Returns the loaded system instances. 
     * @return
     */
    public LinkedList<SystemInstance> getLoadedSystemInstances() {
        return loadedSystemInstances;
    }

    /**
     * Returns the selected item.
     * @param the selected item. 
     */
    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * Returns the selected item. 
     * @return the selected item. 
     */
    public String getSelectedItem() {
        return selectedItem;
    }

    /**
     * Sets the selected connector. 
     * @param the selected connector
     */
    public void setSelectedSystemInstance(SystemInstance selectedSystemInstance) {
        this.selectedSystemInstance = selectedSystemInstance;
    }

    /**
     * Returns the selected system instance. 
     * @return the selected system instance. 
     */
    public SystemInstance getSelectedSystemInstance() {
        return selectedSystemInstance;
    }

    /**
     * This method is executed after the page is loaded. 
     * @param objects has information about the page load. 
     */
    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadedSystemInstances = getSystemInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the system instances. 
     * @return the system instances. 
     */
    public LinkedList<SystemInstance> getSystemInstances() {
        LinkedList<SystemInstance> systemInstances = null;
        try {
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstances = systemInstanceDataProvider.getSystemInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
     Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return systemInstances;
    }

    /**
     * Returns the system instance. 
     * @return the system instance. 
     */
    public SystemInstance getSystemInstanceByID() {
        try {
            if (selectedItem == null || "".equals(selectedItem)) {
                return null;
            }
            int id = Integer.parseInt(selectedItem);

            for (SystemInstance currentSystemIntance : loadedSystemInstances) {
                if (currentSystemIntance.getID() == id) {
                    currentSystemIntance.setGroovyCode(StringEscapeUtils.unescapeJava(currentSystemIntance.getGroovyCode()));
                    return currentSystemIntance;
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
     * Execute a redirect to the edit page. 
     */
    public void redirectToEditPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("SystemInstanceEdit.xhtml?SystemInstanceID=" + selectedItem);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * Deletes the connector. 
     * @throws IOException during database access. 
     */
    public void deleteSystemInstanceByID() throws IOException {
        try {
            if (selectedItem == null || "".equals(selectedItem)) {
                return;
            }
            int id = Integer.parseInt(selectedItem);

            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstanceDataProvider.delete(id);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

}
