/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.DataExportInstance;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.DataExportInstanceDataManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * The class provides bean functions to the JSF new data export instance.
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "dataExportInstanceNew")
@SessionScoped
public class DataExportInstanceNew {

    private String dataExportInstanceName;
    private String dataExportInstanceGroovyCode;

    private String dataExportFilePrefix;
    private String dataExportFilePostfix;
    private String ResponseContentType;

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Returns the export instnace name
     *
     * @return export instance name
     */
    public String getDataExportInstanceName() {
        return dataExportInstanceName;
    }

    /**
     * Return the data export instance groovy code
     *
     * @return groovy code of the data export instnace
     */
    public String getDataExportInstanceGroovyCode() {
        return dataExportInstanceGroovyCode;
    }

    /**
     * Sets the data export instance name
     *
     * @param dataExportInstanceName the data export instance name
     */
    public void setDataExportInstanceName(String dataExportInstanceName) {
        this.dataExportInstanceName = dataExportInstanceName;
    }

    /**
     * Sets the data export instance groovy code
     *
     * @param dataExportInstanceGroovyCode The data export instance groovy code
     */
    public void setDataExportInstanceGroovyCode(String dataExportInstanceGroovyCode) {
        this.dataExportInstanceGroovyCode = dataExportInstanceGroovyCode;
    }

    public String getDataExportFilePrefix() {
        return dataExportFilePrefix;
    }

    public void setDataExportFilePrefix(String dataExportFilePrefix) {
        this.dataExportFilePrefix = dataExportFilePrefix;
    }

    public String getDataExportFilePostfix() {
        return dataExportFilePostfix;
    }

    public void setDataExportFilePostfix(String dataExportFilePostfix) {
        this.dataExportFilePostfix = dataExportFilePostfix;
    }

    public String getResponseContentType() {
        return ResponseContentType;
    }

    public void setResponseContentType(String ResponseContentType) {
        this.ResponseContentType = ResponseContentType;
    }

    /**
     * Creates a new data export instance
     *
     * @throws IOException General execpeiton
     */
    public void createNewDataExportInstance() throws IOException {
        try
        {
            DataExportInstance dataExportInstance = new DataExportInstance();
            dataExportInstance.setName(getDataExportInstanceName());
            dataExportInstance.setGroovyCode(StringEscapeUtils.escapeJava(getDataExportInstanceGroovyCode()));
            
            dataExportInstance.setExportFilePrefix(getDataExportFilePrefix());
            dataExportInstance.setExportFilePostfix(getDataExportFilePostfix());
            dataExportInstance.setResponseContentType(getResponseContentType());
            
            
            
            DataExportInstanceDataManager dataExportInstanceDataProvider = new DataExportInstanceDataManager(applicationConfiguration);
            dataExportInstanceDataProvider.insert(dataExportInstance);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * Executes a redirect to the Index.html
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
