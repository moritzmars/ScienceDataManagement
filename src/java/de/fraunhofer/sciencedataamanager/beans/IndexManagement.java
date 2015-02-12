/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "indexManagement")
@SessionScoped
public class IndexManagement {

    private String tabSelected;

    public String getTabSelected() {
        return tabSelected;
    }

    public void setTabSelected(String tabSelected) {
        this.tabSelected = tabSelected;
    }

    public void valueChanged() {
   String componentId ="";
    }
}
