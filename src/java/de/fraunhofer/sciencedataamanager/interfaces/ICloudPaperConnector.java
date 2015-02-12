/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.interfaces;

import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;

/**
 *
 * @author Moritz Mars
 */
public interface ICloudPaperConnector {
	public SearchDefinitonExecution getCloudPapers(SearchDefinition cloudSearchPattern) throws Exception;
        public int getCurrentProgress() throws Exception; 
}
