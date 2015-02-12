/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.datamanager;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.DataExportInstance;
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
public class DataExportInstanceDataManager {
       private ApplicationConfiguration applicationConfiguration; 
    public DataExportInstanceDataManager(ApplicationConfiguration applicationConfiguration)
    {
       this.applicationConfiguration = applicationConfiguration; 
    }
    public void updateDataExportInstance(DataExportInstance dataExportInstance) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String sqlInsertStatement = "Update data_export set Name=?, GroovyCode =? where ID =?";

        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, dataExportInstance.getName());
        preparedStatement.setString(2, dataExportInstance.getGroovyCode());
        preparedStatement.setInt(3, dataExportInstance.getID());

        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
    }

    public DataExportInstance getDataExportInstanceByID(int id) throws Exception {
        DataExportInstance dataExportInstance = null;

        dataExportInstance = new DataExportInstance();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM data_export where ID=" + id;

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            dataExportInstance.setID(rs.getInt("ID"));
            dataExportInstance.setName(rs.getString("Name"));
            dataExportInstance.setGroovyCode(rs.getString("GroovyCode"));

        }

        st.close();
        rs.close();
        conn.close();
        return dataExportInstance;
    }

    public LinkedList<DataExportInstance> getDataExportInstances() throws Exception {
        LinkedList<DataExportInstance> dataExportInstanceList = new LinkedList<DataExportInstance>();

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "SELECT * FROM data_export";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {

            DataExportInstance dataExportInstance = new DataExportInstance();
            dataExportInstance.setID(rs.getInt("ID"));
            dataExportInstance.setName(rs.getString("Name"));
            dataExportInstance.setGroovyCode(rs.getString("GroovyCode"));

            dataExportInstanceList.add(dataExportInstance);
        }
        st.close();
        rs.close();
        conn.close();

        return dataExportInstanceList;
    }

    public void insert(DataExportInstance dataExportInstance) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String sqlInsertStatement = "INSERT INTO data_export (Name, GroovyCode)"
                + " VALUES (?,?)";
        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
        preparedStatement.setString(1, dataExportInstance.getName());
        preparedStatement.setString(2, dataExportInstance.getGroovyCode());
        preparedStatement.execute();

        preparedStatement.close();
        conn.close();
    }

    public void delete(int id) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        conn = DriverManager.getConnection(this.applicationConfiguration.getSqlConnection());
        String query = "delete from data_export where id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        preparedStatement.execute();

        preparedStatement.close();
        conn.close();

    }

}
