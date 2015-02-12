/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

import java.util.LinkedList;
import java.sql.Timestamp;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "searchDefinitonExecution")
@SessionScoped

public class SearchDefinitonExecution {

    private int ID;

    private String requestUrl;
    private String query;
    private Timestamp ExecutionTime;
    private int TotalItems;
    private int CrawledItems;
    private int UpdatedItems;
    private int NewItems;
    private int LocalItemsFound;
    private int Search_Definiton_ID;
    private String ExecutedBy;
    private Timestamp StartExecutionTimestamp;
    private Timestamp FinishedExecutionTimestamp;
    private LinkedList<ScientificPaperMetaInformation> scientificPaperMetaInformation = new LinkedList<ScientificPaperMetaInformation>();
    private SystemInstance systemInstance;
    private String searchExecutionState;
    private String message;
    private int Search_Definition_Execution_Run_ID;

    private LinkedList<ScientificPaperMetaInformationParseException> scientificPaperMetaInformationParseException = new LinkedList<ScientificPaperMetaInformationParseException>();

    public LinkedList<ScientificPaperMetaInformationParseException> getScientificPaperMetaInformationParseException() {
        return scientificPaperMetaInformationParseException;
    }

    public void setScientificPaperMetaInformationParseException(LinkedList<ScientificPaperMetaInformationParseException> scientificPaperMetaInformationParseException) {
        this.scientificPaperMetaInformationParseException = scientificPaperMetaInformationParseException;
    }

 
    public int getSearch_Definition_Execution_Run_ID() {
        return Search_Definition_Execution_Run_ID;
    }

    public void setSearch_Definition_Execution_Run_ID(int Search_Definition_Execution_Run_ID) {
        this.Search_Definition_Execution_Run_ID = Search_Definition_Execution_Run_ID;
    }

    public String getSearchExecutionState() {
        return searchExecutionState;
    }

    public void setSearchExecutionState(String searchExecutionState) {
        this.searchExecutionState = searchExecutionState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSearchState() {
        return searchExecutionState;
    }

    public void setSearchState(String searchExecutionState) {
        this.searchExecutionState = searchExecutionState;
    }

    public Timestamp getStartExecutionTimestamp() {
        return StartExecutionTimestamp;
    }

    public void setStartExecutionTimestamp(Timestamp StartExecutionTimestamp) {
        this.StartExecutionTimestamp = StartExecutionTimestamp;
    }

    public Timestamp getFinishedExecutionTimestamp() {
        return FinishedExecutionTimestamp;
    }

    public void setFinishedExecutionTimestamp(Timestamp FinishedExecutionTimestamp) {
        this.FinishedExecutionTimestamp = FinishedExecutionTimestamp;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public Timestamp getExecutionTime() {
        return ExecutionTime;
    }

    public void setExecutionTime(Timestamp executionTime) {
        ExecutionTime = executionTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getCrawledItems() {
        return CrawledItems;
    }

    public void setCrawledItems(int CrawledItems) {
        this.CrawledItems = CrawledItems;
    }

    public int getTotalItems() {
        return TotalItems;
    }

    public void setTotalItems(int totalItems) {
        TotalItems = totalItems;
    }

    public int getUpdatedItems() {
        return UpdatedItems;
    }

    public void setUpdatedItems(int updatedItems) {
        UpdatedItems = updatedItems;
    }

    public int getNewItems() {
        return NewItems;
    }

    public void setNewItems(int newItems) {
        NewItems = newItems;
    }

    public int getSearch_Definiton_ID() {
        return Search_Definiton_ID;
    }

    public void setSearch_Definiton_ID(int search_Definiton_ID) {
        Search_Definiton_ID = search_Definiton_ID;
    }

    public String getExecutedBy() {
        return ExecutedBy;
    }

    public void setExecutedBy(String executedBy) {
        ExecutedBy = executedBy;
    }

    public LinkedList<ScientificPaperMetaInformation> getScientificPaperMetaInformation() {
        return scientificPaperMetaInformation;
    }

    public void setScientificPaperMetaInformation(
            LinkedList<ScientificPaperMetaInformation> scientificPaperMetaInformation) {
        this.scientificPaperMetaInformation = scientificPaperMetaInformation;
    }

    public SystemInstance getSystemInstance() {
        return systemInstance;
    }

    public void setSystemInstance(SystemInstance systemInstance) {
        this.systemInstance = systemInstance;
    }

    public int getLocalItemsFound() {
        return LocalItemsFound;
    }

    public void setLocalItemsFound(int LocalItemsFound) {
        this.LocalItemsFound = LocalItemsFound;
    }

}
