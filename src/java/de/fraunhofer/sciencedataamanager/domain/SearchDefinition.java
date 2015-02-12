/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

/**
 *
 * @author Moritz Mars
 */
import java.sql.Timestamp;
import java.util.LinkedList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "searchDefinition")
@SessionScoped

public class SearchDefinition {

    private int ID;
    private String Name;
    private String CreateBy;
    private String ChangedBy;
    private Timestamp CreatedDate;
    private Timestamp ChangedDate;
    private LinkedList<SearchTerm> searchTerms = new LinkedList<SearchTerm>();

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String createBy) {
        CreateBy = createBy;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public Timestamp getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        CreatedDate = createdDate;
    }

    public Timestamp getChangedDate() {
        return ChangedDate;
    }

    public void setChangedDate(Timestamp changedDate) {
        ChangedDate = changedDate;
    }

    public LinkedList<SearchTerm> getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(LinkedList<SearchTerm> searchTerms) {
        this.searchTerms = searchTerms;
    }

     public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SearchDefinition)) {
            return false;
        }
        SearchDefinition objectToCheck = (SearchDefinition) obj;
        
        return this.getID() == objectToCheck.getID();
    }

}
