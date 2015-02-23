/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.business;

import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionRunDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchExecutionDataManager;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.LogLevel;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformationParseException;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitionExecutionRun;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import de.fraunhofer.sciencedataamanager.domain.SearchExecution;
import de.fraunhofer.sciencedataamanager.examples.connectors.ElsevierScienceDirectConnectorBufferAbstract;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import de.fraunhofer.sciencedataamanager.interfaces.ICloudPaperConnector;
import groovy.lang.GroovyClassLoader;
import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.LinkedList;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchExecutionManager")
@SessionScoped
public class SearchExecutionManager {

    private ApplicationConfiguration applicationConfiguration;

    public SearchExecutionManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    ICloudPaperConnector currentExecutedConnector;

    public void execute(int searchDefinitionID) throws Exception {
        this.applicationConfiguration.getLoggingManager().log("Entering methode (public void execute(int searchDefinitionID) throws Exception)", LogLevel.DEBUG);
         
        LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = new LinkedList<SearchDefinitonExecution>();
        SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
        SearchDefinition searchDefinition = searchDefinitionDataProvider.getSearchDefinitionByID(searchDefinitionID);

        SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
        SearchExecution searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);

        SearchDefinitionExecutionRun searchDefinitionExecutionRun = new SearchDefinitionExecutionRun();
        searchDefinitionExecutionRun.setDescription(searchDefinition.getName() + " - " + Calendar.getInstance().getTime().toString());
        searchDefinitionExecutionRun.setStartExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        searchDefinitionExecutionRun.setSearchExecution(searchExecution);
        searchDefinitionExecutionRun.setSearchDefinitionExecutionList(searchDefinitonExecutionList);

        SearchDefinitonExecutionRunDataManager searchDefinitonExecutionRunDataManager = new SearchDefinitonExecutionRunDataManager(applicationConfiguration);
        searchDefinitionExecutionRun = searchDefinitonExecutionRunDataManager.insertSearchDefinitionExecutionRunLastID(searchDefinitionExecutionRun);

        for (SystemInstance systemInstanceLoop : searchExecution.getSystemInstances()) {
            SearchDefinitonExecution searchDefinitonExecution = null;
            try {
                GroovyClassLoader gcl = new GroovyClassLoader();
                Class parsedGroocyClass = gcl.parseClass(StringEscapeUtils.unescapeJava(systemInstanceLoop.getGroovyCode()));
                Class[] constructorParameterConnector = new Class[1];
                constructorParameterConnector[0] = this.applicationConfiguration.getClass();
                //Object groovyClassInstance = parsedGroocyClass.newInstance(constructorParameterConnector);
                Object groovyClassInstance= null;
                Constructor connectorConstructor = parsedGroocyClass.getDeclaredConstructor(constructorParameterConnector);
                if (connectorConstructor != null) {
                    groovyClassInstance= connectorConstructor.newInstance(this.applicationConfiguration);
                } else {
                    groovyClassInstance=parsedGroocyClass.newInstance();
                }
                currentExecutedConnector = (ICloudPaperConnector) groovyClassInstance;
                //currentExecutedConnector = new ElsevierScienceDirectConnectorBufferAbstract(this.applicationConfiguration); 
                //currentExecutedConnector = new WebOfScienceLightConnector(this.applicationConfiguration); 
                searchDefinitonExecution = currentExecutedConnector.getCloudPapers(searchExecution.getSearchDefiniton());
                searchDefinitonExecution.setSearchState("Success");
                searchDefinitonExecution.setSearch_Definiton_ID(searchDefinitionID);
                searchDefinitonExecution.setSystemInstance(systemInstanceLoop);
                searchDefinitionExecutionRun.getSearchDefinitionExecutionList().add(searchDefinitonExecution);

            } catch (Exception ex) {
                 searchDefinitonExecution = new SearchDefinitonExecution();
                searchDefinitonExecution.setSearch_Definiton_ID(searchDefinitionID);
                searchDefinitonExecution.setSystemInstance(systemInstanceLoop);
                searchDefinitonExecution.setSearchState("Failure");
                searchDefinitonExecution.setMessage(ex.toString());
                searchDefinitonExecution.setFinishedExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                searchDefinitionExecutionRun.getSearchDefinitionExecutionList().add(searchDefinitonExecution);
                this.applicationConfiguration.getLoggingManager().logException(ex);
                continue;
            }
        }
        searchDefinitionExecutionRun.setFinishedExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        searchDefinitonExecutionRunDataManager.updateSearchDefinitionExecutionRun(searchDefinitionExecutionRun);

        for (SearchDefinitonExecution searchDefinitonExecution : searchDefinitionExecutionRun.getSearchDefinitionExecutionList()) {
            for (ScientificPaperMetaInformationParseException scientificPaperMetaInformationParseException : searchDefinitonExecution.getScientificPaperMetaInformationParseException()) {
                this.applicationConfiguration.getLoggingManager().logException(scientificPaperMetaInformationParseException.getParseException());
            }
        }

    }

    public int getProgressCurrentExecution() throws Exception {
        if (currentExecutedConnector == null) {
            return 0;
        }
        return currentExecutedConnector.getCurrentProgress();
    }
}
