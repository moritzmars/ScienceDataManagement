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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * The class is the bean for the JSF Page Data Export Instance Edit
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "dataExportInstanceEdit")
@SessionScoped
public class DataExportInstanceEdit {

    private String dataExportInstanceName;
    private String dataExportInstanceGroovyCode;
    private String dataExportFilePrefix;

    private String dataExportFilePostfix;
    private String ResponseContentType;
    private int dataExportInstanceID;

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Return the data export instance name.
     *
     * @return data export instance name.
     */
    public String getDataExportInstanceName() {
        return dataExportInstanceName;
    }

    /**
     * Return the Groovy Code of the Data Export Instance
     *
     * @return Groovy Code of the Data Export Instance
     */
    public String getDataExportInstanceGroovyCode() {
        return dataExportInstanceGroovyCode;
    }

    /**
     * Sets the Data Export Instance Name
     *
     * @param dataExportInstanceName The Data Export Instance Name
     */
    public void setDataExportInstanceName(String dataExportInstanceName) {
        this.dataExportInstanceName = dataExportInstanceName;
    }

    /**
     * Sets the Data Export Instance Groovy Code
     *
     * @param dataExportInstanceGroovyCode The Data Export Instance Goorvy Code
     */
    public void setDataExportInstanceGroovyCode(String dataExportInstanceGroovyCode) {
        this.dataExportInstanceGroovyCode = dataExportInstanceGroovyCode;
    }

    /**
     * The method is executed during first execution of the page and loads the
     * initial data from database.
     *
     * @param event from the JSF Framework
     */
    public void onLoad(ComponentSystemEvent event) {
        try
        {
            dataExportInstanceID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("DataExportInstanceID"));

            DataExportInstanceDataManager dataExportInstanceDataProvider = new DataExportInstanceDataManager(applicationConfiguration);
            DataExportInstance dataExportInstance = dataExportInstanceDataProvider.getDataExportInstanceByID(dataExportInstanceID);
            this.setDataExportInstanceName(dataExportInstance.getName());
            this.setDataExportFilePostfix(dataExportInstance.getExportFilePostfix());
            this.setDataExportFilePrefix(dataExportInstance.getExportFilePrefix());
            this.setResponseContentType(dataExportInstance.getResponseContentType());
            this.setDataExportInstanceGroovyCode(StringEscapeUtils.unescapeJava(dataExportInstance.getGroovyCode()));
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * Updates the Data Export Instance with the current values from the JSF
     * page.
     */
    public void updateDataExportInstance() {
        try
        {
            DataExportInstance dataExportInstance = new DataExportInstance();

            dataExportInstance.setID(dataExportInstanceID);
            dataExportInstance.setName(dataExportInstanceName);

            dataExportInstance.setExportFilePrefix(getDataExportFilePrefix());
            dataExportInstance.setExportFilePostfix(getDataExportFilePostfix());
            dataExportInstance.setResponseContentType(getResponseContentType());

            dataExportInstance.setGroovyCode(StringEscapeUtils.escapeJava(dataExportInstanceGroovyCode));
            DataExportInstanceDataManager dataExportInstanceDataProvider = new DataExportInstanceDataManager(applicationConfiguration);
            dataExportInstanceDataProvider.updateDataExportInstance(dataExportInstance);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

    /**
     * Executes a redirect to the Index.html page.
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

}
