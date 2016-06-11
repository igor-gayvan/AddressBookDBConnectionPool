/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addressbook.database.dao;

import addressbook.database.ConnectionPool;
import addressbook.subject.EntityAddressBook;
import addressbook.database.WrapperConnector;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Haivan
 */
public abstract class AbstractDAO<T extends EntityAddressBook> {

    protected Connection connection;

    protected WrapperConnector connector;

    protected PreparedStatement ps;
    protected CallableStatement cs;
    protected ResultSet resultSet;

    public AbstractDAO() {
    }

    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }

    // методы добавления, поиска, замены, удаления
    public abstract List<T> selectAll();

    public abstract T findEntityById(int id);

    public abstract boolean delete(int id);

    public abstract boolean delete(T entity);

    public abstract boolean insert(T entity);

    public abstract boolean update(T entity);

    public void getConnection() {
        try {
            connection = ConnectionPool.getInstance().getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(AbstractDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // методы закрытия коннекта и Statement
    public void close() {
        connector.closeConnection();
    }

    public void closeConnection() {
        if (ps != null) {
            closeStatement(ps);
        }

        if (cs != null) {
            closeStatement(cs);
        }

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                Logger.getLogger(AbstractDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            ConnectionPool.getInstance().returnConnection(connection);
        } catch (SQLException ex) {
            Logger.getLogger(AbstractDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void closeStatement(Statement statement) {
        connector.closeStatement(statement);
    }

}
