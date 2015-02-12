/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "scientificPaperMetaInformationAuthors")
@SessionScoped
public class ScientificPaperMetaInformationAuthors {

    private int ID;
    private String GivenName;
    private String SurName;
    private int Scientific_Paper_Meta_Information_ID;

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getGivenName() {
        return GivenName;
    }

    public void setGivenName(String givenName) {
        GivenName = givenName;
    }

    public String getSurName() {
        return SurName;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }

    public int getScientific_Paper_Meta_Information_ID() {
        return Scientific_Paper_Meta_Information_ID;
    }

    public void setScientific_Paper_Meta_Information_ID(int Scientific_Paper_Meta_Information_ID) {
        this.Scientific_Paper_Meta_Information_ID = Scientific_Paper_Meta_Information_ID;
    }

}
