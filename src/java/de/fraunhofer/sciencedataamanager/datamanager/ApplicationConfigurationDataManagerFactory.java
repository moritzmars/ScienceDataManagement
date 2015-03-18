/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.interfaces.IApplicationConfigurationDataProvider;

/**
 *This class is the factory for the application configuration data provider. 
 * @author Moritz Mars
 */
public class ApplicationConfigurationDataManagerFactory {

    /**
     * Returns the current application configuration data provider. 
     * @param settings settings to generate the data provider. 
     * @return the application configuration data provider. 
     */
    public static IApplicationConfigurationDataProvider getApplicationConfigurationDataProvider(String[] settings)
    {
        return new ApplicationConfigurationPersistanceDataManager(); 
    }
    
}
