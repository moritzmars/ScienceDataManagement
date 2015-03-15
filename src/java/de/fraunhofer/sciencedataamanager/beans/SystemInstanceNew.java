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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * This class provides logic and data to the connector new page. 
 * @author Moritz Mars
 */
@ManagedBean(name = "systemInstanceNew")
@SessionScoped
public class SystemInstanceNew {

    private String systemInstanceName;
    private String systemInstanceGroovyCode;
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
     * @return the system instance groovy code. 
     */
    public String getSystemInstanceGroovyCode() {
        return systemInstanceGroovyCode;
    }

    /**
     * Sets the system instance name. 
     * @param systemInstanceName system instance name. 
     */
    public void setSystemInstanceName(String systemInstanceName) {
        this.systemInstanceName = systemInstanceName;
    }

    /**
     * Sets the system instance groovy code. 
     * @param systemInstanceGroovyCode system instance groovy code. 
     */
    public void setSystemInstanceGroovyCode(String systemInstanceGroovyCode) {
        this.systemInstanceGroovyCode = systemInstanceGroovyCode;
    }

    /**
     * This method creates a new connector. 
     * @throws IOException during database access. 
     */
    public void createNewSystemInstance() throws IOException {
        try {
            SystemInstance systemInstance = new SystemInstance();
            systemInstance.setName(getSystemInstanceName());
            systemInstance.setGroovyCode(StringEscapeUtils.escapeJava(getSystemInstanceGroovyCode()));

            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstanceDataProvider.insert(systemInstance);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);
         
        }
    }
    
    /**
     * Executes a redirect to index page.
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
