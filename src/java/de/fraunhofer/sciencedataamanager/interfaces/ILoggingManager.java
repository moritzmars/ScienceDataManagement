/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.interfaces;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.LogLevel;

/**
 *
 * @author Moritz Mars
 */
public interface ILoggingManager {

    public void logException(Exception ex);
    public void log(String message, LogLevel logLevel);

}
