/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.interfaces;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;

/**
 * By implementing the interface the implementing class is able to provide an Application Configuration object
 * @author Moritz Mars
 */
public interface IApplicationConfigurationDataProvider {
    public ApplicationConfiguration getApplicationConfiguration(); 
    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration); 
    
}
