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
public class ScientificPaperMetaInformationAffiliation {
    private int ID; 
    private String Identifier_1;
    private String Url; 
    private String Name;
    private String Country;
    private String City; 
    private int Author_Count; 
    private int Document_Count; 
    private int Scientific_Paper_Meta_Information_ID;
    
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getIdentifier_1() {
        return Identifier_1;
    }

    public void setIdentifier_1(String Identifier_1) {
        this.Identifier_1 = Identifier_1;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public int getAuthor_Count() {
        return Author_Count;
    }

    public void setAuthor_Count(int Author_Count) {
        this.Author_Count = Author_Count;
    }

    public int getDocument_Count() {
        return Document_Count;
    }

    public void setDocument_Count(int Document_Count) {
        this.Document_Count = Document_Count;
    }

    public int getScientific_Paper_Meta_Information_ID() {
        return Scientific_Paper_Meta_Information_ID;
    }

    public void setScientific_Paper_Meta_Information_ID(int Scientific_Paper_Meta_Information_ID) {
        this.Scientific_Paper_Meta_Information_ID = Scientific_Paper_Meta_Information_ID;
    }
}
