/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

import java.sql.Timestamp;

/**
 *
 * @author Moritz Mars
 */
public class LoggingEntry {

    private int ID;
    private String Message;
    private LogLevel logLevel;

    private Timestamp CreatedDate;

    public int getID() {
        return ID;
    }

    public String getMessage() {
        return Message;
    }

    public Timestamp getCreatedDate() {
        return CreatedDate;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setCreatedDate(Timestamp CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
    
}
