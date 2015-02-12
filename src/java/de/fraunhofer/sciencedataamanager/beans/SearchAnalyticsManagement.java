/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.business.DataExportExecutionManager;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.DataExportInstanceDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.ScientificPaperMetaInformationDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchAnalyticDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionRunDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import de.fraunhofer.sciencedataamanager.domain.DataExportInstance;
import de.fraunhofer.sciencedataamanager.domain.SearchAnalyticDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitionExecutionRun;
import de.fraunhofer.sciencedataamanager.exampes.export.ExcelDataExport;
import de.fraunhofer.sciencedataamanager.interfaces.IExportScientificPaperMetaInformation;
import groovy.lang.GroovyClassLoader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

    public LinkedList<SearchDefinitionExecutionRun> getLoadedSearchDefinitionExecutionRunList() {
        return loadedSearchDefinitionExecutionRunList;
    }

    public Collection getLoadedDataExportInstances() {
        return loadedDataExportInstances;
    }

    public String getSelectedExportInstance() {
        return selectedExportInstance;
    }

    public void setSelectedExportInstance(String selectedExportInstance) {
        this.selectedExportInstance = selectedExportInstance;
    }

    public String getSelectedSearchDefinitionExecution() {
        return selectedSearchDefinitionExecution;
    }

    public void setSelectedSearchDefinitionExecution(String selectedSearchDefinitionExecution) {
        this.selectedSearchDefinitionExecution = selectedSearchDefinitionExecution;
    }

    public void setSelectedSearchDefinition(String selectedSearchDefinition) {
        this.selectedSearchDefinition = selectedSearchDefinition;
    }

    public void setSelectedSearchAnalytic(String selectedSearchAnalytic) {
        this.selectedSearchAnalytic = selectedSearchAnalytic;
    }

    Map<String, List<Object>> loadedSearchAnalyticsResultMap = null;

    List<Map<String, Object>> loadedSearchAnalyticsResultRows = null;
    List<String> loadedSearchAnalyticsResultColumns = null;

    public List<Map<String, Object>> getLoadedSearchAnalyticsResultRows() {
        return loadedSearchAnalyticsResultRows;
    }

    public Map<String, List<Object>> getLoadedSearchAnalyticsResultMap() {
        return loadedSearchAnalyticsResultMap;
    }

    public String getSelectedSearchAnalytic() {
        return selectedSearchAnalytic;
    }

    public LinkedList<SearchAnalyticDefinition> getLoadedSearchAnalyticDefinitionList() {
        return loadedSearchAnalyticDefinitionList;
    }

    public LinkedList<SearchDefinition> getLoadedSearchDefinitionList() {
        return loadedSearchDefinitionList;
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public String getSelectedSearchDefinition() {
        return selectedSearchDefinition;
    }

    public void loadSearchDefinitionExecution() {
        this.loadedSearchDefinitionExecutionRunList = getSearchExecutionDefinitionRunListBySearchDefinition();
    }

    public void loadTable() {
        if (selectedSearchAnalytic == null || "".equals(selectedSearchAnalytic)) {
            return;
        }
        if (selectedSearchDefinitionExecution == null || "".equals(selectedSearchDefinitionExecution)) {
            return;
        }

        this.loadedSearchAnalyticsResultMap = getScientificMetaInformationBySearchAnalyticDefinition();

        List<String> columns = new ArrayList<String>(loadedSearchAnalyticsResultMap.keySet()); // Note I expect LinkedHashMap ordering here.
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int size = loadedSearchAnalyticsResultMap.values().iterator().next().size();

        for (int i = 0; i < size; i++) {
            Map<String, Object> row = new HashMap<String, Object>();

            for (String column : columns) {
                row.put(column, loadedSearchAnalyticsResultMap.get(column).get(i));
            }

            rows.add(row);
        }
        this.loadedSearchAnalyticsResultRows = rows;
        this.loadedSearchAnalyticsResultColumns = columns;
    }

    public List<String> getLoadedSearchAnalyticsResultColumns() {
        return loadedSearchAnalyticsResultColumns;
    }

    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadedSearchDefinitionList = getSearchDefinitions();
            this.loadedSearchAnalyticDefinitionList = getSearchAnalyticDefintion();
            this.loadedDataExportInstances = getDataExportInstances();

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            LoggingDataManager.logException(ex, applicationConfiguration);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedList<SearchDefinitonExecution> getLoadedSearchDefinitionExecutionList() {
        return loadedSearchDefinitionExecutionList;
    }

    public Collection getDataExportInstances() {
        Collection dataExportInstances = null;

        DataExportInstanceDataManager dataExportInstanceDataManager = new DataExportInstanceDataManager(applicationConfiguration);
        try {

            dataExportInstances = dataExportInstanceDataManager.getDataExportInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            LoggingDataManager.logException(ex, applicationConfiguration);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return dataExportInstances;
    }

    public LinkedList<SearchDefinitonExecution> getSearchExecutionDefinitionListBySearchDefinition() {
        if (selectedSearchDefinition == null || "".equals(selectedSearchDefinition)) {
            return null;
        }
        int searchDefinitionID = Integer.parseInt(selectedSearchDefinition);
        LinkedList<SearchDefinitonExecution> searchExecutionList = null;
        try {
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);
            SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
            searchExecutionList = searchDefinitonExecutionDataProvider.getAllSearchDefinitionExecutionForSearchDefinition(searchDefinition);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
        return searchExecutionList;
    }

    public LinkedList<SearchDefinitionExecutionRun> getSearchExecutionDefinitionRunListBySearchDefinition() {
        if (selectedSearchDefinition == null || "".equals(selectedSearchDefinition)) {
            return null;
        }
        int searchDefinitionID = Integer.parseInt(selectedSearchDefinition);
        LinkedList<SearchDefinitionExecutionRun> searchExecutionRunList = null;
        try {
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(searchDefinitionID);
            SearchDefinitonExecutionRunDataManager searchDefinitonExecutionRunDataManager = new SearchDefinitonExecutionRunDataManager(applicationConfiguration);
            searchExecutionRunList = searchDefinitonExecutionRunDataManager.getAllSearchDefinitionExecutionRunForSearchDefinition(searchDefinition);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
        return searchExecutionRunList;
    }

    public LinkedList<SearchDefinition> getSearchDefinitions() {
        LinkedList<SearchDefinition> searchDefinitions = null;
        try {
            SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
            searchDefinitions = searchDefinitionDataProvider.getSearchDefinitions();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
        return searchDefinitions;
    }

    public LinkedList<SearchAnalyticDefinition> getSearchAnalyticDefintion() {
        LinkedList<SearchAnalyticDefinition> searchAnalyticDefinitionList = null;
        try {
            SearchAnalyticDefinitionDataManager searchAnalyticDefinitionDataManager = new SearchAnalyticDefinitionDataManager(applicationConfiguration);
            searchAnalyticDefinitionList = searchAnalyticDefinitionDataManager.getAnalyticDefinitions();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
        return searchAnalyticDefinitionList;
    }

    public Map<String, List<Object>> getScientificMetaInformationBySearchAnalyticDefinition() {
        Map<String, List<Object>> resultSetMap = null;

        try {
            if (selectedSearchAnalytic == null || "".equals(selectedSearchAnalytic)) {
                return resultSetMap;
            }
            if (selectedSearchDefinitionExecution == null || "".equals(selectedSearchDefinitionExecution)) {
                return resultSetMap;
            }

            int selectedSearchAnalyticID = Integer.parseInt(selectedSearchAnalytic);
            int selectedSearchDefinitionExecutionID = Integer.parseInt(selectedSearchDefinitionExecution);

            for (SearchAnalyticDefinition searchAnalyticDefinition : loadedSearchAnalyticDefinitionList) {
                if (searchAnalyticDefinition.getID() == selectedSearchAnalyticID) {
                    String queryBuffer = searchAnalyticDefinition.getQuery();
                    searchAnalyticDefinition.setQuery(String.format(searchAnalyticDefinition.getQuery(), selectedSearchDefinitionExecutionID));
                    ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataManager = new ScientificPaperMetaInformationDataManager(applicationConfiguration);
                    resultSetMap = scientificPaperMetaInformationDataManager.getScientificPaperMetaInformationResultSetBySearchAnalyticDefinition(searchAnalyticDefinition, null);
                    searchAnalyticDefinition.setQuery(queryBuffer);
                }
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
        return resultSetMap;
    }

    public void redirectToEditPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("SearchAnalyticDefinitionEdit.xhtml?SelectedSearchAnalyticDefinition=" + selectedSearchAnalytic);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }
    }

    public void deleteSearchAnalyticByID() {
        if (selectedSearchAnalytic == null || "".equals(selectedSearchAnalytic)) {
            return;
        }
        int id = Integer.parseInt(selectedSearchAnalytic);

        try {
            SearchAnalyticDefinitionDataManager searchAnalyticDefinitionDataManager = new SearchAnalyticDefinitionDataManager(applicationConfiguration);
            searchAnalyticDefinitionDataManager.delete(id);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LoggingDataManager.logException(ex, applicationConfiguration);

        }

    }

    public void export() {

        try {

            if (selectedExportInstance == null || "".equals(selectedExportInstance)) {
                return;
            }

            int selectedExportInstanceID = Integer.parseInt(selectedExportInstance);
            String excelExportDefaultFilename = "Excel_Export_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + ".xls";

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + excelExportDefaultFilename + "\"");

            DataExportInstanceDataManager dataExportInstanceDataManager = new DataExportInstanceDataManager(applicationConfiguration);
            DataExportInstance dataExportInstance = dataExportInstanceDataManager.getDataExportInstanceByID(selectedExportInstanceID);

            GroovyClassLoader gcl = new GroovyClassLoader();
            Class parsedGroocyClass = gcl.parseClass(StringEscapeUtils.unescapeJava(dataExportInstance.getGroovyCode()));

            Object groovyClassInstance = parsedGroocyClass.newInstance();

            IExportScientificPaperMetaInformation currentDataExportInstance = (IExportScientificPaperMetaInformation) groovyClassInstance;
            currentDataExportInstance.export(this.loadedSearchAnalyticsResultMap, externalContext.getResponseOutputStream());

            facesContext.responseComplete();

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            LoggingDataManager.logException(ex, applicationConfiguration);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }
}
