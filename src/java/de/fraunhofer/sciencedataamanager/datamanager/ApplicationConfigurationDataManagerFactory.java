/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.interfaces.IApplicationConfigurationDataProvider;

/**
 *
 * @author Moritz Mars
 */
public class ApplicationConfigurationDataManagerFactory {
    public static IApplicationConfigurationDataProvider getApplicationConfigurationDataProvider(String[] settings)
    {
        return new ApplicationConfigurationPersistanceDataManager(); 
    }
    
}
