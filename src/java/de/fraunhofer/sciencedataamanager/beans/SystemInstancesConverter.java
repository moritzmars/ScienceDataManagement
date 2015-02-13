/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDatabaseManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Moritz Mars
 */
@FacesConverter("SystemInstanceConverter")
public class SystemInstancesConverter implements Converter {
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration(); 

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try {
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            Collection<SystemInstance> systemInstances = systemInstanceDataProvider.getSystemInstances();
            for(SystemInstance currentSystemInstance:systemInstances)
            {
                if(currentSystemInstance.getName().equals(string))
                return currentSystemInstance;
            }
        } catch (Exception ex) {
            Logger.getLogger(SystemInstancesConverter.class.getName()).log(Level.SEVERE, null, ex);
               this.applicationConfiguration.getLoggingManager().logException(ex);
        }
     return null;
    
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
       if (o == null) return null;
        return ((SystemInstance)o).getName();
    
    }
    
}
