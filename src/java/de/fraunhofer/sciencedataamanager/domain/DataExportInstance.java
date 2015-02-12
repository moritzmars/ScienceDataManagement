/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

import java.sql.Timestamp;

/**
 *
 * @author Moritz Mars
 */
public class DataExportInstance {

    private int ID;
    private String Name;
    private String GroovyCode;
    private String CreateBy;
    private String ChangedBy;
    private Timestamp CreateDate;
    private Timestamp ChangedDate;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getGroovyCode() {
        return GroovyCode;
    }

    public void setGroovyCode(String GroovyCode) {
        this.GroovyCode = GroovyCode;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String CreateBy) {
        this.CreateBy = CreateBy;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String ChangedBy) {
        this.ChangedBy = ChangedBy;
    }

    public Timestamp getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Timestamp CreateDate) {
        this.CreateDate = CreateDate;
    }

    public Timestamp getChangedDate() {
        return ChangedDate;
    }

    public void setChangedDate(Timestamp ChangedDate) {
        this.ChangedDate = ChangedDate;
    }
}
