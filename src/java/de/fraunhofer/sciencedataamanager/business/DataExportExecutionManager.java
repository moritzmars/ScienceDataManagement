/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.business;

import de.fraunhofer.sciencedataamanager.datamanager.DataExportInstanceDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.ScientificPaperMetaInformationDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionDataManager;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.DataExportInstance;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.exampes.export.ExcelDataExport;
import groovy.lang.GroovyClassLoader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "dataExportExecutionManager")
@SessionScoped
public class DataExportExecutionManager {

    private ApplicationConfiguration applicationConfiguration;

    /**
     * This class implements the data export logic.
     *
     * @param applicationConfiguration
     */
    public DataExportExecutionManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    /**
     *
     * @param searchDefintion the search definition to be exportet.
     * @param dataExportInstanceID the id of the data export instance.
     * @param outputStream the output stream.
     * @throws Exception during export.
     */
    public void export(SearchDefinition searchDefintion, int dataExportInstanceID, ExternalContext externalContext) throws Exception {

        SearchDefinitonExecutionDataManager searchDefinitonExecutionDataManager = new SearchDefinitonExecutionDataManager(applicationConfiguration);
        LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = searchDefinitonExecutionDataManager.getSearchDefinitionExecutionForSearchDefinition(searchDefintion);
        Map<String, Map<String, List<Object>>> allConnectorsToExport = new HashMap<String, Map<String, List<Object>>>();
        int i = 0;
        for (SearchDefinitonExecution currentSearchDefinitonExecution : searchDefinitonExecutionList)
        {
            ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataManager = new ScientificPaperMetaInformationDataManager(applicationConfiguration);
            Map<String, List<Object>> dataToExport = scientificPaperMetaInformationDataManager.getScientificMetaInformationBySearchDefinitionAsMap(currentSearchDefinitonExecution);
            allConnectorsToExport.put(currentSearchDefinitonExecution.getSystemInstance().getName(), dataToExport);
            i++;
        }
        DataExportInstanceDataManager dataExportInstanceDataManager = new DataExportInstanceDataManager(applicationConfiguration);
        DataExportInstance dataExportInstance = dataExportInstanceDataManager.getDataExportInstanceByID(dataExportInstanceID);

        String excelExportDefaultFilename = dataExportInstance.getExportFilePrefix() + new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + dataExportInstance.getExportFilePostfix();
        externalContext.setResponseContentType(dataExportInstance.getResponseContentType());
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + excelExportDefaultFilename + "\"");

        GroovyClassLoader gcl = new GroovyClassLoader();
        Class parsedGroocyClass = gcl.parseClass(StringEscapeUtils.unescapeJava(dataExportInstance.getGroovyCode()));

        Object groovyClassInstance = parsedGroocyClass.newInstance();
        ExcelDataExport currentDataExportInstance = new ExcelDataExport();
        //IExportScientificPaperMetaInformation currentDataExportInstance = (IExportScientificPaperMetaInformation) groovyClassInstance;
        currentDataExportInstance.export(allConnectorsToExport, externalContext.getResponseOutputStream());
    }

}
