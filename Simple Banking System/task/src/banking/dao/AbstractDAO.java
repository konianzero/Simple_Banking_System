package banking.dao;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

abstract class AbstractDAO implements DAO {

    private SQLiteDataSource dataSource;
    private Connection connection;
    private Statement statement;

    public AbstractDAO(String databaseName) {
        String pathToDatabase = "./";
        String url = "jdbc:sqlite:" + pathToDatabase + databaseName;

        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
    }

    protected ResultSet select(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    protected void update(String query) throws SQLException {
        statement.executeUpdate(query);
    }

    protected void statementPreparation() {
        createConnection();
        createStatement();
    }

    protected void statementCompletion() {
        closeStatement();
        closeConnection();
    }

    private void createConnection() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    private void createStatement() {
        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    private void closeStatement() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
}

