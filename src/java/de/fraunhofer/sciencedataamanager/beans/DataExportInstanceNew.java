/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.DataExportInstance;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.DataExportInstanceDataManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "dataExportInstanceNew")
@SessionScoped
public class DataExportInstanceNew {

    private String dataExportInstanceName;
    private String dataExportInstanceGroovyCode;
  private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration(); 

    public String getDataExportInstanceName() {
        return dataExportInstanceName;
    }

    public String getDataExportInstanceGroovyCode() {
        return dataExportInstanceGroovyCode;
    }

    public void setDataExportInstanceName(String dataExportInstanceName) {
        this.dataExportInstanceName = dataExportInstanceName;
    }

    public void setDataExportInstanceGroovyCode(String dataExportInstanceGroovyCode) {
        this.dataExportInstanceGroovyCode = dataExportInstanceGroovyCode;
    }

    public void createNewDataExportInstance() throws IOException {
        try {
            DataExportInstance dataExportInstance = new DataExportInstance();
            dataExportInstance.setName(getDataExportInstanceName());
            dataExportInstance.setGroovyCode(StringEscapeUtils.escapeJava(getDataExportInstanceGroovyCode()));

            DataExportInstanceDataManager dataExportInstanceDataProvider = new DataExportInstanceDataManager(applicationConfiguration);
            dataExportInstanceDataProvider.insert(dataExportInstance);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);
         
        }
    }
    
       public void redirectBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }

    }

}
