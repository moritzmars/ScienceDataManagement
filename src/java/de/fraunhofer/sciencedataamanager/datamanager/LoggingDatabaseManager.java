/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.LogLevel;
import de.fraunhofer.sciencedataamanager.domain.LoggingEntry;
import de.fraunhofer.sciencedataamanager.interfaces.ILoggingManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "loggingDataProvider")
@SessionScoped
public class LoggingDatabaseManager implements ILoggingManager {
    
    private ApplicationConfiguration applicationConfiguration; 
    public LoggingDatabaseManager(ApplicationConfiguration applicationConfiguration)
    {
       this.applicationConfiguration = applicationConfiguration; 
       
    }
    
    public void logException(Exception ex) {
        LoggingEntry loggingEntry = new LoggingEntry();
        loggingEntry.setLogLevel(LogLevel.FATAL);
        loggingEntry.setMessage(ex.toString());
        loggingEntry.setCreatedDate(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        LoggingDatabaseManager loggingDataProvider = new LoggingDatabaseManager(applicationConfiguration);
        loggingDataProvider.insert(loggingEntry);
    }
    
    public void insert(LoggingEntry loggingEntry) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = null;
            conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
            String sqlInsertStatement = "INSERT INTO logging_entries (Message, Category, CreatedDate)"
                    + " VALUES (?,?,?)";
            java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
            preparedStatement.setString(1, loggingEntry.getMessage());
            preparedStatement.setString(2, loggingEntry.getLogLevel().toString());
            preparedStatement.setTimestamp(3, loggingEntry.getCreatedDate());
            preparedStatement.execute();
            preparedStatement.close();
            conn.close();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public LinkedList<LoggingEntry> getLoggingEntries() throws Exception {
        LinkedList<LoggingEntry> loggingEntriesList = new LinkedList();
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM logging_entries order by ID DESC";
        
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            
            LoggingEntry loggingEntry = new LoggingEntry();
            loggingEntry.setID(rs.getInt("ID"));
            loggingEntry.setMessage(rs.getString("Message"));
            loggingEntry.setLogLevel(LogLevel.valueOf(rs.getString("Category")));
            loggingEntry.setCreatedDate(rs.getTimestamp("CreatedDate"));
            loggingEntriesList.add(loggingEntry);
        }
        
        st.close();
        rs.close();
        conn.close();
        return loggingEntriesList;
        
    }

    @Override
    public void log(String message, LogLevel logLevel) {
        LoggingEntry loggingEntry = new LoggingEntry();
        loggingEntry.setLogLevel(logLevel);
        loggingEntry.setMessage(message);
        loggingEntry.setCreatedDate(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        LoggingDatabaseManager loggingDataProvider = new LoggingDatabaseManager(applicationConfiguration);
        loggingDataProvider.insert(loggingEntry);
   }
    
}
