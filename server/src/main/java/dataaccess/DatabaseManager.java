package dataaccess;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    private static final String[] CREATE_STATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS  user_data (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256),
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  auth_data (
              `username` varchar(256) NOT NULL,
              `auth_token` varchar(256) NOT NULL,
              PRIMARY KEY (`auth_token`),
              FOREIGN KEY (`username`) REFERENCES user_data(`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  game_data (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `white_username` varchar(256) DEFAULT NULL,
              `black_username` varchar(256) DEFAULT NULL,
              `game_name` varchar(256) NOT NULL,
              `chess_game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              FOREIGN KEY (`white_username`) REFERENCES user_data(`username`),
              FOREIGN KEY (`black_username`) REFERENCES user_data(`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try{
            try (InputStream in = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties props = new Properties();
                props.load(in);
                databaseName = props.getProperty("db.name");
                dbUsername = props.getProperty("db.user");
                dbPassword = props.getProperty("db.password");

                String host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);

            }
        }catch (Exception e) {
            throw new RuntimeException("unable to process db.properties. " + e.getMessage());
        }
        try {
            createDatabase();
            configureDatabaseTables();
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }


    public static void configureDatabaseTables() throws DataAccessException{
        try(Connection conn = getConnection()){
            for(String statement : CREATE_STATEMENTS) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to initialize database tables");
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }

    public static void clearTable(String table){
        String[] deleteStatements = {
                "SET FOREIGN_KEY_CHECKS = 0",
                "TRUNCATE " + table,
                "SET FOREIGN_KEY_CHECKS = 1"
        };
        try(var conn = DatabaseManager.getConnection()) {
            for(String deleteStatement : deleteStatements) {
                try (var preparedDeleteStatement = conn.prepareStatement(deleteStatement)) {
                    preparedDeleteStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException("Error accessing database: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database: " + e.getMessage());
        }
    }
}
