/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.DataExportInstanceDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.ScientificPaperMetaInformationDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchAnalyticDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionRunDataManager;
import de.fraunhofer.sciencedataamanager.domain.DataExportInstance;
import de.fraunhofer.sciencedataamanager.domain.SearchAnalyticDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitionExecutionRun;
import de.fraunhofer.sciencedataamanager.interfaces.IExportScientificPaperMetaInformation;
import groovy.lang.GroovyClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * This class provides logic and data for the search analytic management web
 * side.
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchAnalyticsManagement")
@SessionScoped
public class SearchAnalyticsManagement {

    private String selectedSearchDefinition;
    private String selectedSearchDefinitionExecution;

    private String selectedSearchAnalytic;
    private String selectedExportInstance;

    private LinkedList<SearchDefinitionExecutionRun> loadedSearchDefinitionExecutionRunList;

    private LinkedList<SearchDefinitonExecution> loadedSearchDefinitionExecutionList;
    private LinkedList<SearchAnalyticDefinition> loadedSearchAnalyticDefinitionList;
    private LinkedList<SearchDefinition> loadedSearchDefinitionList;
    private Collection loadedDataExportInstances;

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();

    /**
     * Returns the loaded search definition execution run list.
     *
     * @return the loaded search definition execution run list.
     */
    public LinkedList<SearchDefinitionExecutionRun> getLoadedSearchDefinitionExecutionRunList() {
        return loadedSearchDefinitionExecutionRunList;
    }

    /**
     * Return the loaded data export instances.
     *
     * @return the loaded data export instances.
     */
    public Collection getLoadedDataExportInstances() {
        return loadedDataExportInstances;
    }

    /**
     * Returns the selected export instance,
     *
     * @return the selected export instance.
     */
    public String getSelectedExportInstance() {
        return selectedExportInstance;
    }

    /**
     * Sets the selected export instance
     *
     * @param selectedExportInstance the selected export instance
     */
    public void setSelectedExportInstance(String selectedExportInstance) {
        this.selectedExportInstance = selectedExportInstance;
    }

    /**
     * Returns the selected search definition execution.
     *
     * @return the selected search definition execution.
     */
    public String getSelectedSearchDefinitionExecution() {
        return selectedSearchDefinitionExecution;
    }

    /**
     * Sets the selected search definition execution.
     *
     * @param selectedSearchDefinitionExecution The selected search definition
     * execution.
     */
    public void setSelectedSearchDefinitionExecution(String selectedSearchDefinitionExecution) {
        this.selectedSearchDefinitionExecution = selectedSearchDefinitionExecution;
    }

    /**
     * Sets the selected search definition.
     *
     * @param selectedSearchDefinition the selected search definition.
     */
    public void setSelectedSearchDefinition(String selectedSearchDefinition) {
        this.selectedSearchDefinition = selectedSearchDefinition;
    }

    /**
     * Sets the selected search analytic.
     *
     * @param selectedSearchAnalytic the selected search analytic.
     */
    public void setSelectedSearchAnalytic(String selectedSearchAnalytic) {
        this.selectedSearchAnalytic = selectedSearchAnalytic;
    }

    Map<String, List<Object>> loadedSearchAnalyticsResultMap = null;

    List<Map<String, Object>> loadedSearchAnalyticsResultRows = null;
    List<String> loadedSearchAnalyticsResultColumns = null;

    /**
     * Returns the loaded search analytic result rows.
     *
     * @return the loaded search analytic result rows.
     */
    public List<Map<String, Object>> getLoadedSearchAnalyticsResultRows() {
        return loadedSearchAnalyticsResultRows;
    }

    /**
     * Returns the loaded search analytic result map.
     *
     * @return the loaded search analytic result map.
     */
    public Map<String, List<Object>> getLoadedSearchAnalyticsResultMap() {
        return loadedSearchAnalyticsResultMap;
    }

    /**
     * Returns the selected search analytic.
     *
     * @return the selected search analytic.
     */
    public String getSelectedSearchAnalytic() {
        return selectedSearchAnalytic;
    }

    /**
     * Return the loaded search analytic definition list.
     *
     * @return the loaded search analytic definition list.
     */
    public LinkedList<SearchAnalyticDefinition> getLoadedSearchAnalyticDefinitionList() {
        return loadedSearchAnalyticDefinitionList;
    }

    /**
     * Retunrs the loaded search definition list.
     *
     * @return the loaded search definition list.
     */
    public LinkedList<SearchDefinition> getLoadedSearchDefinitionList() {
        return loadedSearchDefinitionList;
    }

    /**
     * Returns the application configuration.
     *
     * @return the application configuration.
     */
    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    /**
     * Returns the selected search definition.
     *
     * @return the selected search definition.
     */
    public String getSelectedSearchDefinition() {
        return selectedSearchDefinition;
    }

    /**
     * Loads the search definition executions
     */
    public void loadSearchDefinitionExecution() {
        this.loadedSearchDefinitionExecutionRunList = getSearchExecutionDefinitionRunListBySearchDefinition();
    }

    /**
     * Load the data tables.
     */
    public void loadTable() {
        if (selectedSearchAnalytic == null || "".equals(selectedSearchAnalytic))
        {
            return;
        }
        if (selectedSearchDefinitionExecution == null || "".equals(selectedSearchDefinitionExecution))
        {
            return;
        }

        this.loadedSearchAnalyticsResultMap = getScientificMetaInformationBySearchAnalyticDefinition();

        List<String> columns = new ArrayList<String>(loadedSearchAnalyticsResultMap.keySet()); // Note I expect LinkedHashMap ordering here.
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int size = loadedSearchAnalyticsResultMap.values().iterator().next().size();

        for (int i = 0; i < size; i++)
        {
            Map<String, Object> row = new HashMap<String, Object>();

            for (String column : columns)
            {
                row.put(column, loadedSearchAnalyticsResultMap.get(column).get(i));
            }

            rows.add(row);
        }
        this.loadedSearchAnalyticsResultRows = rows;
        this.loadedSearchAnalyticsResultColumns = columns;
    }

    /**
     *
     * @return
     */
    public List<String> getLoadedSearchAnalyticsResultColumns() {
        return loadedSearchAnalyticsResultColumns;
    }

    /**
     * The event is executed after page load
     *
     * @param event the page load event object
     */
    public void onLoad(ComponentSystemEvent event) {
        try
        {
            if (FacesContext.getCurrentInstance().isPostback())
            {
                return;
            }
            this.loadedSearchDefinitionList = getSearchDefinitions();
            this.loadedSearchAnalyticDefinitionList = getSearchAnalyticDefintion();
            this.loadedDataExportInstances = getDataExportInstances();

        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the loaded search definition execution list.
     *
     * @return the loaded search definition list.
     */
    public LinkedList<SearchDefinitonExecution> getLoadedSearchDefinitionExecutionList() {
        return loadedSearchDefinitionExecutionList;
    }

    /**
     * Returns the data export instances.
     *
     * @return the data export instances.
     */
    public Collection getDataExportInstances() {
        Collection dataExportInstances = null;

        DataExportInstanceDataManager dataExportInstanceDataManager = new DataExportInstanceDataManager(applicationConfiguration);
        try
        {

            dataExportInstances = dataExportInstanceDataManager.getDataExportInstances();
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return dataExportInstances;
    }

    /**
     * Returns the search execution list.
     *
     * @return the search execution list.
     */
    public LinkedList<SearchDefinitonExecution> getSearchExecutionDefinitionListBySearchDefinition() {
        if (selectedSearchDefinition == null || "".equals(selectedSearchDefinition))
        {
            return null;
        }
        int searchDefinitionID = Integer.parseInt(selectedSearchDefinition);
        LinkedList<SearchDefinitonExecution> searchExecutionList = null;
        try
        {
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);
            SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
            searchExecutionList = searchDefinitonExecutionDataProvider.getAllSearchDefinitionExecutionForSearchDefinition(searchDefinition);

        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchExecutionList;
    }

    /**
     * Returns the search execution definition run list.
     *
     * @return the search execution definition run list.
     */
    public LinkedList<SearchDefinitionExecutionRun> getSearchExecutionDefinitionRunListBySearchDefinition() {
        if (selectedSearchDefinition == null || "".equals(selectedSearchDefinition))
        {
            return null;
        }
        int searchDefinitionID = Integer.parseInt(selectedSearchDefinition);
        LinkedList<SearchDefinitionExecutionRun> searchExecutionRunList = null;
        try
        {
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);
            SearchDefinitonExecutionRunDataManager searchDefinitonExecutionRunDataManager = new SearchDefinitonExecutionRunDataManager(applicationConfiguration);
            searchExecutionRunList = searchDefinitonExecutionRunDataManager.getAllSearchDefinitionExecutionRunForSearchDefinition(searchDefinition);

        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchExecutionRunList;
    }

    /**
     * Returns the search definitions.
     *
     * @return the search definitions.
     */
    public LinkedList<SearchDefinition> getSearchDefinitions() {
        LinkedList<SearchDefinition> searchDefinitions = null;
        try
        {
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            searchDefinitions = searchDefinitionDataProvider.getSearchDefinitions();
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchDefinitions;
    }

    /**
     * Returns the search analytic definition.
     *
     * @return the search analytic definition.
     */
    public LinkedList<SearchAnalyticDefinition> getSearchAnalyticDefintion() {
        LinkedList<SearchAnalyticDefinition> searchAnalyticDefinitionList = null;
        try
        {
            SearchAnalyticDefinitionDataManager searchAnalyticDefinitionDataManager = new SearchAnalyticDefinitionDataManager(applicationConfiguration);
            searchAnalyticDefinitionList = searchAnalyticDefinitionDataManager.getAnalyticDefinitions();
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return searchAnalyticDefinitionList;
    }

    /**
     * Returns the scientific meta information by search analytic definition.
     *
     * @return the scientific meta information.
     */
    public Map<String, List<Object>> getScientificMetaInformationBySearchAnalyticDefinition() {
        Map<String, List<Object>> resultSetMap = null;

        try
        {
            if (selectedSearchAnalytic == null || "".equals(selectedSearchAnalytic))
            {
                return resultSetMap;
            }
            if (selectedSearchDefinitionExecution == null || "".equals(selectedSearchDefinitionExecution))
            {
                return resultSetMap;
            }

            int selectedSearchAnalyticID = Integer.parseInt(selectedSearchAnalytic);
            int selectedSearchDefinitionExecutionID = Integer.parseInt(selectedSearchDefinitionExecution);

            for (SearchAnalyticDefinition searchAnalyticDefinition : loadedSearchAnalyticDefinitionList)
            {
                if (searchAnalyticDefinition.getID() == selectedSearchAnalyticID)
                {
                    String queryBuffer = searchAnalyticDefinition.getQuery();
                    searchAnalyticDefinition.setQuery(String.format(searchAnalyticDefinition.getQuery(), selectedSearchDefinitionExecutionID));
                    ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataManager = new ScientificPaperMetaInformationDataManager(applicationConfiguration);
                    resultSetMap = scientificPaperMetaInformationDataManager.getScientificPaperMetaInformationResultSetBySearchAnalyticDefinition(searchAnalyticDefinition, null);
                    searchAnalyticDefinition.setQuery(queryBuffer);
                }
            }
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
        return resultSetMap;
    }

    /**
     * Executes a redirect to edit page.
     */
    public void redirectToEditPage() {
        try
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect("SearchAnalyticDefinitionEdit.xhtml?SelectedSearchAnalyticDefinition=" + selectedSearchAnalytic);
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }
    }

    /**
     * Deletes a search analytic by id.
     */
    public void deleteSearchAnalyticByID() {
        if (selectedSearchAnalytic == null || "".equals(selectedSearchAnalytic))
        {
            return;
        }
        int id = Integer.parseInt(selectedSearchAnalytic);

        try
        {
            SearchAnalyticDefinitionDataManager searchAnalyticDefinitionDataManager = new SearchAnalyticDefinitionDataManager(applicationConfiguration);
            searchAnalyticDefinitionDataManager.delete(id);
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.applicationConfiguration.getLoggingManager().logException(ex);

        }

    }

    /**
     * Executes the generic export algorithmus.
     */
    public void export() {
      try
        {
            if (selectedExportInstance == null || "".equals(selectedExportInstance))
            {
                return;
            }

            int selectedExportInstanceID = Integer.parseInt(selectedExportInstance);
            DataExportInstanceDataManager dataExportInstanceDataManager = new DataExportInstanceDataManager(applicationConfiguration);
            DataExportInstance dataExportInstance = dataExportInstanceDataManager.getDataExportInstanceByID(selectedExportInstanceID);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            String excelExportDefaultFilename = dataExportInstance.getExportFilePrefix() + new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + dataExportInstance.getExportFilePostfix();
            externalContext.setResponseContentType(dataExportInstance.getResponseContentType());
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + excelExportDefaultFilename + "\"");

            GroovyClassLoader gcl = new GroovyClassLoader();
            Class parsedGroocyClass = gcl.parseClass(StringEscapeUtils.unescapeJava(dataExportInstance.getGroovyCode()));

            Object groovyClassInstance = parsedGroocyClass.newInstance();

            IExportScientificPaperMetaInformation currentDataExportInstance = (IExportScientificPaperMetaInformation) groovyClassInstance;
            currentDataExportInstance.export(this.loadedSearchAnalyticsResultMap, externalContext.getResponseOutputStream());

            facesContext.responseComplete();

        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }
}
