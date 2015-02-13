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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "dataExportInstanceEdit")
@SessionScoped
public class DataExportInstanceEdit {

    private String dataExportInstanceName;
    private String dataExportInstanceGroovyCode;
    private int dataExportInstanceID;
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

    public void onLoad(ComponentSystemEvent event) {
        try {
            dataExportInstanceID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("DataExportInstanceID"));

            DataExportInstanceDataManager dataExportInstanceDataProvider = new DataExportInstanceDataManager(applicationConfiguration);
            DataExportInstance dataExportInstance = dataExportInstanceDataProvider.getDataExportInstanceByID(dataExportInstanceID);
            this.setDataExportInstanceName(dataExportInstance.getName());
            this.setDataExportInstanceGroovyCode(StringEscapeUtils.unescapeJava(dataExportInstance.getGroovyCode()));
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    public void updateDataExportInstance() {
        try {
            DataExportInstance dataExportInstance = new DataExportInstance();

            dataExportInstance.setID(dataExportInstanceID);
            dataExportInstance.setName(dataExportInstanceName);
            dataExportInstance.setGroovyCode(StringEscapeUtils.escapeJava(dataExportInstanceGroovyCode));
            DataExportInstanceDataManager dataExportInstanceDataProvider = new DataExportInstanceDataManager(applicationConfiguration);
            dataExportInstanceDataProvider.updateDataExportInstance(dataExportInstance);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

    public void redirectBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

}
