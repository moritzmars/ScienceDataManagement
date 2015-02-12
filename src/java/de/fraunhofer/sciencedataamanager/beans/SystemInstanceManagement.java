/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
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
@ManagedBean(name = "systemInstanceManagement")
@SessionScoped
public class SystemInstanceManagement {

    private SystemInstance selectedSystemInstance;
    private String selectedItem;
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    private LinkedList<SystemInstance> loadedSystemInstances;

    public LinkedList<SystemInstance> getLoadedSystemInstances() {
        return loadedSystemInstances;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedSystemInstance(SystemInstance selectedSystemInstance) {
        this.selectedSystemInstance = selectedSystemInstance;
    }

    public SystemInstance getSelectedSystemInstance() {
        return selectedSystemInstance;
    }

    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadedSystemInstances = getSystemInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            LoggingDataManager.logException(ex, applicationConfiguration);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedList<SystemInstance> getSystemInstances() {
        LinkedList<SystemInstance> systemInstances = null;
        try {
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstances = systemInstanceDataProvider.getSystemInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
     Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
        return systemInstances;
    }

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
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
        return null;
    }

    public void redirectToEditPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("SystemInstanceEdit.xhtml?SystemInstanceID=" + selectedItem);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
    }

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
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
    }

}
