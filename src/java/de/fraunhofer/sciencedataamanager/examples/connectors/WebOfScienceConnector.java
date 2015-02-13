/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.examples.connectors;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.interfaces.ICloudPaperConnector;

/**
 *
 * @author Moritz Mars
 */
public class WebOfScienceConnector  implements ICloudPaperConnector {
    
   private ApplicationConfiguration applicationConfiguration; 
    public WebOfScienceConnector(ApplicationConfiguration applicationConfiguration)
    {
       this.applicationConfiguration = applicationConfiguration; 
    }    
    
    @Override
    public SearchDefinitonExecution getCloudPapers(SearchDefinition cloudSearchPattern) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getCurrentProgress() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
