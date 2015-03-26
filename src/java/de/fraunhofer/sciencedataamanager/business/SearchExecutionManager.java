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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import de.fraunhofer.sciencedataamanager.interfaces.ICloudPaperConnector;
import groovy.lang.GroovyClassLoader;
import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.LinkedList;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * This class implements the search definition execution algorithmus. 
 * @author Moritz Mars
 */
@ManagedBean(name = "searchExecutionManager")
@SessionScoped
public class SearchExecutionManager{

    private ApplicationConfiguration applicationConfiguration;

    /**
     * The application configuration constructor. 
     * @param applicationConfiguration the current confguration. 
     */
    public SearchExecutionManager(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    ICloudPaperConnector currentExecutedConnector;

    /**
     * Executes the search definition.
     * @param searchDefinitionID the search definition to be executed. 
     * @throws Exception
     */
    public void execute(int searchDefinitionID) throws Exception {
        this.applicationConfiguration.getLoggingManager().log("Prepare execution by getting all required data from database.", LogLevel.DEBUG);
         
        LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = new LinkedList<SearchDefinitonExecution>();
        SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
        SearchDefinition searchDefinition = searchDefinitionDataProvider.getSearchDefinitionByID(searchDefinitionID);

        SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
        SearchExecution searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);

        SearchDefinitionExecutionRun  searchDefinitionExecutionRun = new SearchDefinitionExecutionRun();
        searchDefinitionExecutionRun.setDescription(searchDefinition.getName() + " - " + Calendar.getInstance().getTime().toString());
        searchDefinitionExecutionRun.setStartExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        searchDefinitionExecutionRun.setSearchExecution(searchExecution);
        searchDefinitionExecutionRun.setSearchDefinitionExecutionList(searchDefinitonExecutionList);

        SearchDefinitonExecutionRunDataManager searchDefinitonExecutionRunDataManager = new SearchDefinitonExecutionRunDataManager(applicationConfiguration);
        searchDefinitionExecutionRun = searchDefinitonExecutionRunDataManager.insertSearchDefinitionExecutionRunLastID(searchDefinitionExecutionRun);
        this.applicationConfiguration.getLoggingManager().log("System is now accessing each data source through connector.", LogLevel.DEBUG);

        for (SystemInstance connectorLoop : searchExecution.getSystemInstances()) {
            this.applicationConfiguration.getLoggingManager().log("Preparing connector " + connectorLoop.getName() +"for execution!", LogLevel.DEBUG);

            SearchDefinitonExecution searchDefinitonExecution = null;
            try {
                GroovyClassLoader gcl = new GroovyClassLoader();
                Class parsedGroocyClass = gcl.parseClass(StringEscapeUtils.unescapeJava(connectorLoop.getGroovyCode()));
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
                searchExecution.getSearchDefiniton().setSystemInstance(connectorLoop);
                this.applicationConfiguration.getLoggingManager().log("Crawling started for connector: " + connectorLoop.getName(), LogLevel.DEBUG);
                searchDefinitonExecution = currentExecutedConnector.getCloudPapers(searchExecution.getSearchDefiniton());
                this.applicationConfiguration.getLoggingManager().log("Crawling stopped for connector: " + connectorLoop.getName(), LogLevel.DEBUG);

                searchDefinitonExecution.setSearchState("Success");
                searchDefinitonExecution.setSearch_Definiton_ID(searchDefinitionID);
                searchDefinitonExecution.setSystemInstance(connectorLoop);
                searchDefinitionExecutionRun.getSearchDefinitionExecutionList().add(searchDefinitonExecution);
                 this.applicationConfiguration.getLoggingManager().log("Connector search finished. Deconstruct connector: " + connectorLoop.getName(), LogLevel.DEBUG);

            } catch (Exception ex) {
                 searchDefinitonExecution = new SearchDefinitonExecution();
                searchDefinitonExecution.setSearch_Definiton_ID(searchDefinitionID);
                searchDefinitonExecution.setSystemInstance(connectorLoop);
                searchDefinitonExecution.setSearchState("Failure");
                searchDefinitonExecution.setMessage(ex.toString());
                searchDefinitonExecution.setFinishedExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                searchDefinitionExecutionRun.getSearchDefinitionExecutionList().add(searchDefinitonExecution);
                this.applicationConfiguration.getLoggingManager().logException(ex);
                continue;
            }
        }
       this.applicationConfiguration.getLoggingManager().log("Start writing data to local database.", LogLevel.DEBUG);

        searchDefinitionExecutionRun.setFinishedExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        searchDefinitonExecutionRunDataManager.updateSearchDefinitionExecutionRun(searchDefinitionExecutionRun);

        for (SearchDefinitonExecution searchDefinitonExecution : searchDefinitionExecutionRun.getSearchDefinitionExecutionList()) {
            for (ScientificPaperMetaInformationParseException scientificPaperMetaInformationParseException : searchDefinitonExecution.getScientificPaperMetaInformationParseException()) {
                this.applicationConfiguration.getLoggingManager().logException(scientificPaperMetaInformationParseException.getParseException());
            }
        }
       this.applicationConfiguration.getLoggingManager().log("Stopped writing data to local database.", LogLevel.DEBUG);


    }
   /**
     * Executes the search definition.
     * @param searchDefinitionID the search definition to be executed. 
     * @throws Exception
     */
    public void executeOnlyInformations(int searchDefinitionID) throws Exception {
        this.applicationConfiguration.getLoggingManager().log("Entering methode (public void execute(int searchDefinitionID) throws Exception)", LogLevel.DEBUG);
         
        LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = new LinkedList<SearchDefinitonExecution>();
        SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
        SearchDefinition searchDefinition = searchDefinitionDataProvider.getSearchDefinitionByID(searchDefinitionID);

        SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
        SearchExecution searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);

        SearchDefinitionExecutionRun  searchDefinitionExecutionRun = new SearchDefinitionExecutionRun();
        searchDefinitionExecutionRun.setDescription(searchDefinition.getName() + " - " + Calendar.getInstance().getTime().toString());
        searchDefinitionExecutionRun.setStartExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        searchDefinitionExecutionRun.setSearchExecution(searchExecution);
        searchDefinitionExecutionRun.setSearchDefinitionExecutionList(searchDefinitonExecutionList);

        SearchDefinitonExecutionRunDataManager searchDefinitonExecutionRunDataManager = new SearchDefinitonExecutionRunDataManager(applicationConfiguration);
        searchDefinitionExecutionRun = searchDefinitonExecutionRunDataManager.insertSearchDefinitionExecutionRunLastID(searchDefinitionExecutionRun);

        for (SystemInstance connectorLoop : searchExecution.getSystemInstances()) {
            SearchDefinitonExecution searchDefinitonExecution = null;
            try {
                GroovyClassLoader gcl = new GroovyClassLoader();
                Class parsedGroocyClass = gcl.parseClass(StringEscapeUtils.unescapeJava(connectorLoop.getGroovyCode()));
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
                searchExecution.getSearchDefiniton().setSystemInstance(connectorLoop);
                searchExecution.getSearchDefiniton().setItemTreshhold(0);
                searchDefinitonExecution = currentExecutedConnector.getCloudPapers(searchExecution.getSearchDefiniton());
                searchDefinitonExecution.setSearchState("Success");
                searchDefinitonExecution.setSearch_Definiton_ID(searchDefinitionID);
                searchDefinitonExecution.setSystemInstance(connectorLoop);
                searchDefinitionExecutionRun.getSearchDefinitionExecutionList().add(searchDefinitonExecution);

            } catch (Exception ex) {
                 searchDefinitonExecution = new SearchDefinitonExecution();
                searchDefinitonExecution.setSearch_Definiton_ID(searchDefinitionID);
                searchDefinitonExecution.setSystemInstance(connectorLoop);
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
    /**
     * Returns the current progress. 
     * @return the current progress. 
     * @throws Exception
     */
    public int getProgressCurrentExecution() throws Exception {
        if (currentExecutedConnector == null) {
            return 0;
        }
        return currentExecutedConnector.getCurrentProgress();
    }
}
