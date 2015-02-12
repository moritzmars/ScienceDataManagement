/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

import java.sql.Timestamp;
import java.util.LinkedList;

/**
 *
 * @author Moritz Mars
 */
public class SearchDefinitionExecutionRun {

    private int ID;
    private String Description;
    private SearchExecution searchExecution;
    private LinkedList<SearchDefinitonExecution> searchDefinitionExecutionList = new LinkedList<SearchDefinitonExecution>();
    private Timestamp StartExecutionTimestamp;
    private Timestamp FinishedExecutionTimestamp;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public LinkedList<SearchDefinitonExecution> getSearchDefinitionExecutionList() {
        return searchDefinitionExecutionList;
    }

    public void setSearchDefinitionExecutionList(LinkedList<SearchDefinitonExecution> searchDefinitionExecutionList) {
        this.searchDefinitionExecutionList = searchDefinitionExecutionList;
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

    public SearchExecution getSearchExecution() {
        return searchExecution;
    }

    public void setSearchExecution(SearchExecution searchExecution) {
        this.searchExecution = searchExecution;
    }
}
