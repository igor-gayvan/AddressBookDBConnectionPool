/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addressbook.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor Gayvan
 */
public class ConnectionPool {

    private static final int MAX_POOL_SIZE = 10;
    private static final int WAITING_TIME = 1000;

    private static ConnectionPool instance;
    private static BlockingQueue<Connection> connections;
    private static int countConnections;

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        try {
            if (instance == null) {
                instance = init();
            }
        } finally {
            return instance;
        }
    }

    private static ConnectionPool init() {

        WrapperConnector connector = new WrapperConnector();

        connections = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
        connections.offer(connector.getConnection());
        countConnections++;

        return new ConnectionPool();
    }

    public Connection getConnection() {
        Connection connection = null;
        connection = connections.poll();
        if (connection == null) {
            if (countConnections < MAX_POOL_SIZE) {
                WrapperConnector connector = new WrapperConnector();
                connection = connector.getConnection();
                
                countConnections++;
            } else {
                try {
                    connection = connections.take();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return connection;
    }

    /**
     * This method returns a connection to connection pool
     */
    public void returnConnection(Connection connection) {
        connections.offer(connection);
    }

    /**
     * This method tries to close remaining connections of connection pool
     */
    public void closeConnections() {
        int remainingConnections = countConnections;
        while (remainingConnections > 0) {
            try {
                connections.poll(WAITING_TIME, TimeUnit.MILLISECONDS).close();
            } catch (InterruptedException | SQLException ex) {
                Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
