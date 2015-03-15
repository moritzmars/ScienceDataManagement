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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * The class provides logic and data for the system instance edit page. 
 * @author Moritz Mars
 */
@ManagedBean(name = "systemInstanceEdit")
@SessionScoped
public class SystemInstanceEdit {

    private String systemInstanceName;
    private String systemInstanceGroovyCode;
    private int systemInstanceID;
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Returns the system instance name. 
     * @return the system instance name. 
     */
    public String getSystemInstanceName() {
        return systemInstanceName;
    }

    /**
     * Returns the system instance groovy code. 
     * @return the system instance groocy code. 
     */
    public String getSystemInstanceGroovyCode() {
        return systemInstanceGroovyCode;
    }

    /**
     * Sets the system instance name.
     * @param the system instance name. 
     */
    public void setSystemInstanceName(String systemInstanceName) {
        this.systemInstanceName = systemInstanceName;
    }

    /**
     * Sets the system instance groovy code. 
     * @param the system instance groovy code. 
     */
    public void setSystemInstanceGroovyCode(String systemInstanceGroovyCode) {
        this.systemInstanceGroovyCode = systemInstanceGroovyCode;
    }

    /**
     * This method is executed after the page is loaded. 
     * @param informations about the page load. 
     */
    public void onLoad(ComponentSystemEvent event) {
        try {
            systemInstanceID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("SystemInstanceID"));

            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            SystemInstance systemInstance = systemInstanceDataProvider.getSystemInstanceByID(systemInstanceID);
            this.setSystemInstanceName(systemInstance.getName());
            this.setSystemInstanceGroovyCode(StringEscapeUtils.unescapeJava(systemInstance.getGroovyCode()));
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * This method update the system instance. 
     */
    public void updateSystemInstance() {
        try {
            SystemInstance systemInstance = new SystemInstance();

            systemInstance.setID(systemInstanceID);
            systemInstance.setName(systemInstanceName);
            systemInstance.setGroovyCode(StringEscapeUtils.escapeJava(systemInstanceGroovyCode));
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstanceDataProvider.updateSystemInstance(systemInstance);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

    /**
     * This method executes a redirect to the index page. 
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
