/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "systemInstanceDataProvider")
@SessionScoped
public class SystemInstanceDataManager {
       private ApplicationConfiguration applicationConfiguration; 

    /**
     *
     * @param applicationConfiguration
     */
    public SystemInstanceDataManager(ApplicationConfiguration applicationConfiguration)
    {
       this.applicationConfiguration = applicationConfiguration; 
    }

    /**
     *
     * @param systemInstance
     * @throws Exception
     */
    public void updateSystemInstance(SystemInstance systemInstance) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String sqlInsertStatement = "Update system_instance set Name=?, GroovyCode =? where ID =?";

        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, systemInstance.getName());
        preparedStatement.setString(2, systemInstance.getGroovyCode());
        preparedStatement.setInt(3, systemInstance.getID());
        
        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
         SearchFieldMappingManager searchFieldMappingManager = new SearchFieldMappingManager(this.applicationConfiguration); 
        searchFieldMappingManager.updateSearchFieldMappings(systemInstance);
       
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public SystemInstance getSystemInstanceByID(int id) throws Exception {
        SystemInstance systemInstance = null;

        systemInstance = new SystemInstance();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM system_instance where ID=" + id;

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            systemInstance.setID(rs.getInt("ID"));
            systemInstance.setName(rs.getString("Name"));
            systemInstance.setGroovyCode(rs.getString("GroovyCode"));

        }
        
        st.close();
        rs.close();
        conn.close();
        
        SearchFieldMappingManager searchFieldMappingManager = new SearchFieldMappingManager(this.applicationConfiguration); 
        systemInstance.setSearchFieldMappings(searchFieldMappingManager.getFieldMappingBySystemInstance(systemInstance));
        return systemInstance;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public LinkedList<SystemInstance> getSystemInstances() throws Exception {
        LinkedList<SystemInstance> systemInstanceList = new LinkedList<SystemInstance>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM system_instance";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            SystemInstance systemInstance = new SystemInstance();
            systemInstance.setID(rs.getInt("ID"));
            systemInstance.setName(rs.getString("Name"));
            systemInstance.setGroovyCode(rs.getString("GroovyCode"));

            systemInstanceList.add(systemInstance);
        }
        st.close();
        rs.close();
        conn.close();

        return systemInstanceList;
    }

    /**
     *
     * @param systemInstance
     * @throws Exception
     */
    public void insert(SystemInstance systemInstance) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String sqlInsertStatement = "INSERT INTO system_instance (Name, GroovyCode)"
                + " VALUES (?,?)";
        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, systemInstance.getName());
        preparedStatement.setString(2, systemInstance.getGroovyCode());
        preparedStatement.execute();

        preparedStatement.close();
        conn.close();
    }

    /**
     *
     * @param id
     * @throws Exception
     */
    public void delete(int id) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "delete from from system_instance where ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        preparedStatement.execute();

        preparedStatement.close();
        conn.close();

    }

}
