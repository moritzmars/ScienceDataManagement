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
public class SearchFieldMapping {

    private int ID; 
    private String MetaFieldName; 
    private String FieldName; 
    
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMetaFieldName() {
        return MetaFieldName;
    }

    public void setMetaFieldName(String MetaFieldName) {
        this.MetaFieldName = MetaFieldName;
    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String FieldName) {
        this.FieldName = FieldName;
    }
    
}
