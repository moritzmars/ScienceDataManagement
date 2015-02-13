/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.interfaces.IApplicationConfigurationDataProvider;

/**
 *
 * @author Moritz Mars
 */
public class ApplicationConfigurationCodeDataManager implements IApplicationConfigurationDataProvider {


    @Override
    public ApplicationConfiguration getApplicationConfiguration() {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(); 
        applicationConfiguration.setSqlConnection("jdbc:mysql://MMDESK:3306/sciencedatamanagement?user=scndatamgr&password=55555moritz7");
        applicationConfiguration.setLoggingManager(new LoggingDatabaseManager(applicationConfiguration));
        return applicationConfiguration;
    }

    @Override
    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
