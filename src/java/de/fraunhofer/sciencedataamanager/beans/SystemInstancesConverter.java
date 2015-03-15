/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * This class converts the connector data. 
 * @author Moritz Mars
 */
@FacesConverter("SystemInstanceConverter")
public class SystemInstancesConverter implements Converter {
    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration(); 

    /**
     * The method returns the connector as a object, 
     * @param fc
     * @param uic
     * @param string the id of the object. 
     * @return
     */
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

    /**
     * Converts the object to a string. 
     * @param fc
     * @param uic
     * @param o
     * @return the string representation of the object. 
     */
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
       if (o == null) return null;
        return ((SystemInstance)o).getName();
    
    }
    
}
