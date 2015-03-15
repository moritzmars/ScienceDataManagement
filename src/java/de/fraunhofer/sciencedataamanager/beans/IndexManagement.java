/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Implementation of the index web site.
 * @author Moritz Mars
 */
@ManagedBean(name = "indexManagement")
@SessionScoped
public class IndexManagement {

    private String tabSelected;

    /**
     * Returns the selected tab. 
     * @return the selected tab. 
     */
    public String getTabSelected() {
        return tabSelected;
    }

    /**
     * Sets the selected tab. 
     * @param The selected tab. 
     */
    public void setTabSelected(String tabSelected) {
        this.tabSelected = tabSelected;
    }

    /**
     * Methode is executed, as value changed.
     */
    public void valueChanged() {
   String componentId ="";
    }
}
