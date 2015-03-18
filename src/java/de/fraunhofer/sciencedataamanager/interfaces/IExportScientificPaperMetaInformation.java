/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.interfaces;

import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Moritz Mars
 */
public interface IExportScientificPaperMetaInformation {
    public void export(Map<String, Map<String, List<Object>>> allConnectorsToExport, OutputStream outputStream) throws Exception;

}
