/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConstants;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.ApplicationLogMonitoringLevel;
import de.fraunhofer.sciencedataamanager.interfaces.IApplicationConfigurationDataProvider;
import java.util.prefs.Preferences;

/**
 *The class is an application configuration data provider which is based on the apache persistance layer. 
 * @author Moritz Mars
 */
public class ApplicationConfigurationPersistanceDataManager implements IApplicationConfigurationDataProvider {

    /**
     * Returns the application configuration. 
     * @return the application configuration. 
     */
    @Override
    public ApplicationConfiguration getApplicationConfiguration() {
        Preferences prefs = Preferences.userRoot().node("de/fraunhofer/sciencedataamanager/persistence");
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        applicationConfiguration.setSqlConnection(prefs.get(ApplicationConstants.PREFERENCE_KEY_SQL_CONNECTION_STRING, ""));
        applicationConfiguration.setApplicationLogMonitoringLevel(ApplicationLogMonitoringLevel.valueOf(prefs.get(ApplicationConstants.PREFERENCE_KEY_SQL_APPLICATION_LOG_MONITORING_LEVEL, "DEBUG")));
        applicationConfiguration.setLoggingManager(new LoggingDatabaseManager(applicationConfiguration));
        
        return applicationConfiguration;
    }

    /**
     * Sets the application configuration
     * @param applicationConfiguration the application configuration. 
     */
    @Override
    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        Preferences prefs = Preferences.userRoot().node("de/fraunhofer/sciencedataamanager/persistence");
        prefs.put(ApplicationConstants.PREFERENCE_KEY_SQL_CONNECTION_STRING, applicationConfiguration.getSqlConnection());
        prefs.put(ApplicationConstants.PREFERENCE_KEY_SQL_APPLICATION_LOG_MONITORING_LEVEL, applicationConfiguration.getApplicationLogMonitoringLevel().toString());
    }
}
