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
import de.fraunhofer.sciencedataamanager.interfaces.IExportScientificPaperMetaInformation;
import groovy.lang.GroovyClassLoader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "dataExportExecutionManager")
@SessionScoped
public class DataExportExecutionManager {

    private ApplicationConfiguration applicationConfiguration;

    public DataExportExecutionManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public void export(SearchDefinition searchDefintion, int dataExportInstanceID, OutputStream outputStream) throws Exception {
        
        SearchDefinitonExecutionDataManager searchDefinitonExecutionDataManager = new SearchDefinitonExecutionDataManager(applicationConfiguration); 
        LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = searchDefinitonExecutionDataManager.getSearchDefinitionExecutionForSearchDefinition(searchDefintion);
        
        ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataManager = new ScientificPaperMetaInformationDataManager(applicationConfiguration); 
        Map<String, List<Object>>dataToExport = scientificPaperMetaInformationDataManager.getScientificMetaInformationBySearchDefinitionAsMap(searchDefinitonExecutionList.get(0));
        
        DataExportInstanceDataManager dataExportInstanceDataManager = new DataExportInstanceDataManager(applicationConfiguration);
        DataExportInstance dataExportInstance = dataExportInstanceDataManager.getDataExportInstanceByID(dataExportInstanceID);
        
        GroovyClassLoader gcl = new GroovyClassLoader();
        Class parsedGroocyClass = gcl.parseClass(StringEscapeUtils.unescapeJava(dataExportInstance.getGroovyCode()));

        Object groovyClassInstance = parsedGroocyClass.newInstance();
        ExcelDataExport currentDataExportInstance = new ExcelDataExport(); 
         //IExportScientificPaperMetaInformation currentDataExportInstance = (IExportScientificPaperMetaInformation) groovyClassInstance;
         currentDataExportInstance.export(dataToExport,outputStream);
    }

}
