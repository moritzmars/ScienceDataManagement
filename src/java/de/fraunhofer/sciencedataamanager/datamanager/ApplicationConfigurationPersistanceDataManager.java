/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConstants;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.interfaces.IApplicationConfigurationDataProvider;
import java.util.prefs.Preferences;

/**
 *
 * @author Moritz Mars
 */
public class ApplicationConfigurationPersistanceDataManager implements IApplicationConfigurationDataProvider {

    @Override
    public ApplicationConfiguration getApplicationConfiguration() {
        Preferences prefs = Preferences.userRoot().node("de/fraunhofer/sciencedataamanager/persistence");
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        applicationConfiguration.setSqlConnection(prefs.get(ApplicationConstants.PREFERENCE_KEY_SQL_CONNECTION_STRING, ""));
        applicationConfiguration.setLoggingManager(new LoggingDatabaseManager(applicationConfiguration));
        return applicationConfiguration;
    }

    @Override
    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        Preferences prefs = Preferences.userRoot().node("de/fraunhofer/sciencedataamanager/persistence");
        prefs.put(ApplicationConstants.PREFERENCE_KEY_SQL_CONNECTION_STRING, applicationConfiguration.getSqlConnection());

    }
}
