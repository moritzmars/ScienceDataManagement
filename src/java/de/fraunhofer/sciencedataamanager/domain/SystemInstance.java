/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "systemInstance")
@SessionScoped
/**
 *
 * @author Moritz Mars
 */
public class SystemInstance {

    private int ID;
    private String Name;
    private String GroovyCode;
    private String CreateBy;
    private String ChangedBy;
    private Timestamp CreateDate;
    private Timestamp ChangedDate;
    private LinkedList<SearchFieldMapping> searchFieldMappings; 

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

    public String getGroovyCode() {
        return GroovyCode;
    }

    public void setGroovyCode(String groovyCode) {
        GroovyCode = groovyCode;
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

    public Timestamp getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Timestamp createDate) {
        CreateDate = createDate;
    }

    public Timestamp getChangedDate() {
        return ChangedDate;
    }

    public void setChangedDate(Timestamp changedDate) {
        ChangedDate = changedDate;
    }
    public LinkedList<SearchFieldMapping> getSearchFieldMappings() {
        return searchFieldMappings;
    }

    public void setSearchFieldMappings(LinkedList<SearchFieldMapping> searchFieldMappings) {
        this.searchFieldMappings = searchFieldMappings;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SystemInstance)) {
            return false;
        }
        SystemInstance objectToCheck = (SystemInstance) obj;
        
        return this.getID() == objectToCheck.getID();
    }

    @Override
    public int hashCode() {
        return (this.getID());
    }

}
