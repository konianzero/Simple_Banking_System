package banking.dao;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

abstract class AbstractDAO implements DAO {

    private final SQLiteDataSource dataSource;

    public AbstractDAO(String databaseName) {
        String pathToDatabase = "./";
        String url = String.format("jdbc:sqlite:%s%s", pathToDatabase, databaseName);

        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
    }

    protected Connection createConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return connection;
    }
}

