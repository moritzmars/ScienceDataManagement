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
import java.util.LinkedList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="searchExecution")
@SessionScoped
public class SearchExecution {
	
	private SearchDefinition searchDefiniton; 
	LinkedList<SystemInstance> systemInstances = new LinkedList<SystemInstance>();
	public SearchDefinition getSearchDefiniton() {
		return searchDefiniton;
	}
	public void setSearchDefiniton(SearchDefinition searchDefiniton) {
		this.searchDefiniton = searchDefiniton;
	}
	public LinkedList<SystemInstance> getSystemInstances() {
		return systemInstances;
	}
	public void setSystemInstances(LinkedList<SystemInstance> systemInstances) {
		this.systemInstances = systemInstances;
	} 
}
